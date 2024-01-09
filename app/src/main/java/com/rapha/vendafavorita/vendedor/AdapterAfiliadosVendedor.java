package com.rapha.vendafavorita.vendedor;

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

public class AdapterAfiliadosVendedor extends RecyclerView.Adapter<AdapterAfiliadosVendedor.AfiliadosVendedorViewHolder> {


    private Context context;
    private ArrayList<Usuario> afiliados;
    private AfiliadosVendedorListaner listaner;


    public AdapterAfiliadosVendedor(Context context, ArrayList<Usuario> afiliados, AfiliadosVendedorListaner listaner) {
        this.context = context;
        this.afiliados = afiliados;
        this.listaner = listaner;
    }

    public interface AfiliadosVendedorListaner {
        void verAfiliado(Usuario usuario);
    }

    @NonNull
    @Override
    public AfiliadosVendedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_afiliado, parent,false);
        return new AfiliadosVendedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AfiliadosVendedorViewHolder holder, int position) {
        Usuario usuario = afiliados.get(position);

        Glide.with(context).load(usuario.getPathFoto()).into(holder.img_perfil_usuario_afiliado);

        if (usuario.isAdmConfirmado()) {
            holder.icon_afiliado_em_autenticado.setVisibility(View.VISIBLE);
            holder.icon_afiliado_em_analise.setVisibility(View.GONE);
        } else {
            holder.icon_afiliado_em_autenticado.setVisibility(View.GONE);
            holder.icon_afiliado_em_analise.setVisibility(View.VISIBLE);
        }

        if (usuario.getUserName() != null) {
            holder.tv_apelido_afiliado.setText(usuario.getUserName());
        } else {
            holder.tv_apelido_afiliado.setText("Usuario sem apelido");
        }

        if (usuario.getNome() != null) {
            holder.tv_nome_afiliado.setText(usuario.getNome());
        } else {
            holder.tv_nome_afiliado.setText("Usuario sem nome");
        }

        holder.tv_hora_cadastro.setText(DateFormatacao.dataCompletaCorrigidaSmall(new Date(usuario.getUltimoLogin()), new Date()));

    }

    @Override
    public int getItemCount() {
        if (afiliados == null) return 0;

        return afiliados.size();
    }

    class AfiliadosVendedorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img_perfil_usuario_afiliado, icon_afiliado_em_autenticado, icon_afiliado_em_analise;
        private TextView tv_nome_afiliado, tv_apelido_afiliado, tv_hora_cadastro;

        public AfiliadosVendedorViewHolder(@NonNull View itemView) {
            super(itemView);
            img_perfil_usuario_afiliado = (ImageView) itemView.findViewById(R.id.img_perfil_usuario_afiliado);
            icon_afiliado_em_autenticado = (ImageView) itemView.findViewById(R.id.icon_afiliado_em_autenticado);
            icon_afiliado_em_analise = (ImageView) itemView.findViewById(R.id.icon_afiliado_em_analise);
            tv_nome_afiliado = (TextView) itemView.findViewById(R.id.tv_nome_afiliado);
            tv_apelido_afiliado = (TextView) itemView.findViewById(R.id.tv_apelido_afiliado);
            tv_hora_cadastro = (TextView) itemView.findViewById(R.id.tv_hora_cadastro);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listaner.verAfiliado(afiliados.get(getAdapterPosition()));
        }
    }

}
