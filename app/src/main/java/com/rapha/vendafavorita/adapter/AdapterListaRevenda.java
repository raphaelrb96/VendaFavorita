package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.setValUnidade(obj.getValorUni());
        holder.setQuant(obj.getQuantidade());
        holder.setValorQuantidade();
        holder.setModo(obj.getModoPreco());
        holder.showAlertAtacado(obj.getIdModoPreco());
        holder.setComissao(obj.getComissaoUnidade());
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



    class NovoItemListRevenda extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout container_quant_min_atacado;
        TextView bt_editar_item_lista_revenda, bt_remover_item_lista_revenda, quantidade, titulo_prod_revenda, valor_produto_detalhe_revenda;
        TextView text_bt_modelo_precificacao_item_carrinho, comissao_produto_item_lista_revenda;
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
            comissao_produto_item_lista_revenda = (TextView) itemView.findViewById(R.id.comissao_produto_item_lista_revenda);
            text_bt_modelo_precificacao_item_carrinho = (TextView) itemView.findViewById(R.id.text_bt_modelo_precificacao_item_carrinho);
            img_prod_revenda = (ImageView) itemView.findViewById(R.id.img_prod_revenda);
            container_quant_min_atacado = (LinearLayout) itemView.findViewById(R.id.container_quant_min_atacado);

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

        public void setComissao(int comss) {
            comissao_produto_item_lista_revenda.setText("R$" + (comss * quant));
        }

        public void setModo(String modo) {
            if(modo == null || modo.length() == 0) {
                text_bt_modelo_precificacao_item_carrinho.setText("Varejo");
                return;
            }
            text_bt_modelo_precificacao_item_carrinho.setText(modo);
        }

        public void showAlertAtacado(int modoId) {
            if(modoId == 3) {
                container_quant_min_atacado.setVisibility(View.VISIBLE);
            } else {
                container_quant_min_atacado.setVisibility(View.GONE);
            }
        }

        public void setQuant(int quant) {
            this.quant = quant;
            setValorQuantidade();
        }

        public void setValorQuantidade() {
            valor_produto_detalhe_revenda.setText("R$"+(val * quant));
            quantidade.setText(String.valueOf(quant));
        }

        public void aumentar() {
            quant++;
            val = valUnidade * quant;
            ObjProdutoRevenda ob = listaRevendas.get(getBindingAdapterPosition());
            int valComComiss = ob.getValorUniComComissao() * quant;
            int comissaoTotal = ob.getComissaoUnidade() * quant;
            ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao(), ob.getIdModoPreco(), ob.getModoPreco(), ob.getQuantidadeMinima());
            listaner.alteracao(novoObj);
        }

        public void diminuir(int minimo) {
            if (quant > minimo) {
                quant--;
                val = valUnidade * quant;
                ObjProdutoRevenda ob = listaRevendas.get(getBindingAdapterPosition());
                int valComComiss = ob.getValorUniComComissao() * quant;
                int comissaoTotal = ob.getComissaoUnidade() * quant;
                ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao(), ob.getIdModoPreco(), ob.getModoPreco(), ob.getQuantidadeMinima());
                listaner.alteracao(novoObj);
            } else {
                //ObjProdutoRevenda ob = listaRevendas.get(getBindingAdapterPosition());
                //listaner.deletar(ob);
            }
        }

        @Override
        public void onClick(View v) {
            ObjProdutoRevenda ob = listaRevendas.get(getBindingAdapterPosition());

            if (v.getId() == R.id.bt_diminui_prod_lista_revenda) {
                diminuir(ob.getQuantidadeMinima());
            } else if (v.getId() == R.id.bt_aumentar_prod_lista_revenda){
                aumentar();
            } else if (v.getId() == R.id.bt_editar_item_lista_revenda) {



                listaner.editar(ob);
            } else if (v.getId() == R.id.bt_remover_item_lista_revenda) {


                listaner.deletar(ob);
            }
        }
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
            valor.setText(  "R$"+val);
            quantidade.setText(String.valueOf(quant));
        }

        public void aumentar() {
            quant++;
            val = valUnidade * quant;
            ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
            int valComComiss = ob.getValorUniComComissao() * quant;
            int comissaoTotal = ob.getComissaoUnidade() * quant;
            ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao(), ob.getIdModoPreco(), ob.getModoPreco(), ob.getQuantidadeMinima());
            listaner.alteracao(novoObj);
        }

        public void diminuir() {
            if (quant > 1) {
                quant--;
                val = valUnidade * quant;
                ObjProdutoRevenda ob = listaRevendas.get(getAdapterPosition());
                int valComComiss = ob.getValorUniComComissao() * quant;
                int comissaoTotal = ob.getComissaoUnidade() * quant;
                ObjProdutoRevenda novoObj = new ObjProdutoRevenda(ob.getCaminhoImg(), comissaoTotal, ob.getComissaoUnidade(), ob.getIdProdut(), ob.getLabo(), ob.getProdutoName(), quant, val, valComComiss, ob.getValorUni(), ob.getValorUniComComissao(), ob.getIdModoPreco(), ob.getModoPreco(), ob.getQuantidadeMinima());
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

}
