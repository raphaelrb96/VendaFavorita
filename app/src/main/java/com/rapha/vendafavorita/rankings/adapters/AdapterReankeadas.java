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

public class AdapterReankeadas extends RecyclerView.Adapter<AdapterReankeadas.RankeadasViewHolder> {

    private ArrayList<ObjectRevenda> revendas;
    private Context context;
    private RankeadasListener listener;

    public AdapterReankeadas(ArrayList<ObjectRevenda> revendas, Context context, RankeadasListener listener) {
        this.revendas = revendas;
        this.context = context;
        this.listener = listener;
    }

    public interface RankeadasListener {
        void deletar(ObjectRevenda objectRevenda, int p);
    }

    @NonNull
    @Override
    public RankeadasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vendas_rankeadas_adm, parent, false);
        return new RankeadasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankeadasViewHolder holder, int position) {
        ObjectRevenda revenda = revendas.get(position);

        Glide.with(context).load(revenda.getPathFotoUserRevenda()).into(holder.img_vendedor_item_vendas_rankeadas_adm);
        holder.cliente_item_vendas_rankeadas_adm.setText(revenda.getNomeCliente());

        int itens = 0;

        for (int i = 0; i < revenda.getListaDeProdutos().size(); i++) {
            int quantidade = revenda.getListaDeProdutos().get(i).getQuantidade();
            itens = itens + quantidade;
        }


        holder.quantidade_item_vendas_rankeadas_adm.setText("+" + itens);
        holder.vendedor_item_vendas_rankeadas_adm.setText(revenda.getUserNomeRevendedor());

        holder.bairro_item_vendas_rankeadas_adm.setText(revenda.getComplemento());
        String horaString = DateFormatacao.dataCompletaCorrigidaSmall2(new Date(revenda.getHora()), new Date());
        holder.data_item_vendas_rankeadas_adm.setText(horaString);


    }

    @Override
    public int getItemCount() {
        return revendas.size();
    }

    class RankeadasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View bt_delete_item_vendas_rankeadas_adm;
        private ImageView img_vendedor_item_vendas_rankeadas_adm;
        private TextView data_item_vendas_rankeadas_adm, cliente_item_vendas_rankeadas_adm, quantidade_item_vendas_rankeadas_adm, vendedor_item_vendas_rankeadas_adm, bairro_item_vendas_rankeadas_adm;


        public RankeadasViewHolder(@NonNull View itemView) {
            super(itemView);
            data_item_vendas_rankeadas_adm = (TextView) itemView.findViewById(R.id.data_item_vendas_rankeadas_adm);
            cliente_item_vendas_rankeadas_adm = (TextView) itemView.findViewById(R.id.cliente_item_vendas_rankeadas_adm);
            bairro_item_vendas_rankeadas_adm = (TextView) itemView.findViewById(R.id.bairro_item_vendas_rankeadas_adm);
            vendedor_item_vendas_rankeadas_adm = (TextView) itemView.findViewById(R.id.vendedor_item_vendas_rankeadas_adm);
            quantidade_item_vendas_rankeadas_adm = (TextView) itemView.findViewById(R.id.quantidade_item_vendas_rankeadas_adm);

            img_vendedor_item_vendas_rankeadas_adm = (ImageView) itemView.findViewById(R.id.img_vendedor_item_vendas_rankeadas_adm);
            bt_delete_item_vendas_rankeadas_adm = (View) itemView.findViewById(R.id.bt_delete_item_vendas_rankeadas_adm);

            bt_delete_item_vendas_rankeadas_adm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.deletar(revendas.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}
