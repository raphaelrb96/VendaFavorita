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

public class AdapterCoresCadastro extends RecyclerView.Adapter<AdapterCoresCadastro.CorCadastroItem> {

    private ArrayList<String> arrayList;
    private Context context;
    private CoresListener coresListener;

    public AdapterCoresCadastro(ArrayList<String> arrayList, Context context, CoresListener coresListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.coresListener = coresListener;
    }

    public interface CoresListener {
        void remov(int pos);
    }


    @NonNull
    @Override
    public CorCadastroItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria_cadastro, parent, false);
        CorCadastroItem corCadastroItem = new CorCadastroItem(view);
        corCadastroItem.checkBox.setChecked(true);
        return corCadastroItem;
    }

    @Override
    public void onBindViewHolder(@NonNull CorCadastroItem holder, int position) {
        String s = arrayList.get(position);
        holder.checkBox.setText(s);
    }

    @Override
    public int getItemCount() {

        if (arrayList != null) {

            return arrayList.size();
        }

        return 0;

    }

    class CorCadastroItem extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private CheckBox checkBox;

        public CorCadastroItem(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_categoria_cadastro);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!b) {
                coresListener.remov(getAdapterPosition());
            }
        }
    }


}
