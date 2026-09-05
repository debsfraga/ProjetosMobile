package br.edu.unisenai.rangonaregua;

import static br.edu.unisenai.rangonaregua.MainActivity.listaLugar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.unisenai.rangonaregua.model.Lugar;


public class NovoLugarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_novo_lugar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbarNovo);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        //carrgar os componentes
        EditText edtNome = findViewById(R.id.edtNome);
        EditText edtCategoria = findViewById(R.id.edtCategoria);
        EditText edtPreco = findViewById(R.id.edtPreco);
        EditText edtObservacao = findViewById(R.id.edtObservacao);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(v -> {
            if (edtNome.getText().toString().isEmpty()) {
                edtNome.setError("Obrigatório");
            } else if (edtCategoria.getText().toString().isEmpty()) {
                edtCategoria.setError("Obrigatório");
            } else if (edtPreco.getText().toString().isEmpty()) {
                edtPreco.setError("Obrigatório");
            } else {
                //Gravar Dados
                Lugar novo = new Lugar(edtNome.getText().toString(),
                        edtCategoria.getText().toString(),
                        Double.parseDouble(edtPreco.getText().toString()),
                        edtObservacao.getText().toString(),
                        0);
                listaLugar.add(novo);
                finish();
            }
        });

    }
}
