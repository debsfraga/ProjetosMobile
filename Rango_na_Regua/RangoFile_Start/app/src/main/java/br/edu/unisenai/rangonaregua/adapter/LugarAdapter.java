package br.edu.unisenai.rangonaregua.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.unisenai.rangonaregua.R;
import br.edu.unisenai.rangonaregua.model.Lugar;

public class LugarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Lugar> lugares;
    private static final int CARD_LIDER = 0;
    private static final int CARD_NORMAL = 1;

    //Interface para tratar os cliques
    public interface Acao {
        void votar(Lugar lugar);
        void detalhes(Lugar lugar);
    }
    private Acao acao;
    //  --FIM


    public LugarAdapter(List<Lugar> lugares, Acao acao) {
        this.lugares = lugares;
        this.acao = acao;
    }

    @Override
    public int getItemViewType(int position){
        return position == 0 ? CARD_LIDER : CARD_NORMAL;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CARD_LIDER){
            View tela = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lider, parent, false);
            return new ViewHolderLider(tela);
        } else {
            View tela = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lugar, parent, false);
            return new ViewHolder(tela);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lugar item = lugares.get(position);

        if(holder instanceof ViewHolderLider){
            ViewHolderLider vh = (ViewHolderLider) holder;
            vh.txtNomeLider.setText(item.getNome());
            vh.txtCategoriaLider.setText(item.getCategoria());
            vh.txtPrecoLider.setText("R$ " + item.getPrecoMedio());
            vh.txtVotosLider.setText(" " + item.getVotos() + " votos");
            vh.txtObsLider.setText(item.getObservacao());
            vh.btnVotarLider.setOnClickListener(v -> acao.votar(item));
            vh.itemView.setOnClickListener(v -> acao.detalhes(item));

        }else {
            ViewHolder vh = (ViewHolder) holder;
            vh.txtPosicao.setText(String.valueOf(position + 1));
            vh.txtNome.setText(item.getNome());
            vh.txtCategoria.setText(item.getCategoria());
            vh.txtPreco.setText("R$ " + item.getPrecoMedio());
            vh.txtVotos.setText(" " + item.getVotos() + " votos");
            vh.btnVotar.setOnClickListener(v -> acao.votar(item));
            vh.itemView.setOnClickListener(v -> acao.detalhes(item));
        }

    }

    @Override
    public int getItemCount() {
        return lugares.size();
    }

    //Carregar os componentes de tela no java
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPosicao, txtNome, txtCategoria, txtPreco, txtVotos;
        Button btnVotar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtPosicao = itemView.findViewById(R.id.txtPosicao);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            txtVotos = itemView.findViewById(R.id.txtVotos);
            btnVotar = itemView.findViewById(R.id.btnVotar);
        }
    }

    public class ViewHolderLider extends RecyclerView.ViewHolder {
        TextView txtNomeLider, txtCategoriaLider, txtPrecoLider, txtVotosLider, txtObsLider;
        Button btnVotarLider;

        public ViewHolderLider (View itemView) {
            super(itemView);
            txtNomeLider = itemView.findViewById(R.id.txtNomeLider);
            txtCategoriaLider = itemView.findViewById(R.id.txtCategoriaLider);
            txtPrecoLider = itemView.findViewById(R.id.txtPrecoLider);
            txtVotosLider = itemView.findViewById(R.id.txtVotosLider);
            btnVotarLider = itemView.findViewById(R.id.btnVotarLider);
            txtObsLider = itemView.findViewById(R.id.txtObservacaoLider);
        }
    }
}


