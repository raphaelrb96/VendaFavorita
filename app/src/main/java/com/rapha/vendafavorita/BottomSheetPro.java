package com.rapha.vendafavorita;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.rapha.vendafavorita.objects.VariantePrecificacao;
import com.rapha.vendafavorita.util.Calculos;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BottomSheetPro extends BottomSheetDialogFragment {

    private static final String ARG_ITEM_COUNT = "BottomSheet";
    private static final String ARG_ITEM_VALOR = "Valor";
    private static final String ARG_ITEM_COMISSAO = "Comissao";

    private ListenerBottomSheetPro listener;
    private int modeloDePrecificacao = 0;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList<VariantePrecificacao> variantesDePreco;

    public interface ListenerBottomSheetPro {
        void clickBottomSheetPro(String s, int pos, VariantePrecificacao varianteDePreco);
    }

    public void setListener(ListenerBottomSheetPro listener) {
        this.listener = listener;
    }

    public void setModo(int modoDePrecificacao) {
        this.modeloDePrecificacao = modoDePrecificacao;
    }


    public static BottomSheetPro newInstance(int comissao, float valor) {
        final BottomSheetPro fragment = new BottomSheetPro();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, 4);
        args.putInt(ARG_ITEM_COMISSAO, comissao);
        args.putFloat(ARG_ITEM_VALOR, valor);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_pro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<VariantePrecificacao> modelosDePreco = Calculos.getListaPrecificacao(getArguments().getInt(ARG_ITEM_COMISSAO), getArguments().getFloat(ARG_ITEM_VALOR));
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter(modelosDePreco, listener, modeloDePrecificacao);
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }





    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text;
        private TextView valor, quantidade, comissao, aviso;
        private MaterialCardView card_item_modelo_precificacao;
        private ListenerBottomSheetPro listener;
        private Context context;
        private ChangeSelected changeSelected;
        private VariantePrecificacao variantePrecificacao;



        public interface ChangeSelected {
            void onChange(int pos);
        }


        public ViewHolder(@NonNull @NotNull View itemView, ListenerBottomSheetPro listenerPro, ChangeSelected selected) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_item_modelo_preco);
            comissao = (TextView) itemView.findViewById(R.id.comissao_item_modelo_preco);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_item_modelo_preco);
            valor = (TextView) itemView.findViewById(R.id.valor_item_modelo_preco);
            aviso = (TextView) itemView.findViewById(R.id.aviso_item_modelo_preco);
            card_item_modelo_precificacao = (MaterialCardView) itemView.findViewById(R.id.card_item_modelo_precificacao);
            itemView.setOnClickListener(this);
            this.listener = listenerPro;
            this.changeSelected = selected;
            this.context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            listener.clickBottomSheetPro(text.getText().toString(), getLayoutPosition(), variantePrecificacao);
            changeSelected.onChange(getLayoutPosition());
        }

        public void setVariante(VariantePrecificacao variante) {
            this.variantePrecificacao = variante;
        }

        public void btnSelected() {
            card_item_modelo_precificacao.setStrokeWidth(3);
            card_item_modelo_precificacao.setStrokeColor(ContextCompat.getColor(context, R.color.azul_dark));
            text.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_check_box_24_blue_dark), null, null, null);
        }

        public void btnDefault() {
            card_item_modelo_precificacao.setStrokeWidth(1);
            card_item_modelo_precificacao.setStrokeColor(ContextCompat.getColor(context, R.color.cinza_medio));
            text.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_check_box_outline_blank_24_blue_dark), null, null, null);
        }

    }






    static class ItemAdapter extends RecyclerView.Adapter<ViewHolder> implements ViewHolder.ChangeSelected {

        private ListenerBottomSheetPro listenerPro;
        private int selected;
        private ArrayList<VariantePrecificacao> modelos;



        public ItemAdapter(ArrayList<VariantePrecificacao> modelosDePreco, ListenerBottomSheetPro listener, int modo) {
            this.modelos = modelosDePreco;
            this.listenerPro = listener;
            this.selected = modo;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false);
            return new ViewHolder(view, listenerPro, this);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            VariantePrecificacao variante = modelos.get(position);
            holder.text.setText(variante.getNome());
            holder.valor.setText("R$" + (int) variante.getValor());
            holder.comissao.setText("R$" + variante.getComissao());
            holder.quantidade.setText("" + variante.getQuantidadeMinima());

            holder.setVariante(variante);

            if(position == selected) {
                holder.btnSelected();
            } else {
                holder.btnDefault();
            }

        }

        @Override
        public int getItemCount() {
            return modelos.size();
        }

        @Override
        public void onChange(int pos) {
            notifyItemChanged(selected);
            selected = pos;
            notifyItemChanged(pos);
        }
    }
}