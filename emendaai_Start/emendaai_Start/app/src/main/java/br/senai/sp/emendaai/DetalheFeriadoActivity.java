package br.senai.sp.emendaai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.senai.sp.emendaai.api.BrasilAPIService;
import br.senai.sp.emendaai.model.Endereco;
import br.senai.sp.emendaai.model.Feriado;
import br.senai.sp.emendaai.util.Datas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetalheFeriadoActivity extends AppCompatActivity {


    private EditText campoCep;
    private ProgressBar progressoCep;
    private TextView txtResultadoCep;
    private Button btnCompartilhar, btnBuscarCep;
    private Endereco pontoEncontro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_feriado);

        TextView txtNome = findViewById(R.id.txtNomeFeriado);
        TextView txtData = findViewById(R.id.txtDataFeriado);
        TextView txtContagem = findViewById(R.id.txtContagemFeriado);
        TextView txtEmenda = findViewById(R.id.txtAvisoEmenda);

        campoCep = findViewById(R.id.campoCep);
        progressoCep = findViewById(R.id.progressoCep);
        txtResultadoCep = findViewById(R.id.txtResultadoCep);
        btnCompartilhar = findViewById(R.id.btnCompartilhar);
        btnBuscarCep = findViewById(R.id.btnBuscarCep);

        Feriado feriado = (Feriado) getIntent().getSerializableExtra("feriado");
        txtNome.setText(feriado.getNome().toString());
        txtData.setText(Datas.porExtenso(feriado.getData()));
        txtContagem.setText(Datas.contagem(feriado.getData()));
        txtEmenda.setVisibility(Datas.ehEmenda(feriado.getData()) ? View.VISIBLE : View.GONE);

        btnCompartilhar.setOnClickListener(view -> {
            String convite = "Role no feriado!!! Bora :) " + '\n' + '\n' +
                    feriado.getNome() + " - " + Datas.porExtenso(feriado.getData()) + '\n' +
                    "Ponto de Encontro:" + pontoEncontro.toString();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, convite);
            startActivity(Intent.createChooser(share, "Compartilhar"));
        });

        btnBuscarCep.setOnClickListener(v -> {
            String cep = campoCep.getText().toString();

            //Ativar RETROFIT
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://brasilapi.com.br/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            BrasilAPIService api = retrofit.create(BrasilAPIService.class);
            // --fim

            Call<Endereco> requisicao = api.bucarEndereco(cep);
            requisicao.enqueue(new Callback<Endereco>() {
                @Override
                public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                    if (response.body() != null) {
                        txtResultadoCep.setVisibility(View.VISIBLE);
                        txtResultadoCep.setText(response.body().toString());
                        btnCompartilhar.setEnabled(true);
                        pontoEncontro = response.body();
                    } else {
                        Toast.makeText(DetalheFeriadoActivity.this,
                                "Endereço não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Endereco> call, Throwable t) {
                    Toast.makeText(DetalheFeriadoActivity.this,
                            "Erro de conexão", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
