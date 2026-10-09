package com.aula.muralfilmes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aula.muralfilmes.adapter.FilmeAdapter;
import com.aula.muralfilmes.model.Filme;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.List;


public class MainActivity extends AppCompatActivity implements FilmeAdapter.OnFilmeClickListener {

    private String meuNome;

    private FirebaseFirestore db;
    private FilmeAdapter adapter;
    private ListenerRegistration registro;
    private TextView txtVazio;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Carregar dados do usuario
        carregarNome();

        // Ação do botão de adicionar
        FloatingActionButton fab = findViewById(R.id.fabAdicionar);
        fab.setOnClickListener(v -> mostrarDialogoNovoFilme());

        db = FirebaseFirestore.getInstance();
        txtVazio = findViewById(R.id.txtVazio);

        RecyclerView listaFilmes = findViewById(R.id.listaFilmes);
        listaFilmes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FilmeAdapter(this);
        listaFilmes.setAdapter(adapter);

        escutarFilmes();
    }

    private void escutarFilmes() {
        registro = db.collection("filmes")
                .orderBy("votos", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, erro) -> {
                    if (erro != null || snapshot == null) {
                        return;
                    }
                    List<Filme> filmes = snapshot.toObjects(Filme.class);
                    adapter.setFilmes(filmes);
                    txtVazio.setVisibility(filmes.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registro != null) {
            registro.remove();
        }
    }

    @Override
    public void onVotar(Filme filme) {
        db.collection("filmes")
                .document(filme.getId())
                .update("votos", FieldValue.increment(1));
    }

    @Override
    public void onExcluir(Filme filme) {
        if (!TextUtils.equals(meuNome, filme.getIndicadoPor())) {
            Toast.makeText(this, "Esse filme é de " + filme.getIndicadoPor(),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Excluir")
                .setMessage("Tirar \"" + filme.getTitulo() + "\" do mural?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Excluir", (d, w) -> {
                    db.collection("filmes").document(filme.getId()).delete();
                    Snackbar.make(findViewById(R.id.main), "Filme removido",
                            Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }


    private void carregarNome() {
        SharedPreferences localNome = getSharedPreferences("nomeUsuario", MODE_PRIVATE);
        meuNome = localNome.getString("nomeUsuario", null);

        if (meuNome != null && !meuNome.equals("")) {
            Toast.makeText(this, "Bem-vindo(a) " + meuNome, Toast.LENGTH_LONG).show();
            return;
        }

        EditText campo = new EditText(this);
        campo.setHint("Seu nome");

        new AlertDialog.Builder(this)
                .setTitle("Quem é você")
                .setMessage("Vai aparecer ao lado dos filmes que você indicar.")
                .setView(campo)
                .setCancelable(false)
                .setPositiveButton("Pronto",(dialogInterface, i) -> {
                    meuNome = campo.getText().toString();
                    localNome.edit().putString("nomeUsuario", meuNome).apply();

                }).show();

    }


    private void mostrarDialogoNovoFilme() {
        View form = LayoutInflater.from(this).inflate(R.layout.tela_filme, null);
        EditText campoTitulo = form.findViewById(R.id.campoTitulo);
        EditText campoGenero = form.findViewById(R.id.campoGenero);

        new AlertDialog.Builder(this)
                .setTitle("Indicar filme")
                .setView(form)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Adicionar", (d, w) -> {
                    String titulo = campoTitulo.getText().toString().trim();
                    String genero = campoGenero.getText().toString().trim();

                    if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(genero)) {
                        Toast.makeText(this, "Preencha título e gênero", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Grava na coleção "filmes" do Firestore
                    db.collection("filmes").add(new Filme(titulo, genero, meuNome));
                })
                .show();
    }
}