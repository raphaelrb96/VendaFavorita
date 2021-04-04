package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;

import java.util.ArrayList;

public class CoresAdapterRevenda extends RecyclerView.Adapter<CoresAdapterRevenda.ViewCorViewHolder> {

    private Context context;
    private ArrayList<String> cores;
    private CoresListenerRevenda listener;
    private int posicaoChecada;


    public CoresAdapterRevenda(Context context, ArrayList<String> cores, CoresListenerRevenda listener, int posicaoChecada) {
        this.context = context;
        this.cores = cores;
        this.listener = listener;
        this.posicaoChecada = posicaoChecada;
    }

    public interface CoresListenerRevenda {
        void escolherCor(String cor, int pos);
    }

    @NonNull
    @Override
    public ViewCorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cores_produto_revenda, parent, false);
        return new ViewCorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCorViewHolder holder, int position) {
        holder.cb.setText(cores.get(position));

        if (posicaoChecada == -1) return;

        if (posicaoChecada == position) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(false);
        }

    }

    public void trocarCor(int posNova) {

        posicaoChecada = posNova;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (cores == null) return 0;
        return cores.size();
    }

    class ViewCorViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        CheckBox cb;

        public ViewCorViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = (CheckBox) itemView.findViewById(R.id.cb_cores_prod_revenda);
            cb.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (!b) return;

            listener.escolherCor(cores.get(getAdapterPosition()), getAdapterPosition());
        }
    }

}
