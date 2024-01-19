package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    public void atualizarLista(ArrayList<ProdutoObj> topProdutos) {
        produtos = null;
        produtos = topProdutos;
        notifyDataSetChanged();
    }

    public interface EmaltaListener {
        void verDetalhesProd(String id);
    }

    @NonNull
    @Override
    public EmAltaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prod_atalho, parent, false);
        return new EmAltaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmAltaViewHolder holder, int position) {
        ProdutoObj obj = produtos.get(position);
        Glide.with(context).load(obj.getPathProduto()).into(holder.imageView);
        holder.nome.setText(obj.getNomeProduto());
    }

    @Override
    public int getItemCount() {
        if (produtos == null) return 0;
        return produtos.size();
    }

    class EmAltaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button bt_item_atalho;
        private TextView nome, preco;
        private ImageView imageView;

        public EmAltaViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_principal);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_principal);
            preco = (TextView) itemView.findViewById(R.id.preco_item_produto_principal);
            bt_item_atalho = (Button) itemView.findViewById(R.id.bt_item_atalho);
            preco.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
            bt_item_atalho.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.verDetalhesProd(produtos.get(getAdapterPosition()).getIdProduto());
        }
    }

}
