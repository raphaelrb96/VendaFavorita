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

public class AdapterListaRevenda extends RecyclerView.Adapter<AdapterListaRevenda.NovoItemListRevenda> {

    private Context context;
    private ArrayList<ObjProdutoRevenda> listaRevendas;
    private RevendaListaner listaner;

    public AdapterListaRevenda(Context context, ArrayList<ObjProdutoRevenda> listaRevendas, RevendaListaner listaner) {
        this.context = context;
        this.listaRevendas = listaRevendas;
        this.listaner = listaner;
    }

    public interface RevendaListaner {
        void alteracao(ObjProdutoRevenda prod);
        void deletar(ObjProdutoRevenda prod);
        void editar(ObjProdutoRevenda prod);
    }

    @NonNull
    @Override
    public NovoItemListRevenda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_revanda, parent, false);
        return new NovoItemListRevenda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovoItemListRevenda holder, int position) {
        ObjProdutoRevenda obj = listaRevendas.get(position);
        holder.setImagem(obj.getCaminhoImg(), context);
        holder.setValUnidade(obj.getValorTotalComComissao());
        holder.setValorQuantidade();
        holder.setQuant(obj.getQuantidade());
        holder.setTitulo(obj.getProdutoName());
    }

    public void atualizarLista(ArrayList<ObjProdutoRevenda> list) {
        listaRevendas = null;
        listaRevendas = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaRevendas.size();
    }

    class ItemListaRevenda extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View aumentar, diminuir;
        private TextView quantidade, valor;
        private ImageView imageView;

        private int quant = 0;
        private int val = 0;
        private int valUnidade = 0;

        public ItemListaRevenda(@NonNull View itemView) {
            super(itemView);
            aumentar = (View) itemView.findViewById(R.id.bt_aumentar_prod_lista_revenda);
            diminuir = (View) itemView.findViewById(R.id.bt_diminui_prod_lista_revenda);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_prod_lista_revenda);
            valor = (TextView) itemView.findViewById(R.id.valor_prod_lista_revenda);
            imageView = (ImageView) itemView.findViewById(R.id.img_prod_lista_revenda);

            aumentar.setOnClickListener(this);
            diminuir.setOnClickListener(this);
        }

        public void setImagem(String path, Context c) {
            Glide.with(c).load(path).into(imageView);
        }

        public void setValUnidade(int valUnidade) {
            this.valUnidade = valUnidade;
            this.val = valUnidade;
        }

        public void setQuant(int quant) {
            this.quant = quant;
            setValorQuantidade();
        }

        public void setValorQuantidade() {
            valor.setText(val + ",00");
            quantidade.setText(String.valueOf(quant));
        }

        public void aumentar() {
            quant++;
            val = valUnidade * quant;
            ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
            int valComComiss = ob.getValorUniComComissao() * quant;
            int comissaoTotal = ob.getComissaoUnidade() * quant;
            ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao());
            listaner.alteracao(novoObj);
        }

        public void diminuir() {
            if (quant > 1) {
                quant--;
                val = valUnidade * quant;
                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
                int valComComiss = ob.getValorUniComComissao() * quant;
                int comissaoTotal = ob.getComissaoUnidade() * quant;
                ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao());
                listaner.alteracao(novoObj);
            } else {
                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
                listaner.deletar(ob);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_diminui_prod_lista_revenda) {
                diminuir();
            } else {
                aumentar();
            }
        }
    }

    class NovoItemListRevenda extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView bt_editar_item_lista_revenda, bt_remover_item_lista_revenda, quantidade, titulo_prod_revenda, valor_produto_detalhe_revenda;
        View aumentar, diminuir;
        ImageView img_prod_revenda;

        private int quant = 0;
        private int val = 0;
        private int valUnidade = 0;

        public NovoItemListRevenda(@NonNull View itemView) {
            super(itemView);
            bt_editar_item_lista_revenda = (TextView) itemView.findViewById(R.id.bt_editar_item_lista_revenda);
            bt_remover_item_lista_revenda = (TextView) itemView.findViewById(R.id.bt_remover_item_lista_revenda);
            aumentar = (View) itemView.findViewById(R.id.bt_aumentar_prod_lista_revenda);
            diminuir = (View) itemView.findViewById(R.id.bt_diminui_prod_lista_revenda);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_prod_lista_revenda);
            titulo_prod_revenda = (TextView) itemView.findViewById(R.id.titulo_prod_revenda);
            valor_produto_detalhe_revenda = (TextView) itemView.findViewById(R.id.valor_produto_detalhe_revenda);
            img_prod_revenda = (ImageView) itemView.findViewById(R.id.img_prod_revenda);

            aumentar.setOnClickListener(this);
            diminuir.setOnClickListener(this);

            bt_editar_item_lista_revenda.setOnClickListener(this);
            bt_remover_item_lista_revenda.setOnClickListener(this);

        }

        public void setImagem(String path, Context c) {
            Glide.with(c).load(path).into(img_prod_revenda);
        }

        public void setTitulo (String s) {
            titulo_prod_revenda.setText(s);
        }

        public void setValUnidade(int valUnidade) {
            this.valUnidade = valUnidade;
            this.val = valUnidade;
        }

        public void setQuant(int quant) {
            this.quant = quant;
            setValorQuantidade();
        }

        public void setValorQuantidade() {
            valor_produto_detalhe_revenda.setText(val + ",00");
            quantidade.setText(String.valueOf(quant));
        }

        public void aumentar() {
            quant++;
            val = valUnidade * quant;
            ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
            int valComComiss = ob.getValorUniComComissao() * quant;
            int comissaoTotal = ob.getComissaoUnidade() * quant;
            ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao());
            listaner.alteracao(novoObj);
        }

        public void diminuir() {
            if (quant > 1) {
                quant--;
                val = valUnidade * quant;
                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
                int valComComiss = ob.getValorUniComComissao() * quant;
                int comissaoTotal = ob.getComissaoUnidade() * quant;
                ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao());
                listaner.alteracao(novoObj);
            } else {
                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
                listaner.deletar(ob);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_diminui_prod_lista_revenda) {
                diminuir();
            } else if (v.getId() == R.id.bt_aumentar_prod_lista_revenda){
                aumentar();
            } else if (v.getId() == R.id.bt_editar_item_lista_revenda) {

                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());

                listaner.editar(ob);
            } else if (v.getId() == R.id.bt_remover_item_lista_revenda) {

                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());

                listaner.deletar(ob);
            }
        }
    }

}
