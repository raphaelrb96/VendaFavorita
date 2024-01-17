package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.SaquesObj;
import com.rapha.vendafavorita.util.FormatoString;
import com.rapha.vendafavorita.util.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class AdapterSaques extends RecyclerView.Adapter<AdapterSaques.SaqueViewHolder> {

    private ArrayList<SaquesObj> listHistorico;
    private Context context;

    public AdapterSaques(ArrayList<SaquesObj> listHistorico, Context context) {
        this.listHistorico = listHistorico;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SaqueViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(context).inflate(R.layout.item_saque_historico, parent, false);
        return new SaqueViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SaqueViewHolder holder, int position) {
        SaquesObj saquesObj = listHistorico.get(position);
        holder.banco_item_saque.setText(saquesObj.getBank());
        holder.nome_item_saque.setText(saquesObj.getTitular());
        holder.chave_item_saque.setText(saquesObj.getChave());
        holder.status_item_saque.setText(Status.getStatusSaque(saquesObj.getStatus()));
        long timer = Status.saqueEmAndamento(saquesObj.getStatus()) ? saquesObj.getTimestampCreated() : saquesObj.getTimestampPay();
        holder.timestamp_item_saque.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(timer), new Date()));
        holder.total_item_saque.setText(FormatoString.formartarPreco(saquesObj.getValorFinal()));
    }

    @Override
    public int getItemCount() {
        if(listHistorico != null) return listHistorico.size();
        return 0;
    }

    class SaqueViewHolder extends RecyclerView.ViewHolder {

        private TextView total_item_saque, status_item_saque, timestamp_item_saque, chave_item_saque, nome_item_saque, banco_item_saque;

        public SaqueViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            total_item_saque = (TextView) itemView.findViewById(R.id.total_item_saque);
            status_item_saque = (TextView) itemView.findViewById(R.id.status_item_saque);
            timestamp_item_saque = (TextView) itemView.findViewById(R.id.timestamp_item_saque);
            chave_item_saque = (TextView) itemView.findViewById(R.id.chave_item_saque);
            nome_item_saque = (TextView) itemView.findViewById(R.id.nome_item_saque);
            banco_item_saque = (TextView) itemView.findViewById(R.id.banco_item_saque);

        }
    }

}
