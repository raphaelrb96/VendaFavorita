package com.rapha.vendafavorita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterCentralCompras;
import com.rapha.vendafavorita.objects.CarComprasActivyParcelable;
import com.rapha.vendafavorita.objects.CompraFinalizada;
import com.rapha.vendafavorita.objects.CompraFinalizadaParcelable;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class CentralComprasActivity extends AppCompatActivity implements AdapterCentralCompras.ClickCentralCompra {

    private FirebaseFirestore firestore;
    private ProgressBar pb;
    private LinearLayout containerErro;
    private RecyclerView rv;
    private View bt_voltar_central_compras;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winPar = win.getAttributes();
        if (on) {
            winPar.flags |= bits;
        } else {
            winPar.flags &= ~bits;
        }
        win.setAttributes(winPar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_compras);

        rv = (RecyclerView) findViewById(R.id.rv_minhas_compras);
        pb = (ProgressBar) findViewById(R.id.pb_minhas_compras);
        containerErro = (LinearLayout) findViewById(R.id.ll_container_erro_minhas_compras);
        bt_voltar_central_compras = (View) findViewById(R.id.bt_voltar_central_compras);
        firestore = FirebaseFirestore.getInstance();

        bt_voltar_central_compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        firestore.collection("Compras").orderBy("hora", Query.Direction.DESCENDING).limit(60).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.getDocuments().size() < 1) {
                        pb.setVisibility(View.GONE);
                        containerErro.setVisibility(View.VISIBLE);
                    } else {
                        ArrayList<CompraFinalizada> compraFinalizadas = new ArrayList<>();
                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                            CompraFinalizada cf = queryDocumentSnapshots.getDocuments().get(i).toObject(CompraFinalizada.class);
                            compraFinalizadas.add(cf);
                        }

                        AdapterCentralCompras adapter = new AdapterCentralCompras(CentralComprasActivity.this, compraFinalizadas, CentralComprasActivity.this);
                        rv.setLayoutManager(new LinearLayoutManager(CentralComprasActivity.this));
                        rv.setAdapter(adapter);
                        pb.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }
                } else {
                    rv.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    containerErro.setVisibility(View.VISIBLE);
                }
            }
        });

    }



    @Override
    public void detalhesCompra(CompraFinalizada compra) {
        ArrayList<CarComprasActivyParcelable> carComprasActivyParcelableArrayList = new ArrayList<>();
        for (int i = 0; i < compra.getListaDeProdutos().size(); i++) {
            CarComprasActivyParcelable p = new CarComprasActivyParcelable(compra.getListaDeProdutos().get(i));
            carComprasActivyParcelableArrayList.add(p);
        }

        CompraFinalizadaParcelable compraFinalizadaParcelable = new CompraFinalizadaParcelable(compra);
        Intent intent = new Intent(this, VendaActivity.class);
        intent.putParcelableArrayListExtra("itens", carComprasActivyParcelableArrayList);
        intent.putExtra("compra", compraFinalizadaParcelable);
        startActivity(intent);
    }
}
