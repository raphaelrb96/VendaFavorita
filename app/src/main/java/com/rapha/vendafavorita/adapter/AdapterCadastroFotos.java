package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ImagemPreUpload;

import java.util.ArrayList;

public class AdapterCadastroFotos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ImagemPreUpload> preImagens;
    private CadastroFotosListener listener;

    public AdapterCadastroFotos(Context context, ArrayList<ImagemPreUpload> preImagens, CadastroFotosListener listener) {
        this.context = context;
        this.preImagens = preImagens;
        this.listener = listener;
    }

    public interface CadastroFotosListener {
        void remover(int pos, String path);
        void adicionar();
        void setPrincipal(int pos);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && preImagens.size() == 0) {
            //primeiro da lista vazia
            return 0;
        } else if (preImagens.size() > 0 && position == preImagens.size()) {
            //ultimo da lista cheia
            return 1;
        } else {
            return 2;
            //fotos
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view0 = LayoutInflater.from(context).inflate(R.layout.item_add_foto_full, parent, false);
        View view1 = LayoutInflater.from(context).inflate(R.layout.item_add_foto_small, parent, false);
        View view2 = LayoutInflater.from(context).inflate(R.layout.item_foto_list_criacao, parent, false);

        if (viewType == 0) {
            return new FullAdd(view0);
        } else if (viewType == 1) {
            return new SmallAdd(view1);
        } else {
            return new ImagemAdd(view2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 2) {
            ImagemAdd imagemAddHolder = (ImagemAdd) holder;
            if(preImagens.get(position).getUri() != null) {
                Glide.with(context).load(preImagens.get(position).getUri()).into(imagemAddHolder.imageView);
            } else {
                Glide.with(context).load(preImagens.get(position).getPath()).into(imagemAddHolder.imageView);
            }

            if (preImagens.get(position).taLiberado()) {
                imagemAddHolder.pb_foto_list_criacao.setVisibility(View.GONE);
                imagemAddHolder.imageView.setAlpha((float) 1);
            } else {
                imagemAddHolder.pb_foto_list_criacao.setVisibility(View.VISIBLE);
                imagemAddHolder.imageView.setAlpha((float) 0.5);
            }

            if (preImagens.get(position).isMain()) {
                imagemAddHolder.fab_img_cadastro_principal.setAlpha((float) 1);
            } else {
                imagemAddHolder.fab_img_cadastro_principal.setAlpha((float) 0.3);
            }
        }
    }

    @Override
    public int getItemCount() {
        return preImagens.size() + 1;
    }

    class FullAdd extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView img_cadastro_img_full;

        public FullAdd(@NonNull View itemView) {
            super(itemView);
            img_cadastro_img_full = (CardView) itemView.findViewById(R.id.img_cadastro_img_full);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.adicionar();
        }
    }

    class SmallAdd extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView bt_add_foto_small;

        public SmallAdd(@NonNull View itemView) {
            super(itemView);
            bt_add_foto_small = (CardView) itemView.findViewById(R.id.bt_add_foto_small);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.adicionar();
        }
    }

    class ImagemAdd extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        View fab_img_cadastro_delete, fab_img_cadastro_principal;
        ProgressBar pb_foto_list_criacao;

        public ImagemAdd(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_cadastro);
            fab_img_cadastro_delete = (View) itemView.findViewById(R.id.fab_img_cadastro_delete);
            fab_img_cadastro_principal = (View) itemView.findViewById(R.id.fab_img_cadastro_principal);
            pb_foto_list_criacao = (ProgressBar) itemView.findViewById(R.id.pb_foto_list_criacao);
            fab_img_cadastro_delete.setOnClickListener(this);
            fab_img_cadastro_principal.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.fab_img_cadastro_principal) {
                listener.setPrincipal(getAdapterPosition());
            } else {
                listener.remover(getAdapterPosition(), preImagens.get(getAdapterPosition()).getPath());
            }

        }
    }



}
