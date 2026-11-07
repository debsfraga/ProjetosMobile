package br.edu.unisenai.chefdebolso;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import br.edu.unisenai.chefdebolso.databinding.ActivityMainBinding;
import br.edu.unisenai.chefdebolso.ia.ChefIA;
import br.edu.unisenai.chefdebolso.model.Mensagem;
import br.edu.unisenai.chefdebolso.ui.MensagemAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String CHAVE_MENSAGENS = "mensagens";

    private ActivityMainBinding binding;

    private ChefIA chef;
    private MensagemAdapter adapter;
    private boolean respondendo = false;
    private Mensagem msgAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tratarBordas();

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setSubtitle(getString(R.string.subtitulo_modelo, Config.MODELO));

        //Carregar a configuração de IA
        chef = new ChefIA(this);

        //Config do RecycleView
        RecyclerView rvMensgens = findViewById((R.id.rvMensagens));
        rvMensgens.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MensagemAdapter();
        rvMensgens.setAdapter(adapter);

        TextInputEditText msg = findViewById(R.id.etMensagem);
        LinearLayout telafundo = findViewById(R.id.estadoVazio);
        MaterialButton bt = findViewById((R.id.btnEnviar));

        bt.setOnClickListener(v -> {
            telafundo.setVisibility(View.GONE);

            if(respondendo){
                if(msgAtual !=null && msgAtual.estaVazia()){
                    msgAtual.substituirTexto("Resposta Interrompida");
                    adapter.atualizar(msgAtual);
                    respondendo=false;
                }
            } else {
                enviar(msg.getText().toString());
                msg.setText("");

            }

        });
    }
    private void enviar(String texto) {
        if (texto.isEmpty() || respondendo){
            return;
        }

        respondendo = true;
        adapter.adicionar(Mensagem.doUsuario(texto));
        Mensagem resposta = Mensagem.doChef("");
        msgAtual = resposta;
        adapter.adicionar(resposta);


        chef.conversar(texto, new ChefIA.OuvinteConversa() {
            @Override
            public void aoReceberPalavra(String palavra) {
                resposta.acrescentar(palavra);
                adapter.atualizar(resposta);
            }

            @Override
            public void aoTerminar() {
                respondendo = false;
            }
        });


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
