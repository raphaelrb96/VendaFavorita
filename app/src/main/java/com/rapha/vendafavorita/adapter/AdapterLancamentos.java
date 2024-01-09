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
import com.rapha.vendafavorita.ProdObj;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objectfeed.ProdutoObj;

import java.util.ArrayList;

public class AdapterLancamentos extends RecyclerView.Adapter<AdapterLancamentos.ProdLancamento> {

    private ArrayList<ProdutoObj> produtos;
    private Context context;
    private HandlerProdAtalho handler;

    public AdapterLancamentos(ArrayList<ProdutoObj> produtos, Context context, HandlerProdAtalho handler) {
        this.produtos = produtos;
        this.context = context;
        this.handler = handler;
    }

    public interface HandlerProdAtalho {
        void abriProduto(ProdutoObj prod);
    }

    @NonNull
    @Override
    public ProdLancamento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_principal, parent, false);
        return new ProdLancamento(view, context, produtos);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdLancamento holder, int position) {
        ProdutoObj obj = produtos.get(position);
        holder.setImagem(obj.getPathProduto());
        holder.setNome(obj.getNomeProduto());
    }

    @Override
    public int getItemCount() {
        if (produtos == null) return 0;
        return produtos.size();
    }

    class ProdLancamento extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nome, preco;
        private FloatingActionButton fab;
        private Button bt_item_novidades;

        private Context context;
        private ArrayList<ProdutoObj> produtos;

        public ProdLancamento(@NonNull View itemView, Context context, ArrayList<ProdutoObj> produtos) {
            super(itemView);
            this.context = context;
            this.produtos = produtos;
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_principal);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_principal);
            preco = (TextView) itemView.findViewById(R.id.preco_item_produto_principal);
            bt_item_novidades = (Button) itemView.findViewById(R.id.bt_item_novidades);
            preco.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
            bt_item_novidades.setOnClickListener(this);

        }

        public void setPreco(String p) {
            preco.setText(p);
        }

        public void setImagem(String img) {
            Glide.with(context).load(img).into(imageView);
        }

        public void setNome(String s) {
            nome.setText(s);
        }

        @Override
        public void onClick(View view) {
            handler.abriProduto(produtos.get(getAdapterPosition()));
        }
    }

}
