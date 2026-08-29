package com.deboraf.meuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btOla = findViewById(R.id.btOla);
        btOla.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        Button btCiclo = findViewById(R.id.btCiclo);
        btCiclo.setOnClickListener(v -> {
            Intent rota = new Intent(this, Ciclo.class);
            startActivity(rota);
        });

        Button bt3 = findViewById(R.id.btLayout);
        bt3.setOnClickListener(v -> {
            Intent rota = new Intent(this, LinearLayout.class);
            startActivity(rota);
        });

        Button bt4 = findViewById(R.id.btNetflix);
        bt4.setOnClickListener(v -> {
            Intent rota = new Intent(this, Netflix.class);
            startActivity(rota);
        });
    }
}