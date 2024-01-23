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
import com.rapha.vendafavorita.objects.Usuario;

import java.util.ArrayList;
import java.util.Date;

public class AdapterAfiliados extends RecyclerView.Adapter<AdapterAfiliados.AfiliadosViewHolder> {


    private ArrayList<Usuario> usuarios;
    private Context context;
    private String authUid;

    public AdapterAfiliados(ArrayList<Usuario> usuarios, Context context, String uid) {
        this.usuarios = usuarios;
        this.context = context;
        this.authUid = uid;
    }

    @NonNull
    @Override
    public AfiliadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_afiliado, parent, false);
        return new AfiliadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AfiliadosViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        if (usuario.getUid().equals(authUid)) {
            holder.desativeItem();
            return;
        }

        Glide.with(context).load(usuario.getPathFoto()).into(holder.img_perfil_usuario_afiliado);

        if (usuario.isAdmConfirmado()) {
            holder.icon_afiliado_em_autenticado.setVisibility(View.VISIBLE);
            holder.icon_afiliado_em_analise.setVisibility(View.GONE);
        } else {
            holder.icon_afiliado_em_autenticado.setVisibility(View.GONE);
            holder.icon_afiliado_em_analise.setVisibility(View.VISIBLE);
        }

        if (usuario.getUserName() != null || usuario.getUserName().length() > 1 || !usuario.getUserName().equals(" ")) {
            holder.tv_apelido_afiliado.setText(usuario.getUserName());
        } else {
            holder.tv_apelido_afiliado.setText("Usuario sem apelido");
        }

        if (usuario.getNome() != null) {
            holder.tv_nome_afiliado.setText(usuario.getNome());
        } else {
            holder.tv_nome_afiliado.setText("Usuario sem nome");
        }

        String dataHora = DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuario.getPrimeiroLogin()), new Date());
        holder.tv_hora_cadastro.setText(dataHora);
    }

    @Override
    public int getItemCount() {
        if (usuarios == null) return 0;

        if (usuarios.size() > 100) return 100;

        return usuarios.size();
    }

    class AfiliadosViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_perfil_usuario_afiliado, icon_afiliado_em_autenticado, icon_afiliado_em_analise;
        private TextView tv_nome_afiliado, tv_apelido_afiliado, tv_hora_cadastro;

        private View container;

        public AfiliadosViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            img_perfil_usuario_afiliado = (ImageView) itemView.findViewById(R.id.img_perfil_usuario_afiliado);
            icon_afiliado_em_autenticado = (ImageView) itemView.findViewById(R.id.icon_afiliado_em_autenticado);
            icon_afiliado_em_analise = (ImageView) itemView.findViewById(R.id.icon_afiliado_em_analise);
            tv_nome_afiliado = (TextView) itemView.findViewById(R.id.tv_nome_afiliado);
            tv_apelido_afiliado = (TextView) itemView.findViewById(R.id.tv_apelido_afiliado);
            tv_hora_cadastro = (TextView) itemView.findViewById(R.id.tv_hora_cadastro);
        }


        public void desativeItem() {
            container.setVisibility(View.GONE);
        }
    }

}
