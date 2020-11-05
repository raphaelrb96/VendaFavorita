package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rapha.vendafavorita.R;

import java.util.ArrayList;

public class FotosDetalheProdutosAdapter extends RecyclerView.Adapter<FotosDetalheProdutosAdapter.FotoViewHolder> {

    private Context context;
    private ArrayList<String> fotos;
    private FotoDetalheListener listener;


    public interface FotoDetalheListener {
        void selecionado(String path);
    }


    public FotosDetalheProdutosAdapter(Context context, ArrayList<String> fotos, FotoDetalheListener listener) {
        this.context = context;
        this.fotos = fotos;
        this.listener = listener;
    }


    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto_detalhes, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        holder.carregarFoto(fotos.get(position), context);
    }

    @Override
    public int getItemCount() {
        if (fotos == null) return 0;
        return fotos.size();
    }

    class FotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_detalhes);
            imageView.setOnClickListener(this);
        }

        public void carregarFoto(String path, Context context) {
            Glide.with(context).load(path).into(imageView);
        }

        @Override
        public void onClick(View view) {

            String path = fotos.get(getAdapterPosition());

            listener.selecionado(path);
        }
    }

}
