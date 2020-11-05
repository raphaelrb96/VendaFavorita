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
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;
import java.util.Date;

public class AdapterMinhasRevendas extends RecyclerView.Adapter<AdapterMinhasRevendas.MinhasRevendasViewHolder> {

    private ArrayList<ObjectRevenda> minhasRevendas;
    private Context context;

    public AdapterMinhasRevendas(ArrayList<ObjectRevenda> minhasRevendas, Context context) {
        this.minhasRevendas = minhasRevendas;
        this.context = context;
    }

    @NonNull
    @Override
    public MinhasRevendasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ultimas_revendas, parent, false);
        return new MinhasRevendasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MinhasRevendasViewHolder holder, int position) {
        ObjectRevenda obj = minhasRevendas.get(position);
        holder.setBairroCliente(obj.getComplemento());
        holder.setComissao(String.valueOf(obj.getComissaoTotal()));
        holder.setDataHora(obj.getHora());
        holder.setnCliente(obj.getNomeCliente());
        int status = obj.getStatusCompra();

        switch (status) {
            case 1:
                holder.setStatus("Aguarde");
                break;
            case 2:
                holder.setStatus("Confirmada");
                break;
            case 3:
                holder.setStatus("Cancelada");
                break;
            case 4:
                holder.setStatus("Em rota");
                break;
            case 5:
                holder.setStatus("Concluida");
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return minhasRevendas.size();
    }

    class MinhasRevendasViewHolder extends RecyclerView.ViewHolder {

        private TextView nCliente, dataHora, bairroCliente, comissao, status;

        public MinhasRevendasViewHolder(@NonNull View itemView) {
            super(itemView);
            nCliente = (TextView) itemView.findViewById(R.id.nome_cliente_minha_revenda);
            dataHora = (TextView) itemView.findViewById(R.id.hora_minhas_revendas);
            bairroCliente = (TextView) itemView.findViewById(R.id.tv_bairro_minhas_revendas);
            status = (TextView) itemView.findViewById(R.id.status_minha_revenda);
            comissao = (TextView) itemView.findViewById(R.id.comissao_minha_revenda);
        }

        public void setnCliente(String cliente) {
            this.nCliente.setText(cliente);
        }

        public void setDataHora(long data) {
            String horaString = DateFormatacao.dataCompletaCorrigidaSmall2(new Date(data), new Date());
            this.dataHora.setText(horaString);
        }

        public void setBairroCliente(String bairro) {
            this.bairroCliente.setText(bairro);
        }

        public void setComissao(String comis) {
            this.comissao.setText(comis);
        }

        public void setStatus(String stat) {
            this.status.setText(stat);
        }
    }

}
