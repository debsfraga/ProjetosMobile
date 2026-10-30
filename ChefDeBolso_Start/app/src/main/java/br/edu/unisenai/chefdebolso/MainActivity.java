package br.edu.unisenai.chefdebolso;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.unisenai.chefdebolso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String CHAVE_MENSAGENS = "mensagens";

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tratarBordas();

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setSubtitle(getString(R.string.subtitulo_modelo, Config.MODELO));



    }

    private void tratarBordas() {
        int baseTopo = binding.toolbar.getPaddingTop();
        int baseBaixo = binding.painelEntrada.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(binding.raiz, (v, insets) -> {
            Insets barras = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            binding.toolbar.setPadding(barras.left, baseTopo + barras.top, barras.right, 0);
            binding.painelEntrada.setPadding(
                    binding.painelEntrada.getPaddingLeft(),
                    binding.painelEntrada.getPaddingTop(),
                    binding.painelEntrada.getPaddingRight(),
                    baseBaixo + barras.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }


}
