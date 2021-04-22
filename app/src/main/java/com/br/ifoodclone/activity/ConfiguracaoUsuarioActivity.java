package com.br.ifoodclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.br.ifoodclone.R;
import com.br.ifoodclone.helpers.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoUsuarioActivity extends AppCompatActivity {
 private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_usuario);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        // Configurações da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações usuário");
        setSupportActionBar(toolbar);
        // Para mostrar a seta de voltar para home
        // Necessário configurar no AndroidManifests
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}