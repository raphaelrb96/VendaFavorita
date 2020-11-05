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
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;

import java.util.ArrayList;
import java.util.Date;

public class ComissoesAfiliadosAdapter extends RecyclerView.Adapter<ComissoesAfiliadosAdapter.ComissoesAfiliados> {

    private Context context;
    private ArrayList<ComissaoAfiliados> comissoesAfiliados;

    public ComissoesAfiliadosAdapter(Context context, ArrayList<ComissaoAfiliados> afiliados) {
        this.context = context;
        this.comissoesAfiliados = afiliados;
    }

    @NonNull
    @Override
    public ComissoesAfiliados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comissao_afiliado, parent, false);
        return new ComissoesAfiliados(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComissoesAfiliados holder, int position) {
        ComissaoAfiliados comissaoAfiliado = comissoesAfiliados.get(position);
        Glide.with(context).load(comissaoAfiliado.getPathVendedor()).into(holder.img_revendedor_comissao_afiliado);
        holder.tv_horario_comissao_afiliado_revenda.setText(DateFormatacao.dataCompletaCorrigida(new Date(comissaoAfiliado.getHora()), new Date()));
        holder.tv_comissao_afiliado_revenda.setText("R$ " + comissaoAfiliado.getComissao() + ",00");
        holder.tv_nome_vendedor_comissao_afiliados.setText(comissaoAfiliado.getNomeVendedor());
        holder.tv_descricao_comissao_afiliado_revenda.setText(comissaoAfiliado.getDescricao());

        switch (comissaoAfiliado.getStatusComissao()) {
            default:
                holder.tv_horario_comissao_afiliado_revenda.setText("Em análise");
                break;
            case 3:
                holder.tv_horario_comissao_afiliado_revenda.setText("Cancelada");
                break;
            case 5:
                holder.tv_horario_comissao_afiliado_revenda.setText("Concluida");
                break;
        }

        if (comissaoAfiliado.getStatusComissao() == 5 && comissaoAfiliado.isPagamentoRecebido()) {
            holder.tv_horario_comissao_afiliado_revenda.setText("PAGAMENTO OK");
        }

    }

    @Override
    public int getItemCount() {
        return comissoesAfiliados.size();
    }

    public class ComissoesAfiliados extends RecyclerView.ViewHolder {

        private ImageView img_revendedor_comissao_afiliado;
        private TextView tv_nome_vendedor_comissao_afiliados, tv_apelido_vendedor_comissao_afiliados, tv_comissao_afiliado_revenda, tv_descricao_comissao_afiliado_revenda, tv_horario_comissao_afiliado_revenda;

        public ComissoesAfiliados(@NonNull View itemView) {
            super(itemView);
            img_revendedor_comissao_afiliado = (ImageView) itemView.findViewById(R.id.img_revendedor_comissao_afiliado);
            tv_nome_vendedor_comissao_afiliados = (TextView) itemView.findViewById(R.id.tv_nome_vendedor_comissao_afiliados);
            tv_apelido_vendedor_comissao_afiliados = (TextView) itemView.findViewById(R.id.tv_apelido_vendedor_comissao_afiliados);
            tv_comissao_afiliado_revenda = (TextView) itemView.findViewById(R.id.tv_comissao_afiliado_revenda);
            tv_horario_comissao_afiliado_revenda = (TextView) itemView.findViewById(R.id.tv_horario_comissao_afiliado_revenda);
            tv_descricao_comissao_afiliado_revenda = (TextView) itemView.findViewById(R.id.tv_descricao_comissao_afiliado_revenda);
        }
    }

}
