package com.example.trabalho_gestaoestoqueveicular;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menuPrincipal extends AppCompatActivity {

    Button btn_cadastrarVeiculo, btn_consultarVeiculo, btn_alterarVeiculo, btn_removerVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_principal);
        btn_cadastrarVeiculo = findViewById(R.id.btn_cadVeiculo);
        btn_cadastrarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), cadastroVeiculo.class);
                startActivity(intent);
            }
        });
        btn_consultarVeiculo = findViewById(R.id.btn_consVeiculo);
        btn_alterarVeiculo = findViewById(R.id.btn_alterarVeiculo);
        btn_removerVeiculo = findViewById(R.id.btn_removerVeiculo);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}