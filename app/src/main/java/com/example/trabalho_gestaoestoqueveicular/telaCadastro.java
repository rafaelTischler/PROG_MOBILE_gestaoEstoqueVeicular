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

import java.net.URLEncoder;
import java.util.Iterator;

public class telaCadastro extends AppCompatActivity {

    TextView lbl_telaLogin;
    Button btn_cadastrar;
    EditText edit_nome, edit_email, edit_telefone, edit_senha;
    usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tela_cadastro);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new usuario(edit_nome.getText().toString(), edit_senha.getText().toString(), edit_email.getText().toString(), edit_telefone.getText().toString());
                new EnviaJSONPost().execute();
                Toast.makeText(telaCadastro.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), telaLogin.class);
                startActivity(intent);
            }
        });
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_telefone = findViewById(R.id.edit_telefone);
        edit_senha = findViewById(R.id.edit_senha);
        lbl_telaLogin = findViewById(R.id.lbl_telaMenu);
        lbl_telaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), telaLogin.class);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    class EnviaJSONPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String url = "http://192.168.0.104/estoqueVeiculos/cadastra_usuario.php";
                JSONObject jsonValores = new JSONObject();
                jsonValores.put("nome", user.getNome());
                jsonValores.put("email", user.getEmail());
                jsonValores.put("telefone", user.getTelefone());
                jsonValores.put("senha", user.getSenha());
                conexao mandar = new conexao();
                String mensagem = mandar.PostJSONObject(url, jsonValores);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getPostDataString(JSONObject params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                Object value = params.get(key);
                if (first) first = false;
                else result.append("&");
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }
}