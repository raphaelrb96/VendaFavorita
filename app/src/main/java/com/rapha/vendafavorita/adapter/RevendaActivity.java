package com.rapha.vendafavorita.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.ComissoesActivity;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.TopProdutosRevenda;
import com.rapha.vendafavorita.objects.TopRevendedores;
import com.rapha.vendafavorita.vendedor.VendedorActivity;

import java.util.ArrayList;
import java.util.Collections;

public class RevendaActivity extends AppCompatActivity implements AdapterRevendas.RevendaListener, AdapterTopRevendedores.TopRevendedoresListener {

    private FirebaseFirestore firestore;
    private RecyclerView rv, rvTopProd, rvTopRevendedores;

    private ArrayList<ObjectRevenda> revendas;
    private ArrayList<TopProdutosRevenda> topProdutosRevendas;
    private ArrayList<TopRevendedores> topRevendedores;

    private AdapterRevendas adapter;
    private AdapterTopProdutos adapterTopProdutos;
    private AdapterTopRevendedores adapterTopRevendedores;

    private NestedScrollView scrol_revendas;

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenda);
        //central de revendas do adm
        rv = (RecyclerView) findViewById(R.id.rv_revenda);
        rvTopProd = (RecyclerView) findViewById(R.id.rv_top_produtos_revendas);
        rvTopRevendedores = (RecyclerView) findViewById(R.id.rv_top_revendedores);
        scrol_revendas = (NestedScrollView) findViewById(R.id.scrol_revendas);
        pb = (ProgressBar) findViewById(R.id.pb_revenda);
        pb.setVisibility(View.VISIBLE);
        scrol_revendas.setVisibility(View.GONE);

        firestore = FirebaseFirestore.getInstance();

        getRevendas();

    }


    private void getRevendas() {


        firestore.collection("Revendas").orderBy("hora", Query.Direction.DESCENDING).limit(150).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.size() == 0) {
                    return;
                }

                revendas = new ArrayList<>();
                topProdutosRevendas = new ArrayList<>();
                topRevendedores = new ArrayList<>();

                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                    ObjectRevenda rev = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);
                    revendas.add(rev);

                    /*

                    for (int j = 0; j < rev.getListaDeProdutos().size(); j++) {
                        ObjProdutoRevenda prodRev = rev.getListaDeProdutos().get(j);
                        TopProdutosRevenda topPrd = new TopProdutosRevenda(prodRev.getProdutoName(), prodRev.getCaminhoImg(), prodRev.getIdProdut(), prodRev.getQuantidade());

                        if (topProdutosRevendas.size() > 0) {

                            boolean produtoJaExiste = false;

                            for (int k = 0; k < topProdutosRevendas.size(); k++) {

                                if (topPrd.getIdProduto().equals(topProdutosRevendas.get(k).getIdProduto())) {
                                    produtoJaExiste = true;
                                    topProdutosRevendas.get(k).setNumeroDeRevendas(topPrd.getNumeroDeRevendas() + topProdutosRevendas.get(k).getNumeroDeRevendas());
                                    break;
                                }

                            }

                            if (!produtoJaExiste) {
                                topProdutosRevendas.add(topPrd);
                            }

                        } else {
                            topProdutosRevendas.add(topPrd);
                        }
                    }

                    TopRevendedores topRevn = new TopRevendedores(rev.getUserNomeRevendedor(), rev.getPathFotoUserRevenda(), rev.getUidUserRevendedor(), rev.getListaDeProdutos().size());

                    if (topRevendedores.size() > 0) {

                        boolean revendedorJaExiste = false;

                        for (int x = 0; x < topRevendedores.size(); x++) {
                            if (topRevendedores.get(x).getUidRevendedor().equals(topRevn.getUidRevendedor())) {
                                revendedorJaExiste = true;
                                topRevendedores.get(x).setNumeroItensReveendas(topRevendedores.get(x).getNumeroItensReveendas() + topRevn.getNumeroItensReveendas());
                                break;
                            }

                        }

                        if (!revendedorJaExiste) {
                            topRevendedores.add(topRevn);
                        }

                    } else {
                        topRevendedores.add(topRevn);
                    }

                     */

                }


                Collections.sort(topProdutosRevendas);
                Collections.sort(topRevendedores);



                rv.setLayoutManager(new LinearLayoutManager(RevendaActivity.this));
                //rvTopProd.setLayoutManager(new LinearLayoutManager(RevendaActivity.this, RecyclerView.HORIZONTAL, false));
                //rvTopRevendedores.setLayoutManager(new LinearLayoutManager(RevendaActivity.this, RecyclerView.HORIZONTAL, false));

                adapter = new AdapterRevendas(RevendaActivity.this, revendas, RevendaActivity.this);
                //adapterTopProdutos = new AdapterTopProdutos(topProdutosRevendas, RevendaActivity.this);
                //adapterTopRevendedores = new AdapterTopRevendedores(RevendaActivity.this, topRevendedores, RevendaActivity.this);


                rv.setAdapter(adapter);
                //rvTopRevendedores.setAdapter(adapterTopRevendedores);
                //rvTopProd.setAdapter(adapterTopProdutos);

                pb.setVisibility(View.GONE);
                scrol_revendas.setVisibility(View.VISIBLE);
            }
        });


    }


    private void updadeState (int state, ObjectRevenda obj) {
        WriteBatch batch = firestore.batch();


        DocumentReference rev = firestore.collection("Revendas").document(obj.getIdCompra());
        DocumentReference rev2 = firestore.collection("MinhasRevendas").document("Usuario").collection(obj.getUidUserRevendedor()).document(obj.getIdCompra());

        if (obj.isExisteComissaoAfiliados()) {

            DocumentReference comissaoRef = firestore.collection("ComissoesAfiliados").document(obj.getIdCompra());
            DocumentReference minhaComissaoRef = firestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(obj.getUidAdm()).document(obj.getIdCompra());

            batch.update(comissaoRef, "statusComissao", state);
            batch.update(minhaComissaoRef, "statusComissao", state);
        }

        batch.update(rev, "statusCompra", state);
        batch.update(rev2, "statusCompra", state);


        batch.commit();
    }

    @Override
    public void cancelar(ObjectRevenda obj) {

        updadeState(3, obj);

    }

    @Override
    public void confirmar(ObjectRevenda obj) {
        updadeState(2, obj);
    }

    @Override
    public void entregar(ObjectRevenda obj) {
        updadeState(4, obj);
    }

    @Override
    public void concluir(ObjectRevenda obj) {
        updadeState(5, obj);
    }

    @Override
    public void verRevended(String uidUserRevendedor, String userNomeRevendedor, String pathFotoUserRevenda) {
        Intent intent = new Intent(this, VendedorActivity.class);

        intent.putExtra("uid", uidUserRevendedor);

        startActivity(intent);
    }

    @Override
    public void verRevendedor(String uid, String path, String nome) {

        Intent intent = new Intent(this, VendedorActivity.class);

        intent.putExtra("uid", uid);

        startActivity(intent);


    }

    private void abrirComissoes(String uid, String path, String nome) {
        Intent intent = new Intent(this, ComissoesActivity.class);

        intent.putExtra("id", uid);
        intent.putExtra("nome", nome);
        intent.putExtra("path", path);
        intent.putExtra("zap", "");
        intent.putExtra("time", 0);

        startActivity(intent);
    }


    private ArrayList<TopRevendedores> ordenarRevendedores (ArrayList<TopRevendedores> topRevendedores) {

        Collections.sort(topRevendedores);

        return topRevendedores;

    }

}
