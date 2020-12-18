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
import com.rapha.vendafavorita.ProdObj;
import com.rapha.vendafavorita.R;

import java.util.ArrayList;

public class AdapterProdutosPainelRevendedor extends RecyclerView.Adapter<AdapterProdutosPainelRevendedor.ProdutosRevendaViewHolder> {

    private ArrayList<ProdObj> prodObjs;
    private Context context;
    private ProdutoPainelListener listener;


    public AdapterProdutosPainelRevendedor(ArrayList<ProdObj> prodObjs, Context context, ProdutoPainelListener listener) {
        this.prodObjs = prodObjs;
        this.context = context;
        this.listener = listener;
    }

    public interface ProdutoPainelListener {
        void clickProduto(ProdObj obj);
    }

    @NonNull
    @Override
    public ProdutosRevendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_revenda, parent, false);
        return new ProdutosRevendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutosRevendaViewHolder holder, int position) {
        ProdObj prodObj = prodObjs.get(position);
        holder.setImageView(prodObj.getImgCapa(), context);
        holder.textView.setText("R$ " + prodObj.getComissao() + ",00");
    }

    @Override
    public int getItemCount() {
        return prodObjs.size();
    }

    class ProdutosRevendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;

        public ProdutosRevendaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_capa_prod_rev);
            textView = (TextView) itemView.findViewById(R.id.bt_revender_painel);
            itemView.setOnClickListener(this);
        }


        public void setImageView(String path, Context con) {
            Glide.with(con).load(path).into(imageView);
        }

        @Override
        public void onClick(View v) {
            listener.clickProduto(prodObjs.get(getAdapterPosition()));
        }
    }

}
