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
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;

import java.util.ArrayList;

public class AdapterProdutosRevenda extends RecyclerView.Adapter<AdapterProdutosRevenda.ProdutoRevendaViewHolder> {

    private ArrayList<ObjProdutoRevenda> lista;
    private Context context;

    public AdapterProdutosRevenda(ArrayList<ObjProdutoRevenda> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ProdutoRevendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        return new ProdutoRevendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoRevendaViewHolder holder, int position) {
        ObjProdutoRevenda obj = lista.get(position);
        Glide.with(context).load(obj.getCaminhoImg()).into(holder.foto);
        int vu = (int) obj.getValorUniComComissao();

        String s3 = " item";

        if (obj.getQuantidade() > 1) {
            s3 = " itens";
        }

        holder.quantidade.setText(obj.getQuantidade() + " " + obj.getProdutoName());
        holder.total.setText("Total: " + String.valueOf((int) obj.getValorTotalComComissao()) + ",00");

        int lucroPrd = obj.getComissaoTotal();

        String lucroPorItem = "Lucro: R$" + lucroPrd + ",00";

        holder.nome.setText(lucroPorItem);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ProdutoRevendaViewHolder extends RecyclerView.ViewHolder {

        private TextView nome, total, quantidade;
        private View btRemover, btAumentar, btDiminuir;
        private ImageView foto;

        public ProdutoRevendaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_cart);
            total = (TextView) itemView.findViewById(R.id.valor_total_produto_cart);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_produto_cart);

            btAumentar = (View) itemView.findViewById(R.id.bt_aumentar_produto_cart);
            btDiminuir = (View) itemView.findViewById(R.id.bt_diminui_produto_cart);
            btRemover = (View) itemView.findViewById(R.id.bt_remove_produto_cart);

            foto = (ImageView) itemView.findViewById(R.id.img_produto_cart);

            btAumentar.setVisibility(View.GONE);
            btDiminuir.setVisibility(View.GONE);
            btRemover.setVisibility(View.GONE);
        }
    }

}
