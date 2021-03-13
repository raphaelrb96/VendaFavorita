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
import com.rapha.vendafavorita.objects.TopAdms;

import java.util.ArrayList;

public class AdapterTopAdm extends RecyclerView.Adapter<AdapterTopAdm.AdmViewHolder>  {

    private Context context;
    private ArrayList<TopAdms> adms;
    private AdmListener listener;

    public AdapterTopAdm(Context context, ArrayList<TopAdms> adms, AdmListener listener) {
        this.context = context;
        this.adms = adms;
        this.listener = listener;
    }

    public interface AdmListener {
        void verAdm(String uid);
    }

    @NonNull
    @Override
    public AdmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_strem_view, parent, false);
        return new AdmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdmViewHolder holder, int position) {

        TopAdms topR = adms.get(position);

        Glide.with(context).load(topR.getPathFotoRevendedor()).into(holder.imagem);
        holder.hora.setText(topR.getNumAfiliados() + " Afiliados");
        holder.nome.setText(topR.getNomeRevendedor());
    }

    @Override
    public int getItemCount() {
        if (adms == null) return 0;
        return adms.size();
    }

    class AdmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nome, hora;
        ImageView imagem;

        public AdmViewHolder(@NonNull View itemView) {
            super(itemView);
            imagem = (ImageView) itemView.findViewById(R.id.img_perfil_stream_view);
            nome = (TextView) itemView.findViewById(R.id.nome_user_strem_view);
            hora = (TextView) itemView.findViewById(R.id.hora_user_strem_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.verAdm(adms.get(getAdapterPosition()).getUidRevendedor());
        }
    }

}
