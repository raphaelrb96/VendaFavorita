package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterProdutoAdmAnalytics;
import com.rapha.vendafavorita.adapter.AdapterTermosAdm;
import com.rapha.vendafavorita.adapter.AdapterUsuariosStreamView;
import com.rapha.vendafavorita.objects.ProdutoAnalitycs;
import com.rapha.vendafavorita.objects.ProdutoMaisProdutoAnalyticsFusao;
import com.rapha.vendafavorita.objects.TermosDePesquisa;
import com.rapha.vendafavorita.objects.UserStreamView;
import com.rapha.vendafavorita.objects.Usuario;
import com.rapha.vendafavorita.objects.UsuarioParcelable;

import java.util.ArrayList;


public class AnalyticsAdmActivity extends AppCompatActivity implements AdapterUsuariosStreamView.StremViewListener {

    private FirebaseFirestore firebaseFirestore;
    private AdapterProdutoAdmAnalytics adapterProdutoAdmAnalytics;
    private AdapterTermosAdm adapterTermosAdm;
    private AdapterUsuariosStreamView adapterUsuariosStreamView;
    private RecyclerView rvClientes, rvProdutos, rvTermos;
    private ProgressBar pb;
    private LinearLayout containerCliente, containerProdutos, containerTermos;
    private ArrayList<UserStreamView> userList;
    private ArrayList<ProdutoMaisProdutoAnalyticsFusao> produtoList;
    private ArrayList<TermosDePesquisa> termoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_adm);
        pb = (ProgressBar) findViewById(R.id.pb_analytics);
        containerCliente = (LinearLayout) findViewById(R.id.ll_container_usuarios_analytics_adm);
        containerProdutos = (LinearLayout) findViewById(R.id.ll_container_produtos_analytics_adm);
        containerTermos = (LinearLayout) findViewById(R.id.ll_container_termos_analytics_adm);

        rvClientes = (RecyclerView) findViewById(R.id.rv_usuario_adm_analytics);
        rvProdutos = (RecyclerView) findViewById(R.id.rv_produtos_adm_analytics);
        rvTermos = (RecyclerView) findViewById(R.id.rv_termos_adm_analytics);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("UsuarioStreamView").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.size() > 0) {

                        userList = new ArrayList<>();

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                            UserStreamView user = queryDocumentSnapshots.getDocuments().get(i).toObject(UserStreamView.class);
                            userList.add(user);
                        }

                        if (userList.size() > 0) {

                            rvClientes.setLayoutManager(new LinearLayoutManager(AnalyticsAdmActivity.this, RecyclerView.HORIZONTAL, false));
                            adapterUsuariosStreamView = new AdapterUsuariosStreamView(AnalyticsAdmActivity.this, userList, AnalyticsAdmActivity.this);
                            rvClientes.setAdapter(adapterUsuariosStreamView);
                            containerCliente.setVisibility(View.VISIBLE);

                        }

                    }
                }

                firebaseFirestore.collection("ProdutosAnalytics").orderBy("numeroAddCart", Query.Direction.DESCENDING).get().addOnSuccessListener(AnalyticsAdmActivity.this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots2) {


                        firebaseFirestore.collection("produtos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot prodResult) {
                                if (prodResult != null && queryDocumentSnapshots2 != null) {

                                    produtoList = new ArrayList<>();

                                    for (int j = 0; j < queryDocumentSnapshots2.getDocuments().size(); j++) {

                                        String id = queryDocumentSnapshots2.getDocuments().get(j).getId();
                                        ProdutoAnalitycs produtoAnalitycs = queryDocumentSnapshots2.getDocuments().get(j).toObject(ProdutoAnalitycs.class);

                                        for (int i = 0; i < prodResult.getDocuments().size(); i++) {
                                            ProdObj prodObj = prodResult.getDocuments().get(i).toObject(ProdObj.class);

                                            if (id.equals(prodObj.getIdProduto())) {
                                                ProdutoMaisProdutoAnalyticsFusao fusao = new ProdutoMaisProdutoAnalyticsFusao(prodObj, produtoAnalitycs);
                                                produtoList.add(fusao);
                                                i = prodResult.getDocuments().size();
                                            }
                                        }

                                    }


                                    if (produtoList.size() > 0) {

                                        rvProdutos.setLayoutManager(new LinearLayoutManager(AnalyticsAdmActivity.this, RecyclerView.HORIZONTAL, false));
                                        adapterProdutoAdmAnalytics = new AdapterProdutoAdmAnalytics(AnalyticsAdmActivity.this, produtoList);
                                        rvProdutos.setAdapter(adapterProdutoAdmAnalytics);
                                        containerProdutos.setVisibility(View.VISIBLE);

                                    }

                                }


                                firebaseFirestore.collection("termosDePesquisa").orderBy("ultimaPesquisa", Query.Direction.DESCENDING).get().addOnSuccessListener(AnalyticsAdmActivity.this, new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots3) {
                                        pb.setVisibility(View.GONE);
                                        if (queryDocumentSnapshots3 != null) {
                                            termoList = new ArrayList<>();

                                            for (int i = 0; i < queryDocumentSnapshots3.size(); i++) {

                                                TermosDePesquisa termosDePesquisa = queryDocumentSnapshots3.getDocuments().get(i).toObject(TermosDePesquisa.class);
                                                termoList.add(termosDePesquisa);

                                            }

                                            if (termoList.size() > 0) {
                                                rvTermos.setLayoutManager(new LinearLayoutManager(AnalyticsAdmActivity.this));
                                                adapterTermosAdm = new AdapterTermosAdm(AnalyticsAdmActivity.this, termoList);
                                                rvTermos.setAdapter(adapterTermosAdm);
                                                containerTermos.setVisibility(View.VISIBLE);
                                            }

                                        }
                                    }
                                });


                            }
                        });


                    }
                });
            }
        });
    }

    @Override
    public void analizerUsuario(String uid) {
        pb.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Aguarde", Toast.LENGTH_LONG).show();
        firebaseFirestore.collection("Usuario").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                UsuarioParcelable usuarioParcelable = new UsuarioParcelable(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm());
                Intent intent = new Intent(AnalyticsAdmActivity.this, ClienteDetalhesActivity.class);
                intent.putExtra("user", usuarioParcelable);
                pb.setVisibility(View.GONE);
                startActivity(intent);
            }
        });
    }
}
