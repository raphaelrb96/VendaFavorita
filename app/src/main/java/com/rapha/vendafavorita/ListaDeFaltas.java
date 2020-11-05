package com.rapha.vendafavorita;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.objects.Falta;

import java.util.ArrayList;

public class ListaDeFaltas extends AppCompatActivity {

    private ExtendedFloatingActionButton bt_add_item_falta;
    private RecyclerView lista_indisponiveis, lista_indisponiveis_manual;
    private ProgressBar pb;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CollectionReference refProdutos;
    private CollectionReference refListaFaltas;

    private QuerySnapshot mQueryDocumentSnapshots;

    private ArrayList<Falta> faltaArrayList;
    private ArrayList<ProdObj> prodObjArrayList;

    private TextView title_priopridade_lista_de_faltas;
    private NestedScrollView nestedscrollview_lista_falta;

    private FaltaAdapter adapterManual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_faltas);
        bt_add_item_falta = (ExtendedFloatingActionButton) findViewById(R.id.bt_add_item_falta);
        lista_indisponiveis =(RecyclerView) findViewById(R.id.lista_indisponiveis);
        title_priopridade_lista_de_faltas = (TextView) findViewById(R.id.title_priopridade_lista_de_faltas);
        nestedscrollview_lista_falta = (NestedScrollView) findViewById(R.id.nestedscrollview_lista_falta);
        lista_indisponiveis_manual =(RecyclerView) findViewById(R.id.lista_indisponiveis_manual);
        pb = (ProgressBar) findViewById(R.id.pb_lista_falta);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        refProdutos = firestore.collection("produtos");
        refListaFaltas = firestore.collection("faltas");

        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                String idDocumento = mQueryDocumentSnapshots.getDocuments().get(viewHolder.getAdapterPosition()).getId();

                faltaArrayList.remove(viewHolder.getAdapterPosition());

                refListaFaltas.document(idDocumento).delete();

                adapterManual.notifyDataSetChanged();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(lista_indisponiveis_manual);

        refListaFaltas.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.size() > 0) {

                        mQueryDocumentSnapshots = queryDocumentSnapshots;

                        faltaArrayList = new ArrayList<>();

                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            Falta falta = queryDocumentSnapshots.getDocuments().get(i).toObject(Falta.class);
                            faltaArrayList.add(falta);
                        }

                        adapterManual = new FaltaAdapter(faltaArrayList, ListaDeFaltas.this);
                        lista_indisponiveis_manual.setLayoutManager(new LinearLayoutManager(ListaDeFaltas.this));
                        lista_indisponiveis_manual.setAdapter(adapterManual);

                        getProdutosIndisponies();

                    } else {
                        lista_indisponiveis_manual.setVisibility(View.GONE);
                        title_priopridade_lista_de_faltas.setVisibility(View.GONE);
                        getProdutosIndisponies();
                    }
                }
            }
        });

        bt_add_item_falta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogo();
            }
        });

    }

    private void getProdutosIndisponies() {

        refProdutos.whereEqualTo("disponivel", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots != null) {

                    nestedscrollview_lista_falta.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);

                    if (queryDocumentSnapshots.size() > 0) {


                        title_priopridade_lista_de_faltas.setVisibility(View.VISIBLE);
                        lista_indisponiveis.setVisibility(View.VISIBLE);
                        prodObjArrayList = new ArrayList<>();
                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            ProdObj falta = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                            prodObjArrayList.add(falta);
                        }

                        ProdutosIndisponiveis faltaAdapter = new ProdutosIndisponiveis(prodObjArrayList, ListaDeFaltas.this);
                        lista_indisponiveis.setLayoutManager(new LinearLayoutManager(ListaDeFaltas.this));
                        lista_indisponiveis.setAdapter(faltaAdapter);


                    } else {

                        title_priopridade_lista_de_faltas.setVisibility(View.VISIBLE);
                        title_priopridade_lista_de_faltas.setText("Lista vazia");

                    }
                }
            }
        });

    }

    private void exibirDialogo() {
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Falta")
                .setMessage("Insira o nome do produto")
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String produtoFalta = input.getText().toString();
                        adicionarAFaltas(produtoFalta);
                    }
                });
        dialog.setView(input);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorSecondaryDark));
    }

    private void adicionarAFaltas(String produtoF) {

        Falta novaFalta = new Falta(produtoF, System.currentTimeMillis());

        refListaFaltas.document().set(novaFalta);

        faltaArrayList.add(novaFalta);
    }



    class ProdutosIndisponiveis extends RecyclerView.Adapter<FaltaViewHolder> {

        private ArrayList<ProdObj> indisponiveis;
        private Context context;

        public ProdutosIndisponiveis(ArrayList<ProdObj> ind, Context context) {
            this.indisponiveis = ind;
            this.context = context;
        }

        @NonNull
        @Override
        public FaltaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_lista_de_faltas, parent, false);
            return new FaltaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FaltaViewHolder holder, int position) {
            holder.textView.setText(indisponiveis.get(position).getProdName());
            Glide.with(context).load(indisponiveis.get(position).getImgCapa()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return indisponiveis.size();
        }
    }

    class FaltaAdapter extends RecyclerView.Adapter<FaltaViewHolder> {

        private ArrayList<Falta> faltas;
        private Context context;

        public FaltaAdapter(ArrayList<Falta> faltas, Context context) {
            this.faltas = faltas;
            this.context = context;
        }

        @NonNull
        @Override
        public FaltaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_lista_de_faltas, parent, false);
            FaltaViewHolder faltaViewHolder = new FaltaViewHolder(view);
            faltaViewHolder.imageView.setVisibility(View.GONE);
            return faltaViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FaltaViewHolder holder, int position) {
            holder.textView.setText(faltas.get(position).getProd());
        }

        @Override
        public int getItemCount() {
            return faltas.size();
        }
    }

    class FaltaViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public FaltaViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.nome_prod_faltas);
            imageView = (ImageView) itemView.findViewById(R.id.img_prod_falta);
        }
    }

}
