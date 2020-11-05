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
import com.rapha.vendafavorita.objects.TopProdutosRevenda;

import java.util.ArrayList;

public class AdapterTopProdutos extends RecyclerView.Adapter<AdapterTopProdutos.TopProdutosViewHolder> {

    private ArrayList<TopProdutosRevenda> produtosRevendas;
    private Context context;

    public AdapterTopProdutos(ArrayList<TopProdutosRevenda> produtosRevendas, Context context) {
        this.produtosRevendas = produtosRevendas;
        this.context = context;
    }

    @NonNull
    @Override
    public TopProdutosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_enviar_produto, parent, false);
        return new TopProdutosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopProdutosViewHolder holder, int position) {
        TopProdutosRevenda produto = produtosRevendas.get(position);
        Glide.with(context).load(produto.getPathProduto()).into(holder.imageView);
        holder.nomeProd.setText(produto.getNomeProduto());
        holder.numeroDeVendas.setText("Itens: " + produto.getNumeroDeRevendas());
    }

    @Override
    public int getItemCount() {
        if (produtosRevendas == null) return 0;

        return produtosRevendas.size();
    }

    class TopProdutosViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView numeroDeVendas;
        TextView nomeProd;

        public TopProdutosViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_produto_cart);
            numeroDeVendas = (TextView) itemView.findViewById(R.id.nome_produto_cart);
            nomeProd = (TextView) itemView.findViewById(R.id.tv_n_itens_top);
        }
    }

}
