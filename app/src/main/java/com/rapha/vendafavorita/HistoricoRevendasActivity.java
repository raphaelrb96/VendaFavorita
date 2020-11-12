package com.rapha.vendafavorita;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterMeuHistoricoDeVendas;
import com.rapha.vendafavorita.adapter.ComissoesAfiliadosAdapter;
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;

public class HistoricoRevendasActivity extends AppCompatActivity {

    private RecyclerView rv_comissoes, rv_comissoes_afiliados;
    private ProgressBar pb_comissoes;
    private TextView text_erro_comissao, valor_a_receber;
    private AdapterMeuHistoricoDeVendas adapterMeuHistoricoDeVendas;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    private NestedScrollView scrol_list_minhas_comissoes;

    private String idUsuario, nome, path, zap;
    private int total;
    private long time;
    private Query refRevenda, refComissoes;
    private ArrayList<ObjectRevenda> listaDeComissoes;
    private ArrayList<ComissaoAfiliados> listaDeComissaoAfiliados;

    private CardView card_carteira_historico_comissoes;
    private TextView titulo_comissoes_afiliados, titulo_comissoes_revendas;
    private View bt_voltar_historico_revendas;
    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsFacebook analitycsGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_revendas);
        //activity do cliente
        rv_comissoes = (RecyclerView) findViewById(R.id.rv_comissoes);
        rv_comissoes_afiliados = (RecyclerView) findViewById(R.id.rv_comissoes_afiliados);
        card_carteira_historico_comissoes = (CardView) findViewById(R.id.card_carteira_historico_comissoes);
        text_erro_comissao = (TextView) findViewById(R.id.text_erro_comissao);
        titulo_comissoes_afiliados = (TextView) findViewById(R.id.titulo_comissoes_afiliados);
        titulo_comissoes_revendas = (TextView) findViewById(R.id.titulo_comissoes_revendas);
        valor_a_receber = (TextView) findViewById(R.id.valor_a_receber);
        bt_voltar_historico_revendas = (View) findViewById(R.id.bt_voltar_historico_revendas);
        pb_comissoes = (ProgressBar) findViewById(R.id.pb_comissoes);
        scrol_list_minhas_comissoes = (NestedScrollView) findViewById(R.id.scrol_list_minhas_comissoes);

        firebaseFirestore = FirebaseFirestore.getInstance();

        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsFacebook(this);
        auth = FirebaseAuth.getInstance();

        idUsuario = getIntent().getStringExtra("id");
        nome = getIntent().getStringExtra("nome");
        path = getIntent().getStringExtra("path");
        zap = getIntent().getStringExtra("zap");

        bt_voltar_historico_revendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String uid = documentoPrincipalDoUsuario.getUid();

        if (uid == null) {
            uid = auth.getUid();
        }

        refRevenda = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(uid);

        refComissoes = firebaseFirestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(uid);

        scrol_list_minhas_comissoes.setVisibility(View.GONE);
        refRevenda.orderBy("hora", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listaDeComissoes = new ArrayList<>();
                if (queryDocumentSnapshots != null) {

                    if (queryDocumentSnapshots.getDocuments().size() > 0) {

                        total = 0;

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                            ObjectRevenda obj = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);

                            if (obj.getStatusCompra() == 5) {
                                //listaDeComissoes.add(obj);
                                if (!obj.isPagamentoRecebido()) {
                                    total = total + obj.getComissaoTotal();
                                }
                            }

                            listaDeComissoes.add(obj);

                        }

                        adapterMeuHistoricoDeVendas = new AdapterMeuHistoricoDeVendas(HistoricoRevendasActivity.this, listaDeComissoes);
                        rv_comissoes.setLayoutManager(new LinearLayoutManager(HistoricoRevendasActivity.this));
                        rv_comissoes.setAdapter(adapterMeuHistoricoDeVendas);



                        getListComissoes();


                    } else {
                        getListComissoes();
                    }
                } else {
                    getListComissoes();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!ADMINISTRADOR) {

            analitycsFacebook.carteiraView(user.getDisplayName(), user.getUid(), pathFotoUser);
            analitycsGoogle.carteiraView(user.getDisplayName(), user.getUid(), pathFotoUser);

        }
    }

    private void getListComissoes() {

        refComissoes.orderBy("hora", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                listaDeComissaoAfiliados = new ArrayList<>();

                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {



                    for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                        ComissaoAfiliados comissao = queryDocumentSnapshots.getDocuments().get(i).toObject(ComissaoAfiliados.class);

                        int comiss = comissao.getComissao();

                        if (comissao.getStatusComissao() == 5) {
                            //listaDeComissoes.add(obj);
                            if (!comissao.isPagamentoRecebido()) {
                                total = total + comiss;
                            }
                        }

                        listaDeComissaoAfiliados.add(comissao);

                    }

                    card_carteira_historico_comissoes.setVisibility(View.VISIBLE);
                    valor_a_receber.setText(total + ",00");

                    if (listaDeComissoes.size() > 0) {
                        comComissoesAfiliados();
                        comComissoesRevendas();
                    } else {
                        comComissoesAfiliados();
                        semComissoesRevendas();
                        text_erro_comissao.setVisibility(View.VISIBLE);
                        text_erro_comissao.setText("Você ainda não possui nenhuma comissão de venda");
                    }

                } else {

                    if (listaDeComissoes.size() > 0) {

                        //sem comissoes de afiliados mas possui revendas
                        semComissoesAfiliados();
                        comComissoesRevendas();

                    } else {
                        //sem comissoes de afiliados e sem revendas
                        semComissoesRevendas();
                        semComissoesAfiliados();
                        text_erro_comissao.setVisibility(View.VISIBLE);
                        text_erro_comissao.setText("Você ainda não possui nenhuma comissão de venda e nem de afiliados");
                    }
                }

                pb_comissoes.setVisibility(View.GONE);
                scrol_list_minhas_comissoes.setVisibility(View.VISIBLE);

            }
        });

    }

    private void semComissoesAfiliados() {
        titulo_comissoes_afiliados.setVisibility(View.GONE);
        rv_comissoes_afiliados.setVisibility(View.GONE);
    }

    private void comComissoesAfiliados() {

        ComissoesAfiliadosAdapter adapter = new ComissoesAfiliadosAdapter(this, listaDeComissaoAfiliados);
        rv_comissoes_afiliados.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv_comissoes_afiliados.setAdapter(adapter);

        titulo_comissoes_afiliados.setVisibility(View.VISIBLE);
        rv_comissoes_afiliados.setVisibility(View.VISIBLE);
    }

    private void semComissoesRevendas() {
        rv_comissoes.setVisibility(View.GONE);
        titulo_comissoes_revendas.setVisibility(View.GONE);
    }

    private void comComissoesRevendas() {
        adapterMeuHistoricoDeVendas = new AdapterMeuHistoricoDeVendas(HistoricoRevendasActivity.this, listaDeComissoes);
        rv_comissoes.setLayoutManager(new LinearLayoutManager(HistoricoRevendasActivity.this));
        rv_comissoes.setAdapter(adapterMeuHistoricoDeVendas);
        rv_comissoes.setVisibility(View.VISIBLE);
        titulo_comissoes_revendas.setVisibility(View.VISIBLE);
    }

    private void listaCheia() {
        scrol_list_minhas_comissoes.setVisibility(View.VISIBLE);
        text_erro_comissao.setVisibility(View.GONE);
        pb_comissoes.setVisibility(View.GONE);
    }

    private void listaVazia() {
        rv_comissoes.setVisibility(View.GONE);
        text_erro_comissao.setVisibility(View.VISIBLE);
        pb_comissoes.setVisibility(View.GONE);
    }

}