package com.rapha.vendafavorita.rankings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;

import java.util.ArrayList;

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
        String participantes = "0 vendedores";
        if (obj.getVendedoresParticipantes() != null) {
            participantes = obj.getVendedoresParticipantes() + " vendedores";
        }
        holder.tv_participantes_ranking_adm.setText(participantes);
        holder.tv_premio_ranking_adm.setText("R$ " + obj.getPremio());
        String revendas = "0 vendas";
        if (obj.getRevendasContabilizadas() != null) {
            revendas = obj.getRevendasContabilizadas() + " vendas";
        }
        holder.tv_vendas_ranking_adm.setText(revendas);
    }

    @Override
    public int getItemCount() {
        return rankingObjs.size();
    }

    class RankingItem extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            bt_ver_detalhes_ranking_adm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            rankingListener.verRanking(rankingObjs.get(getAdapterPosition()));
        }
    }

}
