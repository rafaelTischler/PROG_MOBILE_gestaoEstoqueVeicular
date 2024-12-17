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

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

public class telaLogin extends AppCompatActivity {

    EditText edit_login, edit_senha;
    Button btn_entrar;
    TextView lbl_cadastrar;
    String login = "";
    String senha = "";
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tela_login);
        edit_login = findViewById(R.id.edit_login);
        edit_senha = findViewById(R.id.edit_senha);
        btn_entrar = findViewById(R.id.btn_entrar);
        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviaJSONPostLoginSenha().execute();
            }
        });
        lbl_cadastrar = findViewById(R.id.lbl_cadastrar);
        lbl_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), telaCadastro.class);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    class EnviaJSONPostLoginSenha extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String url = "http://192.168.0.101/estoqueVeiculos/consulta_login.php";
                JSONObject jsonValores = new JSONObject();
                jsonValores.put("login", edit_login.getText().toString());
                jsonValores.put("senha", edit_senha.getText().toString());
                conexao mandar = new conexao();
                String mensagem = conexao.PostJSONObject(url, jsonValores);
                try {
                    JSONObject jsonObject = new JSONObject(mensagem);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        login = jsonItem.optString("email").toString();
                        senha = jsonItem.optString("senha").toString();
                        userId = jsonItem.optString("id").toString();
                    }
                    if ((edit_login.getText().toString().equals(login)) && (edit_senha.getText().toString().equals(senha))) {
                        Intent i = new Intent(getApplicationContext(), menuPrincipal.class);
                        i.putExtra("userId", userId);
                        startActivity(i);
                    } else {
                        Toast.makeText(telaLogin.this, "Login ou senha incorretos", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    Toast.makeText(telaLogin.this, "Problemas ao tentar conectar ao banco de dados", Toast.LENGTH_LONG).show();
                }
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
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }
}
