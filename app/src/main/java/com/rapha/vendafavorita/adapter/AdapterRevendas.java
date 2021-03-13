package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;
import java.util.Date;

public class AdapterRevendas extends RecyclerView.Adapter<AdapterRevendas.RevendasViewHolder> {

    private Context context;
    private ArrayList<ObjectRevenda> revendaArrayList;
    private RevendaListener revendaListener;

    public AdapterRevendas(Context context, ArrayList<ObjectRevenda> revendaArrayList, RevendaListener revendaListener) {
        this.context = context;
        this.revendaArrayList = revendaArrayList;
        this.revendaListener = revendaListener;
    }

    public interface RevendaListener {
        void cancelar(ObjectRevenda obj);
        void confirmar(ObjectRevenda obj);
        void entregar(ObjectRevenda obj);
        void concluir(ObjectRevenda obj);

        void verRevended(String uidUserRevendedor, String userNomeRevendedor, String pathFotoUserRevenda);
    }

    @NonNull
    @Override
    public RevendasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_revenda, parent, false);
        return new RevendasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevendasViewHolder holder, int position) {
        ObjectRevenda obj = revendaArrayList.get(position);
        holder.setHora_revenda(obj.getHora());
        holder.setNome_cliente_revenda(obj.getNomeCliente());
        holder.setNumero_cliente_revenda(obj.getPhoneCliente());
        holder.setForma_de_pagamento_cliente_revenda(obj.getFormaDePagar());
        String rua = obj.getAdress();
        if (obj.getComplemento().length() > 0) {
            rua = rua + "(" + obj.getComplemento() + ")";
        }
        holder.setEndereco_cliente_revenda(rua);
        holder.setNome_revendedor_revenda(obj.getUserNomeRevendedor());
        holder.setComissao_revendedor_revenda(obj.getComissaoTotal());
        holder.setTotal_revenda(obj.getValorTotal());
        holder.setRv_produtos_revenda(obj.getListaDeProdutos(), context);
        int status = obj.getStatusCompra();
        switch (status) {
            case 1:
                holder.setStatus_revender("Aguardando a confirmação");
                break;
            case 2:
                holder.setStatus_revender("Confirmada");
                break;
            case 3:
                holder.setStatus_revender("Cancelada");
                break;
            case 4:
                holder.setStatus_revender("Saiu para entrega");
                break;
            case 5:
                holder.setStatus_revender("Concluida");
                break;
            default:
                break;
        }

        if (obj.isPagamentoRecebido()) {
            holder.status_pagamento_comissao.setChecked(true);
        } else {
            holder.status_pagamento_comissao.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return revendaArrayList.size();
    }

    class RevendasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView endereco_cliente_revenda, numero_cliente_revenda, nome_cliente_revenda, hora_revenda;
        private TextView nome_revendedor_revenda, comissao_revendedor_revenda, total_revenda;
        private RecyclerView rv_produtos_revenda;

        private LinearLayout ll_bt_confirmar_revenda, ll_bt_entregar_revenda, ll_bt_cancelar_revenda, ll_bt_concluir_revenda;

        private TextView status_revender, forma_de_pagamento_cliente_revenda;

        private ToggleButton status_pagamento_comissao;

        public RevendasViewHolder(@NonNull View itemView) {

            super(itemView);

            endereco_cliente_revenda = (TextView) itemView.findViewById(R.id.endereco_cliente_revenda);
            numero_cliente_revenda = (TextView) itemView.findViewById(R.id.numero_cliente_revenda);
            nome_cliente_revenda = (TextView) itemView.findViewById(R.id.nome_cliente_revenda);
            hora_revenda = (TextView) itemView.findViewById(R.id.hora_revenda);
            forma_de_pagamento_cliente_revenda = (TextView) itemView.findViewById(R.id.forma_de_pagamento_cliente_revenda);
            nome_revendedor_revenda = (TextView) itemView.findViewById(R.id.nome_revendedor_revenda);
            comissao_revendedor_revenda = (TextView) itemView.findViewById(R.id.comissao_revendedor_revenda);
            total_revenda = (TextView) itemView.findViewById(R.id.total_revenda);
            rv_produtos_revenda = (RecyclerView) itemView.findViewById(R.id.rv_produtos_revenda);

            ll_bt_confirmar_revenda = (LinearLayout) itemView.findViewById(R.id.ll_bt_confirmar_revenda);
            ll_bt_entregar_revenda = (LinearLayout) itemView.findViewById(R.id.ll_bt_entregar_revenda);
            ll_bt_cancelar_revenda = (LinearLayout) itemView.findViewById(R.id.ll_bt_cancelar_revenda);
            ll_bt_concluir_revenda = (LinearLayout) itemView.findViewById(R.id.ll_bt_concluir_revenda);

            status_revender = (TextView) itemView.findViewById(R.id.status_revender);

            status_pagamento_comissao = (ToggleButton) itemView.findViewById(R.id.status_pagamento_comissao);

            ll_bt_cancelar_revenda.setOnClickListener(this);
            ll_bt_concluir_revenda.setOnClickListener(this);
            ll_bt_entregar_revenda.setOnClickListener(this);
            ll_bt_confirmar_revenda.setOnClickListener(this);

            nome_revendedor_revenda.setOnClickListener(this);

        }

        public void setEndereco_cliente_revenda(String endereco) {
            this.endereco_cliente_revenda.setText(endereco);
        }

        public void setNumero_cliente_revenda(String num) {
            this.numero_cliente_revenda.setText(num);
        }

        public void setNome_cliente_revenda(String nome) {
            this.nome_cliente_revenda.setText(nome);
        }

        public void setHora_revenda(long hora) {
            String horaString = DateFormatacao.dataCompletaCorrigidaSmall2(new Date(hora), new Date());
            this.hora_revenda.setText(horaString);
        }

        public void setForma_de_pagamento_cliente_revenda(int forma) {

            String formaDePagamento = "Cartão";
            if (forma == 4) {
                formaDePagamento = "Dinheiro";
            }

            this.forma_de_pagamento_cliente_revenda.setText(formaDePagamento);

        }

        public void setNome_revendedor_revenda(String nomeRev) {
            this.nome_revendedor_revenda.setText(nomeRev);
        }

        public void setComissao_revendedor_revenda(int comissao) {
            this.comissao_revendedor_revenda.setText("R$" + comissao + ",00");
        }

        public void setTotal_revenda(int total) {
            this.total_revenda.setText("R$" + total + ",00");
        }

        public void setRv_produtos_revenda(ArrayList<ObjProdutoRevenda> lista, Context context) {
            this.rv_produtos_revenda.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            AdapterProdutosRevenda adapter = new AdapterProdutosRevenda(lista, context);
            this.rv_produtos_revenda.setAdapter(adapter);
        }

        public void setStatus_revender(String status) {
            this.status_revender.setText(status);
        }

        @Override
        public void onClick(View v) {

            ObjectRevenda o = revendaArrayList.get(getAdapterPosition());

            if (v.getId() == R.id.ll_bt_cancelar_revenda) {
                //cancelar
                revendaListener.cancelar(o);
                status_revender.setText("Cancelada");
            } else if (v.getId() == R.id.ll_bt_concluir_revenda) {
                //concluir
                revendaListener.concluir(o);
                status_revender.setText("Concluida");
            } else if (v.getId() == R.id.ll_bt_entregar_revenda) {
                //entregar
                revendaListener.entregar(o);
                status_revender.setText("Saiu para entrega");
            } else if (v.getId() == R.id.ll_bt_confirmar_revenda) {
                //confirmar
                revendaListener.confirmar(o);
                status_revender.setText("Confirmada");
            } else if (v.getId() == R.id.nome_revendedor_revenda) {
                revendaListener.verRevended(o.getUidUserRevendedor(), o.getUserNomeRevendedor(), o.getPathFotoUserRevenda());
            }
        }
    }

}
