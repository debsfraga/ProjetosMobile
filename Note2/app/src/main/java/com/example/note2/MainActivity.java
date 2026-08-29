package com.example.note2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btAdicionar;
    private List<Nota> listaNotas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //configurar o RecycleView
        RecyclerView rvNota = findViewById(R.id.rvNota);
        rvNota.setLayoutManager(new LinearLayoutManager(this));
        //rvNota.setLayoutManager(new GridLayoutManager(this,2));

        //carregar os dados no RecyclerView
        AdapterNota adapter = new AdapterNota(listaNotas);
        rvNota.setAdapter(adapter);

        btAdicionar = findViewById(R.id.btAdicionar);
        btAdicionar.setOnClickListener(v -> {
        //Lógica para adicionar uma nova nota
        View tela = LayoutInflater.from(this).inflate(R.layout.tela_adicionar,null,false);
            EditText campoTitulo = tela.findViewById(R.id.campoTitulo);
            EditText campoDescricao = tela.findViewById(R.id.campoDescricao);
        new MaterialAlertDialogBuilder(this)
                .setTitle("Adicionar Nota")
                .setView(tela)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Adicionar", (DialogInterface dialog, int which) -> {
                    Nota item = new Nota(campoTitulo.getText().toString(), campoDescricao.getText().toString());

                    listaNotas.add(item);
                    adapter.notifyDataSetChanged();
                }).show();
        });
    }
}