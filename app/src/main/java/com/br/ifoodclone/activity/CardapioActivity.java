package com.br.ifoodclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.ifoodclone.R;
import com.br.ifoodclone.adapters.AdapterProduto;
import com.br.ifoodclone.helpers.ConfiguracaoFirebase;
import com.br.ifoodclone.helpers.usuarioFirebase;
import com.br.ifoodclone.model.Empresa;
import com.br.ifoodclone.model.Produto;
import com.br.ifoodclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CardapioActivity extends AppCompatActivity {
    private RecyclerView recyclerProdutoscardapio;
    private ImageView imageEmpresaCardapio;
    private TextView textNomeEmpresaCardapio,textEntregaEmpresa,textTempoEmpresa,textCategoriaEmpresa;
    private Empresa empresaSelecionada;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idEmpresa;
    private AlertDialog dialog;
    private String idUsuarioLogado;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = usuarioFirebase.getIdUsuario();

        // Recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();

        if( bundle != null){
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");
            textNomeEmpresaCardapio.setText(empresaSelecionada.getNome());
            textCategoriaEmpresa.setText(empresaSelecionada.getCategoria());
            textTempoEmpresa.setText("Tempo entrega " +empresaSelecionada.getTempo());
            textEntregaEmpresa.setText("Taxa entrega R$  " + empresaSelecionada.getPrecoEntrega().toString());
            idEmpresa = empresaSelecionada.getIdUsuario();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imageEmpresaCardapio);
        }

        // Configurações da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        // Para mostrar a seta de voltar para home(empresaActivity)
        // Necessário configurar no AndroidManisfest
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerProdutoscardapio.setLayoutManager( new LinearLayoutManager(this ) );
        recyclerProdutoscardapio.setHasFixedSize( true );
        adapterProduto = new AdapterProduto( produtos, this );
        recyclerProdutoscardapio.setAdapter( adapterProduto );

        // Recuperando os produtos do Firebase
        recuperarProdutos();
        recuperarDadosUsuario();
    }

    private void recuperarDadosUsuario() {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Carregando dados").setCancelable(false).build();
        dialog.show();

        DatabaseReference usuarioRef = firebaseRef.child("usuario").child(idUsuarioLogado);


        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperarPedido();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarPedido() {
        dialog.dismiss();
    }

    // Método responsável por recuperar os Produtos
    private void recuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child( idEmpresa );

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear(); // Limpando a lista antes de começarmos

                // Laço responsável por trazer todos os produtos da empresa
                for (DataSnapshot ds: snapshot.getChildren()) {
                    produtos.add( ds.getValue( Produto.class ) );
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Criando as opções do menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);

        return super.onCreateOptionsMenu(menu);
    }
    // Verificando a opção selecionada no menu pelo usuário
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuPedido:

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarComponentes(){
        recyclerProdutoscardapio = findViewById(R.id.recyclerProdutoCardapio);
        imageEmpresaCardapio = findViewById(R.id.imageEmpresaCardapio);
        textNomeEmpresaCardapio = findViewById(R.id.textNomeEmpresaCardapio);
        textCategoriaEmpresa = findViewById(R.id.textCategoriaEmpresa);
        textEntregaEmpresa = findViewById(R.id.textEntregaEmpresa);
        textTempoEmpresa = findViewById(R.id.textTempoEmpresa);
    }
}