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
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.SolicitacaoRevendedor;

import java.util.ArrayList;

public class RevendedoresAtivosAdapter extends RecyclerView.Adapter<RevendedoresAtivosAdapter.RevendedorViewHolder> {

    private ArrayList<SolicitacaoRevendedor> revendedores;
    private Context context;
    private RevendedorAtivoListener listener;


    public RevendedoresAtivosAdapter(ArrayList<SolicitacaoRevendedor> revendedores, Context context, RevendedorAtivoListener listener) {
        this.revendedores = revendedores;
        this.context = context;
        this.listener = listener;
    }

    public interface RevendedorAtivoListener {
        void verRevendedor(SolicitacaoRevendedor revendedor);
    }


    @NonNull
    @Override
    public RevendedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_revendedor_ativo, parent, false);
        return new RevendedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevendedorViewHolder holder, int position) {
        SolicitacaoRevendedor revendedor = revendedores.get(position);
        holder.setImagem(revendedor.getFoto(), context);
        holder.setNome(revendedor.getNome());
        if (revendedor.getZap() != null) {
            holder.setNumero(revendedor.getZap());
        }
    }

    @Override
    public int getItemCount() {
        return revendedores.size();
    }

    class RevendedorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView tv_nome_revendedor_ativo, numero;

        public RevendedorViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_revendedor);
            tv_nome_revendedor_ativo = (TextView) itemView.findViewById(R.id.tv_nome_revendedor_ativo);
            numero = (TextView) itemView.findViewById(R.id.tv_numero_revendedor_ativo);
            itemView.setOnClickListener(this);
        }

        public void setImagem(String s, Context context) {
            Glide.with(context).load(s).into(imageView);
        }

        public void setNumero(String n) {
            numero.setText(n);
        }

        public void setNome(String n) {
            tv_nome_revendedor_ativo.setText(n);
        }

        @Override
        public void onClick(View v) {
            listener.verRevendedor(revendedores.get(getAdapterPosition()));
        }
    }

}
