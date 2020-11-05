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

import java.util.ArrayList;

public class AdapterTopRevendedores extends RecyclerView.Adapter<AdapterTopRevendedores.UserTopRevenda> {

    private Context context;
    private ArrayList<TopRevendedores> revendedores;
    private TopRevendedoresListener topRevendedoresListener;


    public AdapterTopRevendedores(Context context, ArrayList<TopRevendedores> revendedores, TopRevendedoresListener topRevendedoresListener) {
        this.context = context;
        this.revendedores = revendedores;
        this.topRevendedoresListener = topRevendedoresListener;
    }

    public interface TopRevendedoresListener {
        void verRevendedor(String uid, String path, String nome);
    }

    @NonNull
    @Override
    public UserTopRevenda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_strem_view, parent, false);
        return new UserTopRevenda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTopRevenda holder, int position) {
        TopRevendedores topR = revendedores.get(position);
        Glide.with(context).load(topR.getPathFotoRevendedor()).into(holder.imagem);
        holder.hora.setText("Itens: " + topR.getNumeroItensReveendas());
        holder.nome.setText(topR.getNomeRevendedor());
    }

    @Override
    public int getItemCount() {
        if (revendedores == null) return 0;
        return revendedores.size();
    }

    class UserTopRevenda extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nome, hora;
        ImageView imagem;

        public UserTopRevenda(@NonNull View itemView) {
            super(itemView);
            imagem = (ImageView) itemView.findViewById(R.id.img_perfil_stream_view);
            nome = (TextView) itemView.findViewById(R.id.nome_user_strem_view);
            hora = (TextView) itemView.findViewById(R.id.hora_user_strem_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
