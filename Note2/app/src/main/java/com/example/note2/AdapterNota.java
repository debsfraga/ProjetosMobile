package com.example.note2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterNota extends RecyclerView.Adapter<AdapterNota.ViewHolder> {
    // construtor para receber os dados
    private List<Nota> listaNotas;
    public AdapterNota(List<Nota> dados) {
        this.listaNotas = dados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Faz o vínculo do card que vamos usar
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nota, parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Preenche os campos de cada CARD
        Nota item = listaNotas.get(position);
        holder.txtTitulo.setText(item.getTitulo());
        holder.txtDescricao.setText(item.getDescricao());

    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    // Permite vincular os objetps XML no código java
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescricao;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
        }
    }
}
