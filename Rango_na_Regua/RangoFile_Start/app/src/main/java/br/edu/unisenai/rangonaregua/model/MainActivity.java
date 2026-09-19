package br.edu.unisenai.rangonaregua.model;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import br.edu.unisenai.rangonaregua.R;
import br.edu.unisenai.rangonaregua.adapter.LugarAdapter;
import br.edu.unisenai.rangonaregua.data.LugarRepository;


public class MainActivity extends AppCompatActivity implements LugarAdapter.Acao {

    private LugarRepository repository;
    private ListenerRegistration registro;

    static List<Lugar> listaLugar = new ArrayList<>();

    LugarAdapter adapter;

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


        // Carregar o Database
        //listaLugar = Catalogo.inicial();
        repository = new LugarRepository();


        FloatingActionButton btNovo = findViewById(R.id.fabNovo);
        btNovo.setOnClickListener(v -> {
            Intent intent = new Intent(this, NovoLugarActivity.class);
            startActivity(intent);
        });

        //Carregar o recycle
        RecyclerView rvLugares = findViewById(R.id.rvLugares);
        rvLugares.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LugarAdapter(listaLugar, this);
        rvLugares.setAdapter(adapter);

        //Configurar o deslizar
        configDeslizar();

    }

    private void configDeslizar(){
        ItemTouchHelper.SimpleCallback deslizar = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicao = viewHolder.getAdapterPosition();
                Lugar item = listaLugar.get(posicao);
                repository.excluir(item);

                //Desfazer - voltar o item
                Snackbar.make(findViewById(R.id.rvLugares), "Lugar Removido", Snackbar.LENGTH_LONG)
                        .setAction("Desfazer", v -> repository.restaurar(item))
                        .show();

            }
        };

        new ItemTouchHelper(deslizar).attachToRecyclerView(findViewById(R.id.rvLugares));
    }

    @Override
    protected void onResume(){
        super.onResume();

        registro = repository.lerRealTime((value, error) -> {
          if (error != null) {
              Log.e("ERRO", error.getMessage());
              return;
          }

          listaLugar.clear();
          listaLugar.addAll(value.toObjects(Lugar.class));
          adapter.notifyDataSetChanged();

        });
    }

    @Override
    public void votar(Lugar lugar){
        //lugar.setVotos(lugar.getVotos() +1);
        //Catalogo.ordenarPorVotos(listaLugar);
        // adapter.notifyDataSetChanged();

        repository.votar(lugar);

    }

    @Override
    public void detalhes(Lugar lugar){
        Intent rota = new Intent(this, DetalheActivity.class);
        rota.putExtra("obj", lugar);
        startActivity(rota);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSair) {
            FirebaseAuth autenticar = FirebaseAuth.getInstance();
            autenticar.signOut();
            Intent rota = new Intent(this, LoginActivity.class);
            startActivity(rota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
