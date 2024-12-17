package com.example.trabalho_gestaoestoqueveicular;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class consultaVeiculo extends AppCompatActivity {

    Spinner spMarca, spModelo;
    ListView listVeiculos;
    TextView lbl_menu;
    Button btnBuscar;
    List<String> marcas = new ArrayList<>();
    List<String> modelos = new ArrayList<>();
    List<String> veiculos = new ArrayList<>();
    List<automovel> automoveis = new ArrayList<>();
    ArrayAdapter<String> adapterMarca, adapterModelo, adapterVeiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_veiculo);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        spMarca = findViewById(R.id.sp_marca);
        spModelo = findViewById(R.id.sp_modelo);
        listVeiculos = findViewById(R.id.list_veiculos);
        lbl_menu = findViewById(R.id.lbl_telaMenu);
        lbl_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), menuPrincipal.class);
                startActivity(intent);
            }
        });
        btnBuscar = findViewById(R.id.btn_cadastrarVeiculo);
        new CarregarDados().execute();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void preencherSpinners() {
        for (automovel auto : automoveis) {
            if (!marcas.contains(auto.getMarca())) {
                marcas.add(auto.getMarca());
            }
        }

        adapterMarca = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marcas);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(adapterMarca);

        spMarca.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                modelos.clear();
                String marcaSelecionada = marcas.get(position);
                for (automovel auto : automoveis) {
                    if (auto.getMarca().equals(marcaSelecionada) && !modelos.contains(auto.getModelo())) {
                        modelos.add(auto.getModelo());
                    }
                }
                adapterModelo = new ArrayAdapter<>(consultaVeiculo.this, android.R.layout.simple_spinner_item, modelos);
                adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spModelo.setAdapter(adapterModelo);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        btnBuscar.setOnClickListener(v -> {
            veiculos.clear();
            String marca = spMarca.getSelectedItem().toString();
            String modelo = spModelo.getSelectedItem().toString();

            for (automovel auto : automoveis) {
                if (auto.getMarca().equals(marca) && auto.getModelo().equals(modelo)) {
                    veiculos.add("id: " + auto.getId() + "\nMarca: " + auto.getMarca() + "\nModelo: " + auto.getModelo() + "\nAno: " + auto.getAno() + "\nCor: " + auto.getCor() + "\nCombustível: " + auto.getCombustivel() +
                            "\nCadastrado por: " + auto.getUsuario() + "\nData de cadastro: " + auto.getDataCad());
                }
            }

            adapterVeiculos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, veiculos);
            listVeiculos.setAdapter(adapterVeiculos);
        });
    }

    private class CarregarDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultado = new StringBuilder();
            try {
                URL url = new URL("http://192.168.0.101/estoqueVeiculos/consulta_automoveis.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String linha;
                while ((linha = reader.readLine()) != null) {
                    resultado.append(linha);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultado.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            try {
                JSONArray jsonArray = new JSONArray(resultado);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    automoveis.add(new automovel(
                            obj.getInt("id"),
                            obj.getString("marca"),
                            obj.getString("modelo"),
                            obj.getString("ano"),
                            obj.getString("cor"),
                            obj.getString("combustivel"),
                            obj.getString("usuario_nome"),
                            obj.getString("data_cadastro")
                    ));
                }
                preencherSpinners();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
