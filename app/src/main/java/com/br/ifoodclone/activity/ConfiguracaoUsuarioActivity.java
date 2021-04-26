package com.br.ifoodclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.ifoodclone.R;
import com.br.ifoodclone.helpers.ConfiguracaoFirebase;
import com.br.ifoodclone.helpers.usuarioFirebase;
import com.br.ifoodclone.model.Empresa;
import com.br.ifoodclone.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome, editUsuarioEnderenco, editUsuarioCep, editUsuarioCidade;
    private ImageView imagemUsuarioPerfil;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private String urlImagemSelecionada = "";
    private static final int SELECAO_GALERIA = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_usuario);
        inicializacomponentes();

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = usuarioFirebase.getIdUsuario();
        // Configurações da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações usuário");
        setSupportActionBar(toolbar);
        // Para mostrar a seta de voltar para home
        // Necessário configurar no AndroidManifests
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void validarDadosUsuario(View View) {
        // Valida se os campos foram preenchidos
        String nome = editUsuarioNome.getText().toString();
        String endereco = editUsuarioEnderenco.getText().toString();
        String cep = editUsuarioCep.getText().toString();
        String cidade = editUsuarioCidade.getText().toString();

        if (!nome.isEmpty()) {
            if (!endereco.isEmpty()) {
                if (!cep.isEmpty()) {
                    if (!cidade.isEmpty()) {
                        // salvando os dados da empresa
                        Usuario usuario = new Usuario();
                        usuario.setIdUsuario(idUsuarioLogado);
                        usuario.setNome(nome);
                        usuario.setEndereco(endereco);
                        usuario.setCep(cep);
                        usuario.setCidade(cidade);
                        usuario.setUrlImagem(urlImagemSelecionada);
                        usuario.salvar();
                        exibirMensagem("Dados salvos com sucesso!");
                        finish();
                        startActivity(new Intent(this, HomeActivity.class));
                    } else {
                        exibirMensagem("Digite a cidade e o estado!");
                    }

                } else {
                    exibirMensagem("Digite o seu CEP!");
                }

            } else {
                exibirMensagem("Digite seu endereço!");
            }
        } else {
            exibirMensagem("Digite seu nome completo!");
        }
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void inicializacomponentes() {
        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioEnderenco = findViewById(R.id.editUsuarioEndereco);
        editUsuarioCep = findViewById(R.id.editUsuarioCep);
        editUsuarioCidade = findViewById(R.id.editusuarioCidade);
        imagemUsuarioPerfil = findViewById(R.id.imagemPerfilUsuario);
    }
}