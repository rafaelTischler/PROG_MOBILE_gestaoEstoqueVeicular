package com.example.trabalho_gestaoestoqueveicular;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class cadastroVeiculo extends AppCompatActivity {

    Button btn_cadastrar;
    EditText edit_marca, edit_modelo, edit_cor, edit_ano, edit_comb;
    TextView lbl_voltar;
    automovel auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_veiculo);
        btn_cadastrar = findViewById(R.id.btn_cadastrarVeiculo);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto = new automovel(edit_marca.getText().toString(), edit_modelo.getText().toString(), edit_ano.getText().toString(), edit_cor.getText().toString(), edit_comb.getText().toString());
                new EnviaJSONPostVeiculo().execute();
                Toast.makeText(cadastroVeiculo.this, "Veículo cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), menuPrincipal.class);
                startActivity(intent);
            }
        });
        edit_marca = findViewById(R.id.edit_marca);
        edit_modelo = findViewById(R.id.edit_modelo);
        edit_cor = findViewById(R.id.edit_cor);
        edit_ano = findViewById(R.id.edit_ano);
        edit_comb = findViewById(R.id.edit_comb);
        lbl_voltar = findViewById(R.id.lbl_telaMenu);
        lbl_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), menuPrincipal.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    class EnviaJSONPostVeiculo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String url = "http://192.168.137.1/estoqueVeiculos/cadastra_veiculo.php";
                JSONObject jsonValores = new JSONObject();
                jsonValores.put("marca", auto.getMarca());
                jsonValores.put("modelo", auto.getModelo());
                jsonValores.put("ano", auto.getAno());
                jsonValores.put("cor", auto.getCor());
                jsonValores.put("combustivel", auto.getCombustivel());
                conexao mandar = new conexao();
                String mensagem = mandar.PostJSONObject(url, jsonValores);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}