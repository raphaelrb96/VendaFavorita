package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;
import java.util.Date;

public class AdapterMeuHistoricoDeVendas extends RecyclerView.Adapter<AdapterMeuHistoricoDeVendas.RevendaMeuHistoricoViewHolder> {

    private Context context;
    private ArrayList<ObjectRevenda> revendaArrayList;


    public AdapterMeuHistoricoDeVendas (Context context, ArrayList<ObjectRevenda> revendaArrayList) {
        this.context = context;
        this.revendaArrayList = revendaArrayList;

    }


    public interface MeuHistoricoRevendasListener {
        void cancelar(ObjectRevenda obj);
        void confirmar(ObjectRevenda obj);
        void entregar(ObjectRevenda obj);
        void concluir(ObjectRevenda obj);
    }


    @NonNull
    @Override
    public RevendaMeuHistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_minhas_revendas, parent, false);
        return new RevendaMeuHistoricoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevendaMeuHistoricoViewHolder holder, int position) {
        ObjectRevenda obj = revendaArrayList.get(position);
        holder.setHora_revenda(obj.getHora());
        holder.setNome_cliente_revenda("Cliente: " + obj.getNomeCliente());
        holder.setNumero_cliente_revenda("Contato: " + obj.getPhoneCliente());
        String rua = obj.getAdress();
        if (obj.getComplemento().length() > 0) {
            rua = rua + " (" + obj.getComplemento() + ")";
        }

        String formaDePagamento = "";

        switch (obj.getFormaDePagar()) {
            default:
                break;
            case 4:
                formaDePagamento = "Forma de pagamento: Dinheiro";
                break;
            case 2:
                formaDePagamento = "Forma de pagamento: Crédito";
                break;
            case 1:
                formaDePagamento = "Forma de pagamento: Débito";
                break;
        }

        holder.setEndereco_cliente_revenda("Endereço: " + rua + "\n" + formaDePagamento);
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
            if (obj.getStatusCompra() == 5) {
                holder.setStatus_revender("PAGAMENTO CONCLUIDO");
            }
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return revendaArrayList.size();
    }

    class RevendaMeuHistoricoViewHolder extends RecyclerView.ViewHolder {

        private TextView endereco_cliente_revenda, numero_cliente_revenda, nome_cliente_revenda, hora_revenda;
        private TextView comissao_revendedor_revenda, total_revenda;
        private RecyclerView rv_produtos_revenda;


        private TextView status_revender;

        public RevendaMeuHistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            status_revender = (TextView) itemView.findViewById(R.id.status_revender);
            endereco_cliente_revenda = (TextView) itemView.findViewById(R.id.endereco_cliente_revenda);
            numero_cliente_revenda = (TextView) itemView.findViewById(R.id.numero_cliente_revenda);
            nome_cliente_revenda = (TextView) itemView.findViewById(R.id.nome_cliente_revenda);
            hora_revenda = (TextView) itemView.findViewById(R.id.hora_revenda);
            comissao_revendedor_revenda = (TextView) itemView.findViewById(R.id.comissao_revendedor_revenda);
            total_revenda = (TextView) itemView.findViewById(R.id.total_revenda);
            rv_produtos_revenda = (RecyclerView) itemView.findViewById(R.id.rv_produtos_revenda);
        }

        public void setEndereco_cliente_revenda(String endereco) {
            this.endereco_cliente_revenda.setText(endereco);
        }

        public void setNumero_cliente_revenda(String num) {
            this.numero_cliente_revenda.setText(num);
        }

        public void setStatus_revender(String status) {
            this.status_revender.setText(status);
        }

        public void setNome_cliente_revenda(String nome) {
            this.nome_cliente_revenda.setText(nome);
        }

        public void setHora_revenda(long hora) {
            String horaString = DateFormatacao.dataCompletaCorrigidaSmall2(new Date(hora), new Date());
            this.hora_revenda.setText(horaString);
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
    }

}
