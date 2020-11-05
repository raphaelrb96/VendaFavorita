package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rapha.vendafavorita.ProdObj;
import com.rapha.vendafavorita.R;

import java.util.ArrayList;


public class AdapterEnviarProduto extends RecyclerView.Adapter<AdapterEnviarProduto.ProdutoView> {

    private Context context;
    private ArrayList<ProdObj> produtos;
    private EnviarProdutoListener listener;

    public AdapterEnviarProduto(Context context, ArrayList<ProdObj> produtos, EnviarProdutoListener listener) {
        this.context = context;
        this.produtos = produtos;
        this.listener = listener;
    }

    public interface EnviarProdutoListener {
        void enviarProduto(ProdObj obj);
    }

    @NonNull
    @Override
    public ProdutoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_enviar_produto, parent, false);
        return new ProdutoView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoView holder, int position) {
        ProdObj prod = produtos.get(position);
        Glide.with(context).load(prod.getImgCapa()).into(holder.img);
        holder.nome.setText(prod.getProdName());
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class ProdutoView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView bt_enviar_prod;
        private ImageView img;
        private TextView nome;

        public ProdutoView(@NonNull View itemView) {
            super(itemView);
            bt_enviar_prod = (CardView) itemView.findViewById(R.id.bt_enviar_prod);
            img = (ImageView) itemView.findViewById(R.id.img_produto_cart);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_cart);
            bt_enviar_prod.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            listener.enviarProduto(produtos.get(getAdapterPosition()));
        }
    }

}
