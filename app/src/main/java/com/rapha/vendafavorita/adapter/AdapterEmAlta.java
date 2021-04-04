package com.rapha.vendafavorita.adapter;

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
import com.rapha.vendafavorita.objectfeed.ProdutoObj;

import java.util.ArrayList;

public class AdapterEmAlta extends RecyclerView.Adapter<AdapterEmAlta.EmAltaViewHolder> {

    private Context context;
    private EmaltaListener listener;
    private ArrayList<ProdutoObj> produtos;

    public AdapterEmAlta(Context context, EmaltaListener listener, ArrayList<ProdutoObj> produtos) {
        this.context = context;
        this.listener = listener;
        this.produtos = produtos;
    }

    public interface EmaltaListener {
        void verDetalhesProd(String id);
    }

    @NonNull
    @Override
    public EmAltaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prods_em_alta, parent, false);
        return new EmAltaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmAltaViewHolder holder, int position) {
        ProdutoObj obj = produtos.get(position);
        Glide.with(context).load(obj.getPathProduto()).into(holder.img_prod_em_alta);
        holder.titulo_prod_em_alta.setText(obj.getNomeProduto());
        String coloccao = String.valueOf(position + 1);
        holder.colocacao_item_prod_em_alta.setText(coloccao + "º Lugar");
    }

    @Override
    public int getItemCount() {
        if (produtos == null) return 0;
        return produtos.size();
    }

    class EmAltaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView colocacao_item_prod_em_alta, titulo_prod_em_alta;
        private ImageView img_prod_em_alta;

        public EmAltaViewHolder(@NonNull View itemView) {
            super(itemView);

            colocacao_item_prod_em_alta = (TextView) itemView.findViewById(R.id.colocacao_item_prod_em_alta);
            titulo_prod_em_alta = (TextView) itemView.findViewById(R.id.titulo_prod_em_alta);
            img_prod_em_alta = (ImageView) itemView.findViewById(R.id.img_prod_em_alta);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.verDetalhesProd(produtos.get(getAdapterPosition()).getIdProduto());
        }
    }

}
