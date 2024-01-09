package com.rapha.vendafavorita;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomSheetCateg extends BottomSheetDialogFragment {

    private static final String ARG_CATEGORIAS = "Categorias";
    private ListenerBottomSheetCategoria listener;
    private Context context;

    public interface ListenerBottomSheetCategoria {
        void clickBottomSheetCategoria(String s, int pos);
    }

    public void setListener(ListenerBottomSheetCategoria listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void show(@NonNull @NotNull FragmentManager manager, @Nullable @org.jetbrains.annotations.Nullable String tag) {
        super.show(manager, tag);
    }

    public static BottomSheetCateg newInstance() {

        Bundle args = new Bundle();

        BottomSheetCateg fragment = new BottomSheetCateg();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_categoria, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_categoria_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(listener));
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text;
        private BottomSheetCateg.ListenerBottomSheetCategoria listener;


        public ViewHolder(@NonNull @NotNull View itemView, BottomSheetCateg.ListenerBottomSheetCategoria listenerPro) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_item_categ);
            itemView.setOnClickListener(this);
            this.listener = listenerPro;
        }

        @Override
        public void onClick(View v) {
            listener.clickBottomSheetCategoria(text.getText().toString(), getLayoutPosition());
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<BottomSheetCateg.ViewHolder> {

        private ListenerBottomSheetCategoria listenerBottomSheetCategoria;
        private ArrayList<String> categs;


        public ItemAdapter(ListenerBottomSheetCategoria listenerBottomSheetCategoria) {
            this.listenerBottomSheetCategoria = listenerBottomSheetCategoria;
            this.categs = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.categorias)));
        }

        @NonNull
        @Override
        public BottomSheetCateg.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_sheet_categoria, parent, false);
            return new BottomSheetCateg.ViewHolder(view, listenerBottomSheetCategoria);
        }

        @Override
        public void onBindViewHolder(BottomSheetCateg.ViewHolder holder, int position) {
            String nome = categs.get(position);
            holder.text.setText(nome);
        }

        @Override
        public int getItemCount() {
            if(categs == null) return 0;
            return categs.size();
        }

    }
}
