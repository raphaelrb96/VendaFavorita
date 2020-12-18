package com.rapha.vendafavorita.rankings.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.adapter.AdapterProdutosRevenda;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.rankings.objcts.Participantes;
import com.rapha.vendafavorita.rankings.objcts.RankingObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterResume extends RecyclerView.Adapter<AdapterResume.RankingAtivoVh> {

    private ArrayList<RankingObj> rankings;
    private Context context;
    private String uid;
    private ResumeRankingListener listener;

    public AdapterResume(ArrayList<RankingObj> rankings, Context context, String uid, ResumeRankingListener listener) {
        this.rankings = rankings;
        this.context = context;
        this.uid = uid;
        this.listener = listener;
    }

    public interface ResumeRankingListener {

        void printar(String x);

    }


    @NonNull
    @Override
    public AdapterResume.RankingAtivoVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking_ativo, parent, false);
        return new RankingAtivoVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResume.RankingAtivoVh vh, int position) {

        RankingObj obj = rankings.get(position);

        ArrayList<ObjectRevenda> revendas = obj.getRevendasContabilizadas();
        ArrayList<Participantes> revendedores = new ArrayList<>();

        int meusItensVendidos = 0;
        int minhaColocacao = 0;
        int numVendas = revendas.size();
        int meta = obj.getMeta();


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

                if (uid.equals(objrev.getUidUserRevendedor())) {

                    meusItensVendidos = meusItensVendidos + itens;

                }

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

        for (int i = 0; i < revendedores.size(); i++) {

            if (revendas.get(i).getUidUserRevendedor().equals(uid)) {

                minhaColocacao = i + 1;
                i = revendedores.size();

            }

        }


        vh.tv_titulo_ranking_ativo.setText(obj.getTitulo());
        vh.tv_meta_ranking_ativo.setText(meusItensVendidos + "/" + meta);



        if (minhaColocacao == 0) {
            vh.tv_colocacao_ranking_ativo.setText((revendedores.size() + 1) + "º lugar");
        } else {
            vh.tv_colocacao_ranking_ativo.setText(minhaColocacao + "º lugar");
        }

        vh.tv_premio_ranking_ativo.setText("R$ " + obj.getPremio());
        vh.tv_inicio_ranking_ativo.setText(obj.getInicioString());
        vh.tv_fim_ranking_ativo.setText(obj.getFimString());
        vh.tv_regras_ranking_ativo.setText(obj.getRegras());
        vh.tv_descricao_ranking_ativo.setText(obj.getDescricao());
        vh.tv_participantes_ranking_ativo_resume.setText((revendedores.size() + 1) + " Pessoas");
        if (revendedores.size() > 0) {
            vh.setRv(revendedores, context);
        }

    }

    @Override
    public int getItemCount() {
        if (rankings == null) return 0;

        return rankings.size();
    }

    class RankingAtivoVh extends RecyclerView.ViewHolder {

        TextView tv_titulo_ranking_ativo, tv_list_ranking_ativo_classificacao, tv_regras_ranking_ativo, tv_descricao_ranking_ativo, tv_meta_ranking_ativo, tv_colocacao_ranking_ativo, tv_premio_ranking_ativo, tv_inicio_ranking_ativo, tv_fim_ranking_ativo, tv_participantes_ranking_ativo_resume;

        RecyclerView rv_ranking_ativo_classificacao;

        public RankingAtivoVh(@NonNull View itemView) {
            super(itemView);
            tv_titulo_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_titulo_ranking_ativo_resume);
            tv_meta_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_meta_ranking_ativo_resume);
            tv_colocacao_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_colocacao_ranking_ativo_resume);
            tv_premio_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_premio_ranking_ativo_resume);
            tv_inicio_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_inicio_ranking_ativo_resume);
            tv_fim_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_fim_ranking_ativo_resume);
            tv_regras_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_regras_ranking_ativo);
            tv_descricao_ranking_ativo = (TextView) itemView.findViewById(R.id.tv_descricao_ranking_ativo);
            tv_participantes_ranking_ativo_resume = (TextView) itemView.findViewById(R.id.tv_participantes_ranking_ativo_resume);
            tv_list_ranking_ativo_classificacao = (TextView) itemView.findViewById(R.id.tv_list_ranking_ativo_classificacao);
            rv_ranking_ativo_classificacao = (RecyclerView) itemView.findViewById(R.id.rv_ranking_ativo_classificacao);
        }

        public void setRv (ArrayList<Participantes> lista, Context context) {
            this.rv_ranking_ativo_classificacao.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            AdapterClassificacaoRanking adapter = new AdapterClassificacaoRanking(context, lista);
            this.rv_ranking_ativo_classificacao.setAdapter(adapter);
        }

    }

    class RankingConcluido extends RecyclerView.ViewHolder {

        TextView tv_titulo_ranking_concluido, premio_ranking_concluido, tv_vencedores_ranking_concluido, tv_vendas_ranking_concluido, tv_inicio_ranking_concluido, tv_fim_ranking_concluido, tv_participantes_ranking_concluido;

        public RankingConcluido(@NonNull View itemView) {
            super(itemView);
            tv_titulo_ranking_concluido = (TextView) itemView.findViewById(R.id.tv_titulo_ranking_concluido);
            tv_inicio_ranking_concluido = (TextView) itemView.findViewById(R.id.tv_inicio_ranking_concluido);
            tv_fim_ranking_concluido = (TextView) itemView.findViewById(R.id.tv_fim_ranking_concluido);
            tv_participantes_ranking_concluido = (TextView) itemView.findViewById(R.id.tv_participantes_ranking_concluido);
            tv_vendas_ranking_concluido = (TextView) itemView.findViewById(R.id.tv_vendas_ranking_concluido);
            tv_vencedores_ranking_concluido = (TextView) itemView.findViewById(R.id.tv_vencedores_ranking_concluido);
            premio_ranking_concluido = (TextView) itemView.findViewById(R.id.premio_ranking_concluido);
        }
    }

}
