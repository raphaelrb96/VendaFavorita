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
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.rankings.objcts.Participantes;

import java.util.ArrayList;

public class AdapterClassificacaoRanking extends RecyclerView.Adapter<AdapterClassificacaoRanking.ClassificacaoViewHolder> {

    private Context context;
    private ArrayList<Participantes> revendedores;

    public AdapterClassificacaoRanking(Context context, ArrayList<Participantes> revendedores) {
        this.context = context;
        this.revendedores = revendedores;
    }

    @NonNull
    @Override
    public ClassificacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classificacao, parent, false);
        return new ClassificacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificacaoViewHolder holder, int position) {
        Participantes revendedor = revendedores.get(position);
        holder.tv_posicao_classificacao.setText((position + 1) + "º posição");
        Glide.with(context).load(revendedor.getPathFoto()).into(holder.img_vendedor_item_classificacao);
        holder.tv_vendedor_classificacao.setText(revendedor.getNome());
        String itenx = " Item";
        if (revendedor.getItensVendidos() > 1) {
            itenx = " Itens";
        }

        holder.tv_num_vendas_classficacao.setText(revendedor.getItensVendidos() + itenx);

    }

    @Override
    public int getItemCount() {
        if (revendedores == null) return 0;
        return revendedores.size();
    }

    class ClassificacaoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_posicao_classificacao, tv_vendedor_classificacao, tv_num_vendas_classficacao;
        ImageView img_vendedor_item_classificacao;

        public ClassificacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_posicao_classificacao = (TextView) itemView.findViewById(R.id.tv_posicao_classificacao);
            img_vendedor_item_classificacao = (ImageView) itemView.findViewById(R.id.img_vendedor_item_classificacao);
            tv_vendedor_classificacao = (TextView) itemView.findViewById(R.id.tv_vendedor_classificacao);
            tv_num_vendas_classficacao = (TextView) itemView.findViewById(R.id.tv_num_vendas_classficacao);
        }
    }

}
