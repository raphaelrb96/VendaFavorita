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
import com.google.android.material.card.MaterialCardView;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.util.FormatoString;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_lista_pedido, parent, false);
        return new ProdutoRevendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoRevendaViewHolder holder, int position) {
        ObjProdutoRevenda obj = lista.get(position);

        Glide.with(context).load(obj.getCaminhoImg()).into(holder.foto);

        int valorUnd = (int) obj.getValorUniComComissao();
        int quantidade = obj.getQuantidade();

        String quant = String.valueOf(quantidade);
        String qtdFormt = quant.length() < 2 ? "0" + quant : quant;
        holder.quantidade.setText(qtdFormt);

        holder.total.setText(FormatoString.formartarPreco(quantidade * valorUnd));
        holder.nome.setText(obj.getProdutoName());
        holder.vUnid.setText(FormatoString.formartarPreco(obj.getValorUniComComissao()));
        if (obj.getModoPreco() != null) {
            holder.modelo.setText(obj.getModoPreco());
        } else {
            holder.modelo.setText("Varejo");
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ProdutoRevendaViewHolder extends RecyclerView.ViewHolder {

        private TextView nome, total, quantidade, modelo, vUnid;
        private ImageView foto;
        private MaterialCardView cardModelo;

        public ProdutoRevendaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_item_lista_pedido);
            total = (TextView) itemView.findViewById(R.id.valor_total_produto_item_lista_pedido);
            vUnid = (TextView) itemView.findViewById(R.id.valor_uni_produto_item_lista_pedido);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_produto_item_lista_pedido);
            modelo = (TextView) itemView.findViewById(R.id.text_precificacao_item_lista_pedido);
            foto = (ImageView) itemView.findViewById(R.id.img_produto_item_lista_pedido);
            cardModelo = (MaterialCardView) itemView.findViewById(R.id.modelo_preco_item_lista_pedido);

        }
    }

}
