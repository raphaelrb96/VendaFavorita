package com.rapha.vendafavorita;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.rapha.vendafavorita.objects.EntregaObj;
import com.rapha.vendafavorita.objects.GarantiaObj;
import com.rapha.vendafavorita.objects.PagamentosObj;
import com.rapha.vendafavorita.objects.ParcelamentoObj;
import com.rapha.vendafavorita.util.Listas;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomSheetOptions extends BottomSheetDialogFragment {

    private static final String ARG_TYPE_GARANTIA = "GARANTIA";
    private static final String ARG_TYPE_ENTREGA = "ENTREGA";
    private static final String ARG_TYPE_PAGAMENTO = "PAGAMENTO";
    private static final String ARG_TYPE_PARCELAMENTO = "PARCELAMENTO";
    private static final String ARG_TYPE = "option";
    private static final String ARG_TOTAL = "total";
    private static final String ARG_ID = "id";

    public static final int TYPE_GARANTIA = 1;
    public static final int TYPE_ENTREGA = 2;
    public static final int TYPE_PAGAMENTO = 3;
    public static final int TYPE_PARCELAMENTO = 4;


    private ListenerBottomSheetOption listener;
    private Context context;
    private RecyclerView recyclerView;

    public interface ListenerBottomSheetOption {
        void clickBottomSheetOption(String s, int pos, int type, int id);
    }

    public void setListener(ListenerBottomSheetOption listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void show(@NonNull @NotNull FragmentManager manager, @Nullable @org.jetbrains.annotations.Nullable String tag) {
        super.show(manager, tag);
    }

    public static BottomSheetOptions newInstance(int type, int total, int id) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        args.putInt(ARG_TOTAL, total);
        args.putInt(ARG_ID, id);
        BottomSheetOptions fragment = new BottomSheetOptions();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_option, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (getArguments() == null) {
            return;
        }

        int aType = getArguments().getInt(ARG_TYPE);
        int aTotal = getArguments().getInt(ARG_TOTAL);
        int aId = getArguments().getInt(ARG_ID);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_option);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemAdapter itemAdapter = new ItemAdapter(listener, aType, aId);
        switch (aType) {
            case TYPE_GARANTIA:
                itemAdapter.setGarantiaObjs(Listas.getListOptionsGarantia(aTotal));
                break;
            case TYPE_ENTREGA:
                itemAdapter.setEntregaObjs(Listas.getListOptionsEntregas());
                break;
            case TYPE_PAGAMENTO:
                itemAdapter.setPagamentosObjs(Listas.getListOptionsPagamentos());
                break;
            case TYPE_PARCELAMENTO:
                itemAdapter.setParcelamentoObjs(Listas.getListOptionsParcelamento(aTotal));
                break;
        }
        recyclerView.setAdapter(itemAdapter);


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialCardView card_item_option;
        private TextView text;
        private TextView descricao, valor;
        private int type;
        private BottomSheetOptions.ListenerBottomSheetOption listener;
        private int id;
        private Context context;


        public ViewHolder(@NonNull @NotNull View itemView, BottomSheetOptions.ListenerBottomSheetOption listenerPro, int type) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_item_option);
            descricao = (TextView) itemView.findViewById(R.id.desc_item_option);
            valor = (TextView) itemView.findViewById(R.id.valor_item_option);
            card_item_option = (MaterialCardView) itemView.findViewById(R.id.card_item_option);
            itemView.setOnClickListener(this);
            this.listener = listenerPro;
            this.type = type;
            this.context = itemView.getContext();
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            listener.clickBottomSheetOption(text.getText().toString(), getLayoutPosition(), type, id);
        }

        public void btnSelected() {
            card_item_option.setStrokeWidth(3);
            card_item_option.setStrokeColor(ContextCompat.getColor(context, R.color.azul_dark));
            text.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_check_box_24_blue_dark), null, null, null);
        }

        public void btnDefault() {
            card_item_option.setStrokeWidth(1);
            card_item_option.setStrokeColor(ContextCompat.getColor(context, R.color.cinza_medio));
            text.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_check_box_outline_blank_24_blue_dark), null, null, null);
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<BottomSheetOptions.ViewHolder> {

        private ListenerBottomSheetOption listenerBottomSheetCategoria;
        private int type;
        private int idSelected;
        private ArrayList<GarantiaObj> garantiaObjs;
        private ArrayList<EntregaObj> entregaObjs;
        private ArrayList<PagamentosObj> pagamentosObjs;
        private ArrayList<ParcelamentoObj> parcelamentoObjs;


        public ItemAdapter(ListenerBottomSheetOption listenerBottomSheetCategoria, int type, int id) {
            this.listenerBottomSheetCategoria = listenerBottomSheetCategoria;
            this.type = type;
            this.idSelected = id;
        }

        public void setGarantiaObjs(ArrayList<GarantiaObj> garantiaObjs) {
            this.garantiaObjs = garantiaObjs;
        }

        public void setEntregaObjs(ArrayList<EntregaObj> entregaObjs) {
            this.entregaObjs = entregaObjs;
        }

        public void setPagamentosObjs(ArrayList<PagamentosObj> pagamentosObjs) {
            this.pagamentosObjs = pagamentosObjs;
        }

        public void setParcelamentoObjs(ArrayList<ParcelamentoObj> parcelamentoObjs) {
            this.parcelamentoObjs = parcelamentoObjs;
        }

        @NonNull
        @Override
        public BottomSheetOptions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_sheet_option, parent, false);
            return new BottomSheetOptions.ViewHolder(view, listenerBottomSheetCategoria, type);
        }

        @Override
        public void onBindViewHolder(BottomSheetOptions.ViewHolder holder, int position) {

            String titulo = "";
            String descricao = "";
            String valorString = "";
            int id = 1;

            switch (type) {
                case TYPE_GARANTIA:
                    if(garantiaObjs != null) {
                        GarantiaObj garantiaObj = garantiaObjs.get(position);
                        titulo = garantiaObj.getTitulo();
                        descricao = garantiaObj.getDescricao();
                        valorString = garantiaObj.getValorString();
                        id = garantiaObj.getId();
                    }
                    break;
                case TYPE_ENTREGA:
                    if(entregaObjs != null) {
                        EntregaObj obj = entregaObjs.get(position);
                        titulo = obj.getTitulo();
                        descricao = obj.getDescricao();
                        valorString = obj.getValorString();
                        id = obj.getId();
                    }
                    break;
                case TYPE_PAGAMENTO:
                    if(pagamentosObjs != null) {
                        PagamentosObj obj = pagamentosObjs.get(position);
                        titulo = obj.getTitulo();
                        descricao = obj.getDescricao();
                        valorString = obj.getValorString();
                        id = obj.getId();
                    }
                    break;
                case TYPE_PARCELAMENTO:
                    if(parcelamentoObjs != null) {
                        ParcelamentoObj obj = parcelamentoObjs.get(position);
                        titulo = obj.getTitulo();
                        descricao = obj.getDescricao();
                        valorString = obj.getValorString();
                        id = obj.getId();
                    }
                    break;
            }
            holder.setId(id);
            holder.text.setText(titulo);
            holder.descricao.setText(descricao);
            holder.valor.setText(valorString);

            if(id == idSelected) {
                holder.btnSelected();
            } else {
                holder.btnDefault();
            }
        }

        @Override
        public int getItemCount() {
            int count = 0;
            switch (type) {
                case TYPE_GARANTIA:
                    if(garantiaObjs != null) count = garantiaObjs.size();
                    break;
                case TYPE_ENTREGA:
                    if(entregaObjs != null) count = entregaObjs.size();
                    break;
                case TYPE_PAGAMENTO:
                    if(pagamentosObjs != null) count = pagamentosObjs.size();
                    break;
                case TYPE_PARCELAMENTO:
                    if(parcelamentoObjs != null) count = parcelamentoObjs.size();
                    break;
            }
            return count;
        }

    }
}
