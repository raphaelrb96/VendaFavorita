package com.rapha.vendafavorita.rankings.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;
import java.util.Date;

public class AdapterNaoRankeadas extends RecyclerView.Adapter<AdapterNaoRankeadas.NaoRankeadasViewHolder> {

    private ArrayList<ObjectRevenda> revendas;
    private Context context;
    private NaoRankeadasListener listener;

    public AdapterNaoRankeadas(ArrayList<ObjectRevenda> revendas, Context context, NaoRankeadasListener listener) {
        this.revendas = revendas;
        this.context = context;
        this.listener = listener;
    }

    public interface NaoRankeadasListener {
        void rankear(ObjectRevenda revenda, int p);
    }

    @NonNull
    @Override
    public NaoRankeadasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vendas_nao_rankeadas_adm, parent, false);
        return new NaoRankeadasViewHolder(view);
    }

    public void atualizarAdapter (ArrayList<ObjectRevenda> rs) {
        this.revendas = rs;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull NaoRankeadasViewHolder holder, int position) {
        ObjectRevenda revenda = revendas.get(position);

        Glide.with(context).load(revenda.getPathFotoUserRevenda()).into(holder.img_vendedor_item_vendas_nao_rankeadas_adm);


        boolean concl = false;
        if (revenda.getStatusCompra() == 5) {
            concl = true;
        }
        holder.setIcone(concl);

        int itens = 0;
        String it = " Item";

        for (int i = 0; i < revenda.getListaDeProdutos().size(); i++) {
            int quantidade = revenda.getListaDeProdutos().get(i).getQuantidade();
            itens = itens + quantidade;
        }

        if (itens > 1) it = " Itens";

        holder.cliente_item_vendas_nao_rankeadas_adm.setText("Vendeu: " + itens + it + " para " + revenda.getNomeCliente());

        String horaString = DateFormatacao.dataCompletaCorrigidaSmall2(new Date(revenda.getHora()), new Date());

        holder.quantidade_item_vendas_nao_rankeadas_adm.setText(horaString);
        holder.bt_rankear_vendas_nao_rankeadas_adm.setText("+" + itens + " para " + revenda.getUserNomeRevendedor());

    }

    @Override
    public int getItemCount() {
        return revendas.size();
    }

    class NaoRankeadasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView bt_rankear_vendas_nao_rankeadas_adm, cliente_item_vendas_nao_rankeadas_adm, quantidade_item_vendas_nao_rankeadas_adm;
        private ImageView img_vendedor_item_vendas_nao_rankeadas_adm, icon_concluida_vendas_nao_rankeadas_adm, icon_analisando_vendas_nao_rankeadas_adm;

        public NaoRankeadasViewHolder(@NonNull View itemView) {
            super(itemView);
            bt_rankear_vendas_nao_rankeadas_adm = (TextView) itemView.findViewById(R.id.bt_rankear_vendas_nao_rankeadas_adm);
            img_vendedor_item_vendas_nao_rankeadas_adm = (ImageView) itemView.findViewById(R.id.img_vendedor_item_vendas_nao_rankeadas_adm);
            cliente_item_vendas_nao_rankeadas_adm = (TextView) itemView.findViewById(R.id.cliente_item_vendas_nao_rankeadas_adm);
            quantidade_item_vendas_nao_rankeadas_adm = (TextView) itemView.findViewById(R.id.quantidade_item_vendas_nao_rankeadas_adm);
            icon_concluida_vendas_nao_rankeadas_adm = (ImageView) itemView.findViewById(R.id.icon_concluida_vendas_nao_rankeadas_adm);
            icon_analisando_vendas_nao_rankeadas_adm = (ImageView) itemView.findViewById(R.id.icon_analisando_vendas_nao_rankeadas_adm);
            bt_rankear_vendas_nao_rankeadas_adm.setOnClickListener(this);
        }

        public void setIcone(boolean concluida) {
            if (concluida) {
                icon_analisando_vendas_nao_rankeadas_adm.setVisibility(View.GONE);
                icon_concluida_vendas_nao_rankeadas_adm.setVisibility(View.VISIBLE);
            } else {
                icon_analisando_vendas_nao_rankeadas_adm.setVisibility(View.VISIBLE);
                icon_concluida_vendas_nao_rankeadas_adm.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            listener.rankear(revendas.get(getAdapterPosition()), getAdapterPosition());
        }
    }

}
