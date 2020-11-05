package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.SolicitacaoRevendedor;

import java.util.ArrayList;

public class AdapterSolicitacoesRevendas extends RecyclerView.Adapter<AdapterSolicitacoesRevendas.SolicitacoesRevendas>  {

    private ArrayList<SolicitacaoRevendedor> solicitacaoRevendedores;
    private Context context;
    private SolicitacoesClickListener solicitacoesClickListener;


    public AdapterSolicitacoesRevendas(ArrayList<SolicitacaoRevendedor> solicitacaoRevendedores, Context context, SolicitacoesClickListener solicitacoesClickListener) {
        this.solicitacaoRevendedores = solicitacaoRevendedores;
        this.context = context;
        this.solicitacoesClickListener = solicitacoesClickListener;
    }

    public interface SolicitacoesClickListener {
        void liberarRevendedor(SolicitacaoRevendedor solicitacaoRevendedor, int position);
    }


    @NonNull
    @Override
    public SolicitacoesRevendas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_solicitacoes_revendedores, parent, false);
        return new SolicitacoesRevendas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitacoesRevendas holder, int position) {
        SolicitacaoRevendedor solicitacaoRevendedor = solicitacaoRevendedores.get(position);
        if (solicitacaoRevendedor.getFoto().length() > 0 || solicitacaoRevendedor.getFoto() != null) {
            holder.setFoto(solicitacaoRevendedor.getFoto(), context);
        }

        holder.setTvName(solicitacaoRevendedor.getNome());
        holder.setTvObs(solicitacaoRevendedor.getObs());
        holder.setTvZap(solicitacaoRevendedor.getZap());
    }

    @Override
    public int getItemCount() {
        return solicitacaoRevendedores.size();
    }

    class SolicitacoesRevendas extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName, tvZap, tvObs;
        private ImageView foto;
        private Button btLiberar;

        public SolicitacoesRevendas(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_nome_revendedor);
            tvZap = (TextView) itemView.findViewById(R.id.tv_whatapp_revendedor);
            tvObs = (TextView) itemView.findViewById(R.id.tv_obs_revendedor);
            foto = (ImageView) itemView.findViewById(R.id.img_solicitacoes_revendedores);
            btLiberar = (Button) itemView.findViewById(R.id.bt_liberar_revendedores);
            btLiberar.setOnClickListener(this);
        }

        public void setTvName(String name) {
            tvName.setText(name);
        }

        public void setTvZap(String zap) {
            tvZap.setText(zap);
        }

        public void setTvObs(String obs) {
            tvObs.setText(obs);
        }

        public void setFoto(String path, Context context) {
            Glide.with(context).load(path).into(foto);
        }

        public void exibiButton(boolean exibir) {
            if (exibir) {
                btLiberar.setVisibility(View.VISIBLE);
            } else {
                btLiberar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            SolicitacaoRevendedor solicitacaoRevendedor = solicitacaoRevendedores.get(getAdapterPosition());
            solicitacoesClickListener.liberarRevendedor(solicitacaoRevendedor, getAdapterPosition());
            exibiButton(false);
        }
    }

}
