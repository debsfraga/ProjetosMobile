package com.deboraf.meuapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ciclo extends AppCompatActivity {

    private int contador = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ciclo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("CICLO", "onCreate");

        Button bt = findViewById(R.id.btContar);
        TextView txt = findViewById(R.id.txtSaida);

        bt.setOnClickListener(v -> {
            contador++;
            txt.setText(String.valueOf(contador));
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("contador", contador);
        Log.d("CICLOx", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contador = savedInstanceState.getInt("contador");
        Log.d("CICLOx", "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CICLOx", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CICLOx", "onResume");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CICLOx", "onStop");
    }


}