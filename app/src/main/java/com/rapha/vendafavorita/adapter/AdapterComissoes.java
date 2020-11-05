package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;
import java.util.Date;

public class AdapterComissoes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ObjectRevenda> listaDeComissoes;
    private ComissoesListener comissoesListener;


    public AdapterComissoes(Context context, ArrayList<ObjectRevenda> listaDeComissoes, ComissoesListener comissoesListener) {
        this.context = context;
        this.listaDeComissoes = listaDeComissoes;
        this.comissoesListener = comissoesListener;
    }


    public interface ComissoesListener {
        void pagamento(boolean pago, String idVenda);
        void click();

        void cancelar(ObjectRevenda obj);
        void confirmar(ObjectRevenda obj);
        void entregar(ObjectRevenda obj);
        void concluir(ObjectRevenda obj);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_revenda, parent, false);
        return new RevendasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {

        RevendasViewHolder holder = (RevendasViewHolder) h;
        ObjectRevenda obj = listaDeComissoes.get(position);


        holder.setHora_revenda(obj.getHora());
        holder.setNome_cliente_revenda(obj.getNomeCliente());
        holder.setNumero_cliente_revenda(obj.getPhoneCliente());
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
                holder.setStatus_revender("Aguardando a confirmação do pedido");
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
            if (obj.getStatusCompra() == 5) {
                holder.setStatus_revender("PAGAMENTO CONCLUIDO");
            }
        } else {
            holder.status_pagamento_comissao.setChecked(false);
        }

            /*
            comissoesViewHolder.valor_comissao.setText(comissao.getComissaoTotal() + ",00");
            comissoesViewHolder.horario_comissao.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(comissao.getHora()), new Date()));
            if (comissao.isPagamentoRecebido()) {
                comissoesViewHolder.status_pagamento_comissao.setChecked(true);
            } else {
                comissoesViewHolder.status_pagamento_comissao.setChecked(false);
            }

            comissoesViewHolder.descricao_comissao.setText("Comissão da minha venda para " + comissao.getNomeCliente());

             */

    }

    @Override
    public int getItemCount() {
        return listaDeComissoes.size();
    }

    class TopoComissoesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img_usuario_comissao;
        private TextView tv_nome_usuario_comissao, tv_time_comeco_usuario_comissao, total_a_receber_comissao, tv_zap_usuario_comissao;
        private ExtendedFloatingActionButton efab_pagar_comissoes;


        public TopoComissoesViewHolder(@NonNull View itemView) {
            super(itemView);
            img_usuario_comissao = (ImageView) itemView.findViewById(R.id.img_usuario_comissao);
            tv_nome_usuario_comissao = (TextView) itemView.findViewById(R.id.tv_nome_usuario_comissao);
            tv_time_comeco_usuario_comissao = (TextView) itemView.findViewById(R.id.tv_time_comeco_usuario_comissao);
            total_a_receber_comissao = (TextView) itemView.findViewById(R.id.total_a_receber_comissao);
            tv_zap_usuario_comissao = (TextView) itemView.findViewById(R.id.tv_zap_usuario_comissao);

            efab_pagar_comissoes = (ExtendedFloatingActionButton) itemView.findViewById(R.id.efab_pagar_comissoes);

            efab_pagar_comissoes.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //comissoesListener.pagarTudo(listaDeComissoes);
        }
    }

    class RevendasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private TextView endereco_cliente_revenda, numero_cliente_revenda, nome_cliente_revenda, hora_revenda;
        private TextView nome_revendedor_revenda, comissao_revendedor_revenda, total_revenda;
        private RecyclerView rv_produtos_revenda;

        private LinearLayout ll_bt_confirmar_revenda, ll_bt_entregar_revenda, ll_bt_cancelar_revenda, ll_bt_concluir_revenda;

        private TextView status_revender;

        private ToggleButton status_pagamento_comissao;

        public RevendasViewHolder(@NonNull View itemView) {

            super(itemView);

            endereco_cliente_revenda = (TextView) itemView.findViewById(R.id.endereco_cliente_revenda);
            numero_cliente_revenda = (TextView) itemView.findViewById(R.id.numero_cliente_revenda);
            nome_cliente_revenda = (TextView) itemView.findViewById(R.id.nome_cliente_revenda);
            hora_revenda = (TextView) itemView.findViewById(R.id.hora_revenda);
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

            status_pagamento_comissao.setOnCheckedChangeListener(this);

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

        public void setNome_revendedor_revenda(String nomeRev) {
            this.nome_revendedor_revenda.setText(nomeRev);
        }

        public void setComissao_revendedor_revenda(int comissao) {
            this.comissao_revendedor_revenda.setText("R$" + comissao + ",00");
        }

        public void setTotal_revenda(int total) {
            this.total_revenda.setText("R$" + total + ",00");
        }

        public void setRv_produtos_revenda(ArrayList<ObjProdutoRevenda> lis, Context context) {
            this.rv_produtos_revenda.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            AdapterProdutosRevenda adapter = new AdapterProdutosRevenda(lis, context);
            this.rv_produtos_revenda.setAdapter(adapter);
        }

        public void setStatus_revender(String status) {
            this.status_revender.setText(status);
        }

        @Override
        public void onClick(View v) {

            ObjectRevenda o = listaDeComissoes.get(getAdapterPosition());

            if (v.getId() == R.id.ll_bt_cancelar_revenda) {
                //cancelar
                comissoesListener.cancelar(o);
                status_revender.setText("Cancelada");
            } else if (v.getId() == R.id.ll_bt_concluir_revenda) {
                //concluir
                comissoesListener.concluir(o);
                status_revender.setText("Concluida");
            } else if (v.getId() == R.id.ll_bt_entregar_revenda) {
                //entregar
                comissoesListener.entregar(o);
                status_revender.setText("Entregue");
            } else if (v.getId() == R.id.ll_bt_confirmar_revenda) {
                //confirmar
                comissoesListener.confirmar(o);
                status_revender.setText("Confirmada");
            }


        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (listaDeComissoes.get(getAdapterPosition()).isPagamentoRecebido() != b) {
                comissoesListener.pagamento(b, listaDeComissoes.get(getAdapterPosition()).getIdCompra());
            }
        }
    }

}
