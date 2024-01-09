package com.rapha.vendafavorita.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterParcelamento extends RecyclerView.Adapter<AdapterParcelamento.ParcelamentoViewHolder> {

    private ArrayList<String> lista;

    public AdapterParcelamento(ArrayList<String> lista) {
        this.lista = lista;
    }

    @NonNull
    @NotNull
    @Override
    public ParcelamentoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parcelamento, parent, false);
        return new ParcelamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ParcelamentoViewHolder holder, int position) {
        holder.setData(lista.get(position), position);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ParcelamentoViewHolder extends RecyclerView.ViewHolder {

        private TextView num_parcelamento_item;
        private TextView valor_parcelamento_item;

        public ParcelamentoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            num_parcelamento_item = (TextView) itemView.findViewById(R.id.num_parcelamento_item);
            valor_parcelamento_item = (TextView) itemView.findViewById(R.id.valor_parcelamento_item);
        }

        public void setValor(String v) {
            valor_parcelamento_item.setText(v);
        }

        public void setNum(String n) {
            num_parcelamento_item.setText(n);
        }

        public void setData (String v, int p) {
            setNum(String.valueOf(p+1));
            setValor(v);
        }
    }
}
