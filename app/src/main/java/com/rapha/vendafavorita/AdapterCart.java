package com.rapha.vendafavorita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.rapha.vendafavorita.CartActivity.produtoss;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {

    private AnalizarClickPayFinal clickPayFinal;
    private Context context;

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_main, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CarComprasActivy obj = produtoss.get(position);
        Glide.with(context).load(obj.caminhoImg).into(holder.foto);
        int vu = (int) obj.valorUni;
        holder.quantidade.setText(String.valueOf(obj.quantidade));
        holder.total.setText(String.valueOf((int) obj.valorTotal) + ",00");
        holder.nome.setText(obj.produtoName);
    }

    @Override
    public int getItemCount() {
        return produtoss.size();
    }

    public void swapDados(ArrayList<CarComprasActivy> arrayList) {
        produtoss = arrayList;
        notifyDataSetChanged();
    }

    public interface AnalizarClickPayFinal {
        void aumentarQuantidade(CarComprasActivy carComprasActivy, String str, int i);

        void diminuirQuantidade(CarComprasActivy carComprasActivy, String str, int i);

        void removerProduto(String str, int p);
    }

    public AdapterCart(AnalizarClickPayFinal clickPayFinal, Context context) {
        this.clickPayFinal = clickPayFinal;
        this.context = context;
    }

    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nome, total, quantidade;
        private View btRemover, btAumentar, btDiminuir;
        private ImageView foto;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.tv_nome_cart_main);
            total = (TextView) itemView.findViewById(R.id.tv_total_cart_main);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_produto_cart_main);

            btAumentar = (View) itemView.findViewById(R.id.bt_aumentar_produto_cart_main);
            btDiminuir = (View) itemView.findViewById(R.id.bt_diminui_produto_cart_main);
            //btRemover = (View) itemView.findViewById(R.id.bt_remove_produto_cart);

            foto = (ImageView) itemView.findViewById(R.id.imagem_cart_main);

            btAumentar.setOnClickListener(this);
            //btRemover.setOnClickListener(this);
            btDiminuir.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CarComprasActivy c = produtoss.get(getAdapterPosition());
                    clickPayFinal.removerProduto(c.getIdProdut(), getAdapterPosition());
                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            CarComprasActivy c = produtoss.get(getAdapterPosition());
            if (v.getId() == R.id.bt_aumentar_produto_cart_main) {

                clickPayFinal.aumentarQuantidade(c, c.idProdut, getAdapterPosition());
            } else if (v.getId() == R.id.bt_diminui_produto_cart_main) {
                if (c.getQuantidade() > 1) {
                    clickPayFinal.diminuirQuantidade(c, c.idProdut, getAdapterPosition());
                } else {
                    clickPayFinal.removerProduto(c.getIdProdut(), getAdapterPosition());
                }
            }
        }
    }

}
