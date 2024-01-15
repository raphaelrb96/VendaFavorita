package com.rapha.vendafavorita.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bijoysingh.starter.recyclerview.RecyclerViewHolder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.EntregaObj;
import com.rapha.vendafavorita.objects.GarantiaObj;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.util.FormatoString;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import static com.rapha.vendafavorita.Constantes.getMotivoCancelamento;

public class AdapterMeuHistoricoDeVendas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ObjectRevenda> revendaArrayList;
    private OnChangeListVendasListener listener;
    private int fromIndex, toIndex;


    public AdapterMeuHistoricoDeVendas (Context context, ArrayList<ObjectRevenda> revendaArrayList, OnChangeListVendasListener listener) {
        this.context = context;
        this.revendaArrayList = revendaArrayList;
        this.listener = listener;
        this.fromIndex = 0;
    }

    public interface OnChangeListVendasListener {
        void onChangeList();
    }


    public interface MeuHistoricoRevendasListener {
        void cancelar(ObjectRevenda obj);
        void confirmar(ObjectRevenda obj);
        void entregar(ObjectRevenda obj);
        void concluir(ObjectRevenda obj);
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 20) return 2;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_minhas_revendas, parent, false);
            return new RevendaMeuHistoricoViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_ver_mais, parent, false);
            return new VerMaisViewHolder(view);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        if(h.getItemViewType() == 1) {
            RevendaMeuHistoricoViewHolder holder = (RevendaMeuHistoricoViewHolder) h;
            ObjectRevenda obj = revendaArrayList.get(position);
            holder.setHora_revenda(obj.getHora());
            holder.setNome_cliente_revenda("" + obj.getNomeCliente());
            holder.setNumero_cliente_revenda("" + obj.getPhoneCliente());
            String rua = obj.getAdress();
            String cidade = obj.getCidade() != null ? obj.getCidade() : "Manaus";
            String estado = obj.getEstado() != null ? obj.getEstado() : "Amazonas";
            String cep = obj.getCep() != null ? obj.getCep() : "69000-000";

            String observacoes = "Nenhuma";

            if(obj.getObs() != null) {
                if(obj.getObs().length() > 1) {
                    observacoes = obj.getObs();
                }
            }

            holder.obs_item_revenda.setText(observacoes);

            String formaDePagamento = "";
            String parcelamento = "";

            if(obj.getParcelaFinal() != null) {
                parcelamento = obj.getParcelaFinal().getTitulo() + " de " + FormatoString.formartarPreco(obj.getParcelaFinal().getTotal() / obj.getParcelaFinal().getId()) + "\n";
            }

            int formaDePagamentoFinal = obj.getPagamentoFinal() != null ? obj.getPagamentoFinal().getId() : obj.getFormaDePagar();
            switch (formaDePagamentoFinal) {
                default:
                    break;
                case 4:
                    formaDePagamento = "Dinheiro";
                    break;
                case 2:
                case 3:
                    formaDePagamento = parcelamento + "Crédito";
                    break;
                case 1:
                    formaDePagamento = "Débito";
                    break;
                case 5:
                    formaDePagamento = "Pix";
                    break;
                case 6:
                    formaDePagamento = parcelamento + "Link";
                    break;
            }

            holder.setTaxa(obj.getEntregaFinal());
            holder.setGarantia(obj.getGarantiaFinal());
            holder.setEndereco_cliente_revenda(rua, cidade, estado, cep);
            if (obj.getComplemento().length() > 0) {
                holder.setBairro_cliente_revenda(obj.getComplemento().toUpperCase());
            }
            holder.setForma_pag_revenda(formaDePagamento);
            holder.setComissao_revendedor_revenda(obj.getComissaoTotal());
            holder.setTotal_revenda(obj.getValorTotal());
            holder.setRv_produtos_revenda(obj.getListaDeProdutos(), context);
            int status = obj.getStatusCompra();
            switch (status) {
                case 1:
                    holder.setStatus_revender("Aguardando a confirmação");
                    holder.setCardColor(ContextCompat.getColor(context, R.color.cinza_medio));
                    holder.close_motivo_cancelamento();
                    break;
                case 2:
                    holder.setStatus_revender("Confirmada");
                    holder.close_motivo_cancelamento();
                    holder.setCardColor(ContextCompat.getColor(context, R.color.colorSecondary));

                    break;
                case 3:
                    holder.setStatus_revender("Cancelada");
                    holder.setCardColor(ContextCompat.getColor(context, R.color.verdeDark));
                    if (obj.getIdCancelamento() > 0) {
                        holder.set_motivo_cancelamento(getMotivoCancelamento(obj.getIdCancelamento()));
                    } else {
                        if (obj.getIdCancelamento() == -1) {
                            holder.set_motivo_cancelamento(obj.getDetalheCancelamento());
                        }
                    }
                    break;
                case 4:
                    holder.setStatus_revender("Saiu para entrega");
                    holder.close_motivo_cancelamento();
                    holder.setCardColor(ContextCompat.getColor(context, R.color.red_dark));
                    holder.close_motivo_cancelamento();
                    break;
                case 5:
                    holder.setStatus_revender("Concluida");
                    holder.close_motivo_cancelamento();
                    holder.setCardColor(ContextCompat.getColor(context, R.color.verde));
                    break;
                default:
                    break;
            }

            if (obj.isPagamentoRecebido()) {
                if (obj.getStatusCompra() == 5) {
                    holder.setStatus_revender("PAGAMENTO CONCLUIDO");
                    holder.setCardColor(ContextCompat.getColor(context, R.color.cinza_text));

                }
            } else {

            }

        } else {
            if(revendaArrayList.size() < 20) {
                VerMaisViewHolder vholder = (VerMaisViewHolder) h;
                vholder.setGone();
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = revendaArrayList.size();
        if(size < 20) {
            return revendaArrayList.subList(0, size - 1).size() + 1;
        } else {
            return 20 + 1;
        }
    }

    private void getNewList() {
        int size = revendaArrayList.size();
        ArrayList<ObjectRevenda> listNew = new ArrayList<ObjectRevenda>(revendaArrayList.subList(20, size - 1));
        setRevendaArrayList(listNew);
    }

    public void setRevendaArrayList(ArrayList<ObjectRevenda> list) {
        revendaArrayList = list;
        notifyDataSetChanged();
        listener.onChangeList();
    }

    class RevendaMeuHistoricoViewHolder extends RecyclerView.ViewHolder {

        private TextView endereco_cliente_revenda, numero_cliente_revenda, nome_cliente_revenda, hora_revenda;
        private TextView comissao_revendedor_revenda, total_revenda, bairro_cliente_revenda, cidade_cliente_revenda, estado_cliente_revenda;
        private RecyclerView rv_produtos_revenda;
        private TextView forma_pag_revenda, status_revender_motivo_cancelamento, obs_item_revenda, cep, taxa, garantia;
        private MaterialCardView card_comissao_revendedor_revenda;


        private TextView status_revender;

        public RevendaMeuHistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            status_revender = (TextView) itemView.findViewById(R.id.status_revender);
            bairro_cliente_revenda = (TextView) itemView.findViewById(R.id.bairro_cliente_revenda);
            endereco_cliente_revenda = (TextView) itemView.findViewById(R.id.endereco_cliente_revenda);
            numero_cliente_revenda = (TextView) itemView.findViewById(R.id.numero_cliente_revenda);
            forma_pag_revenda = (TextView) itemView.findViewById(R.id.forma_pag_revenda);
            nome_cliente_revenda = (TextView) itemView.findViewById(R.id.nome_cliente_revenda);
            hora_revenda = (TextView) itemView.findViewById(R.id.hora_revenda);
            comissao_revendedor_revenda = (TextView) itemView.findViewById(R.id.comissao_revendedor_revenda);
            total_revenda = (TextView) itemView.findViewById(R.id.total_revenda);
            cidade_cliente_revenda = (TextView) itemView.findViewById(R.id.cidade_cliente_revenda);
            obs_item_revenda = (TextView) itemView.findViewById(R.id.obs_item_revenda);
            estado_cliente_revenda = (TextView) itemView.findViewById(R.id.estado_cliente_revenda);
            cep = (TextView) itemView.findViewById(R.id.cep_cliente_revenda);
            taxa = (TextView) itemView.findViewById(R.id.taxa_entrega_cliente_revenda);
            garantia = (TextView) itemView.findViewById(R.id.garantia_item_revenda);
            status_revender_motivo_cancelamento = (TextView) itemView.findViewById(R.id.status_revender_motivo_cancelamento);
            rv_produtos_revenda = (RecyclerView) itemView.findViewById(R.id.rv_produtos_revenda);
            card_comissao_revendedor_revenda = (MaterialCardView) itemView.findViewById(R.id.card_comissao_revendedor_revenda);
        }

        public void setGarantia(GarantiaObj garantiaObj) {
            if(garantiaObj == null) {
                String texto = "7 dias pra Troca";
                this.garantia.setText(texto);
                return;
            }
            String titulo = garantiaObj.getTitulo();
            String desc = garantiaObj.getValorString();
            String texto = titulo + "\n" + desc;
            this.garantia.setText(texto);
        }

        public void setTaxa(EntregaObj entregaObj) {
            if(entregaObj == null) {
                this.taxa.setText("Entrega Local\nGrátis");
                return;
            }

            String titulo = entregaObj.getTitulo();
            String desc = entregaObj.getValorString();
            String texto = titulo + "\n" + desc;
            this.taxa.setText(texto);
        }

        public void setCardColor(int cor) {
            this.card_comissao_revendedor_revenda.setStrokeColor(cor);
            this.status_revender.setTextColor(cor);
            this.comissao_revendedor_revenda.setTextColor(cor);
        }

        public void set_motivo_cancelamento(String motivo) {
            this.status_revender_motivo_cancelamento.setText(motivo);
            this.status_revender_motivo_cancelamento.setVisibility(View.VISIBLE);
        }

        public void close_motivo_cancelamento() {
            this.status_revender_motivo_cancelamento.setVisibility(View.GONE);
        }

        public void setForma_pag_revenda(String s) {
            this.forma_pag_revenda.setText(s);
        }

        public void setNumero_cliente_revenda(String num) {
            this.numero_cliente_revenda.setText(num);
        }

        public void setBairro_cliente_revenda(String bairro) {
            this.bairro_cliente_revenda.setText(bairro);
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
            this.total_revenda.setText(FormatoString.formartarPreco(total) + ",00");
        }

        public void setRv_produtos_revenda(ArrayList<ObjProdutoRevenda> lista, Context context) {
            this.rv_produtos_revenda.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            AdapterProdutosRevenda adapter = new AdapterProdutosRevenda(lista, context);
            this.rv_produtos_revenda.setAdapter(adapter);
        }

        public void setEndereco_cliente_revenda(String rua, String cidade, String estado, String cep) {
            this.endereco_cliente_revenda.setText(rua);
            this.cidade_cliente_revenda.setText(cidade);
            this.estado_cliente_revenda.setText(estado);
            this.cep.setText(cep);
        }
    }

    class VerMaisViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialButton bt_ver_mais;

        public VerMaisViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bt_ver_mais = (MaterialButton) itemView.findViewById(R.id.bt_ver_mais);
            bt_ver_mais.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            getNewList();
        }

        public void setGone() {
            itemView.setVisibility(View.GONE);
        }
    }

}
