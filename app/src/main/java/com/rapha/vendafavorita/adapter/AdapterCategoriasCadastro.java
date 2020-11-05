package com.rapha.vendafavorita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapha.vendafavorita.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.rapha.vendafavorita.CadastroDeProdutoActivity.categoriaList;

public class AdapterCategoriasCadastro extends RecyclerView.Adapter<AdapterCategoriasCadastro.CategoriaCadastroItem> {

    private ArrayList<String> arrayList;
    private Context context;
    private MudancaDeCategoria mudancaDeCategoria;

    public AdapterCategoriasCadastro(Context context, MudancaDeCategoria mudancaDeCategoria) {
        this.arrayList = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.categorias)));
        this.context = context;
        this.mudancaDeCategoria = mudancaDeCategoria;
    }

    @Override
    public int getItemViewType(int position) {
        if (categoriaList.contains(String.valueOf(position))) {
            return 1;
        }
        return 0;
    }

    @NonNull
    @Override
    public CategoriaCadastroItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria_cadastro, parent, false);
        CategoriaCadastroItem cadastroItem = new CategoriaCadastroItem(view);
        if (viewType == 1) {
            cadastroItem.checkBox.setChecked(true);
        }
        return cadastroItem;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaCadastroItem holder, int position) {

        holder.checkBox.setText(arrayList.get(position) + " ");
        if(categoriaList.contains(String.valueOf(position))) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface MudancaDeCategoria {
        void mudanca(String id, int pos, boolean adicionar);
    }



    class CategoriaCadastroItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CheckBox checkBox;

        public CategoriaCadastroItem(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_categoria_cadastro);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mudancaDeCategoria.mudanca(arrayList.get(getAdapterPosition()), getAdapterPosition(), checkBox.isChecked());
        }
    }

}
