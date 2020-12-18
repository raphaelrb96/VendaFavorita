package com.rapha.vendafavorita.rankings.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.rankings.objcts.Participantes;
import com.rapha.vendafavorita.rankings.objcts.RankingObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterRankingList extends RecyclerView.Adapter<AdapterRankingList.RankingItem> {

    private Context context;
    private ArrayList<RankingObj> rankingObjs;
    private RankingListener rankingListener;

    public AdapterRankingList(Context context, ArrayList<RankingObj> rankingObjs, RankingListener rankingListener) {
        this.context = context;
        this.rankingObjs = rankingObjs;
        this.rankingListener = rankingListener;
    }

    public interface RankingListener {
        void verRanking(RankingObj rankingObj);
        void excluir(String id, int p);
        void concluir(String id);
    }

    @NonNull
    @Override
    public RankingItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
        return new RankingItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingItem holder, int position) {
        RankingObj obj = rankingObjs.get(position);
        holder.tv_titulo_ranking_adm.setText(obj.getTitulo());
        holder.tv_fim_ranking_adm.setText("Até: " + obj.getFimString());
        holder.tv_inicio_ranking_adm.setText("De: " + obj.getInicioString());

        ArrayList<ObjectRevenda> revendas = obj.getRevendasContabilizadas();
        ArrayList<Participantes> revendedores = new ArrayList<>();

        int meta = obj.getMeta();

        holder.tv_premio_ranking_adm.setText("R$ " + obj.getPremio());

        if (revendas == null) {
            holder.tv_participantes_ranking_adm.setText("0 Vendedores");
            holder.tv_vendas_ranking_adm.setText("0 Vendas");
            return;
        }

        for (int i = 0; i < revendas.size(); i++) {

            ObjectRevenda objrev = revendas.get(i);

            ArrayList<ObjProdutoRevenda> oprs = objrev.getListaDeProdutos();

            String uidVendaAtual = objrev.getUidUserRevendedor();
            String nomeVendAtual = objrev.getUserNomeRevendedor();
            String pathVendAtual = objrev.getPathFotoUserRevenda();

            int vendas = 1;
            int itens = 0;
            int totalVendido = 0;

            for (int j = 0; j < oprs.size(); j++) {

                ObjProdutoRevenda obRev = oprs.get(j);

                itens = itens + obRev.getQuantidade();

                totalVendido = totalVendido + obRev.getValorTotal();


            }

            Participantes participantes = new Participantes(uidVendaAtual, nomeVendAtual, pathVendAtual, vendas, itens, totalVendido);
            boolean jaExiste = false;
            int indiceNaLista = 0;

            for (int part = 0; part < revendedores.size(); part++) {

                Participantes atual = revendedores.get(part);

                if (atual.getUid().equals(participantes.getUid())) {

                    jaExiste = true;
                    indiceNaLista = part;
                    part = revendedores.size();

                }

            }

            if (jaExiste) {

                revendedores.get(indiceNaLista).setItensVendidos(itens);
                revendedores.get(indiceNaLista).setTotalVendido(totalVendido);
                revendedores.get(indiceNaLista).setVendas(vendas);

            } else {
                revendedores.add(participantes);
            }


        }

        Collections.sort(revendedores, new Comparator<Participantes>() {
            @Override
            public int compare(Participantes participantes, Participantes t1) {
                return Integer.compare(participantes.getItensVendidos(), t1.getItensVendidos());
            }
        });

        Collections.reverse(revendedores);

        String participant = revendedores.size() + " vendedores";

        String revend = "0 vendas";
        if (obj.getRevendasContabilizadas() != null) {
            revend = obj.getRevendasContabilizadas().size() + " vendas";
        }

        holder.tv_participantes_ranking_adm.setText(participant);
        holder.tv_vendas_ranking_adm.setText(revend);
    }

    @Override
    public int getItemCount() {
        return rankingObjs.size();
    }

    class RankingItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View bt_ranking_excluir;
        private View bt_ranking_concluir;
        private TextView bt_ver_detalhes_ranking_adm, tv_vendas_ranking_adm, tv_participantes_ranking_adm, tv_titulo_ranking_adm, tv_premio_ranking_adm, tv_inicio_ranking_adm, tv_fim_ranking_adm;

        public RankingItem(@NonNull View itemView) {
            super(itemView);
            bt_ver_detalhes_ranking_adm = (TextView) itemView.findViewById(R.id.bt_ver_detalhes_ranking_adm);
            tv_titulo_ranking_adm = (TextView) itemView.findViewById(R.id.tv_titulo_ranking_adm);
            tv_premio_ranking_adm = (TextView) itemView.findViewById(R.id.tv_premio_ranking_adm);
            tv_inicio_ranking_adm = (TextView) itemView.findViewById(R.id.tv_inicio_ranking_adm);
            tv_fim_ranking_adm = (TextView) itemView.findViewById(R.id.tv_fim_ranking_adm);
            tv_participantes_ranking_adm = (TextView) itemView.findViewById(R.id.tv_participantes_ranking_adm);
            tv_vendas_ranking_adm = (TextView) itemView.findViewById(R.id.tv_vendas_ranking_adm);
            bt_ranking_concluir = (View) itemView.findViewById(R.id.bt_ranking_concluir);
            bt_ranking_excluir = (View) itemView.findViewById(R.id.bt_ranking_excluir);
            bt_ver_detalhes_ranking_adm.setOnClickListener(this);
            bt_ranking_concluir.setOnClickListener(this);
            bt_ranking_excluir.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.bt_ranking_excluir) {
                rankingListener.excluir(rankingObjs.get(getAdapterPosition()).getId(), getAdapterPosition());
            } else if (view.getId() == R.id.bt_ranking_concluir) {
                rankingListener.concluir(rankingObjs.get(getAdapterPosition()).getId());
            } else {
                rankingListener.verRanking(rankingObjs.get(getAdapterPosition()));
            }
        }
    }

}
