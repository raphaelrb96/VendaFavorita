package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rapha.vendafavorita.AdapterProdutos;
import com.rapha.vendafavorita.ProdObj;
import com.rapha.vendafavorita.R;

import java.util.ArrayList;

import static com.rapha.vendafavorita.MainActivity.ids;

public class AdapterProdutoAtalho extends RecyclerView.Adapter<AdapterProdutoAtalho.ProdAtalho> {

    private ArrayList<ProdObj> produtos;
    private Context context;
    private HandlerProdAtalho handler;

    public AdapterProdutoAtalho(ArrayList<ProdObj> produtos, Context context, HandlerProdAtalho handler) {
        this.produtos = produtos;
        this.context = context;
        this.handler = handler;
    }

    public interface HandlerProdAtalho {
        void abriProduto(ProdObj prod);
    }

    @NonNull
    @Override
    public ProdAtalho onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prod_atalho, parent, false);
        return new ProdAtalho(view, context, produtos);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdAtalho vh, int position) {
        ProdObj obj = produtos.get(position);
        vh.setImagem(obj.getImgCapa());
        vh.setPreco(String.valueOf((int) obj.getProdValor()) + ",00");
        vh.setNome(obj.getProdName());
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class ProdAtalho extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nome, preco;
        private FloatingActionButton fab;

        private Context context;
        private ArrayList<ProdObj> produtos;

        public ProdAtalho(@NonNull View itemView, Context context, ArrayList<ProdObj> produtos) {
            super(itemView);
            this.context = context;
            this.produtos = produtos;
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_principal);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_principal);
            preco = (TextView) itemView.findViewById(R.id.preco_item_produto_principal);
            fab = (FloatingActionButton) itemView.findViewById(R.id.fab_produto_item);
            itemView.setOnClickListener(this);

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
