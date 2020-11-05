package com.rapha.vendafavorita.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;

public class RevendedorComissoesAdapter extends RecyclerView.Adapter<RevendedorComissoesAdapter.RevendedorComissoesViewHolder> {





    @NonNull
    @Override
    public RevendedorComissoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RevendedorComissoesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RevendedorComissoesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_nome_revededor_comissoes, tv_numero_revendedor_comissoes, tv_revendedor_comissao;
        ImageView img_user_comissoes;
        FrameLayout bt_telefone_revendedor_comissoes;

        public RevendedorComissoesViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_numero_revendedor_comissoes = (TextView) itemView.findViewById(R.id.tv_numero_revendedor_comissoes);
            tv_revendedor_comissao = (TextView) itemView.findViewById(R.id.tv_revendedor_comissao);
            tv_nome_revededor_comissoes = (TextView) itemView.findViewById(R.id.tv_nome_revededor_comissoes);
            img_user_comissoes = (ImageView) itemView.findViewById(R.id.img_user_comissoes);
            bt_telefone_revendedor_comissoes = (FrameLayout) itemView.findViewById(R.id.bt_telefone_revendedor_comissoes);
            itemView.setOnClickListener(this);
            bt_telefone_revendedor_comissoes.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_telefone_revendedor_comissoes) {

            } else {

            }
        }
    }
}
