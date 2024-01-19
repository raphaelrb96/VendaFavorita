package com.rapha.vendafavorita;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterMeuHistoricoDeVendas;
import com.rapha.vendafavorita.adapter.AdapterTopProdutos;
import com.rapha.vendafavorita.adapter.ComissoesAfiliadosAdapter;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.TopProdutosRevenda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;

public class HistoricoRevendasActivity extends AppCompatActivity implements AdapterMeuHistoricoDeVendas.OnChangeListVendasListener {

    private RecyclerView rv_comissoes, rv_comissoes_afiliados, rv_comissoes_top_produtos;
    private ProgressBar pb_comissoes;
    private TextView text_erro_comissao;
    private AdapterMeuHistoricoDeVendas adapterMeuHistoricoDeVendas;
    private NestedScrollView nestedscroll_historico_vendas;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    private LinearLayout scrol_list_minhas_comissoes;

    private String idUsuario, nome, path, zap;

    private int total = 0;
    private long time;
    private Query refRevenda, refComissoes;
    private ArrayList<ObjectRevenda> listaDeComissoes;
    private ArrayList<ComissaoAfiliados> listaDeComissaoAfiliados;

    private TextView titulo_comissoes_afiliados, titulo_comissoes_revendas, titulo_top_produtos;
    private View bt_voltar_historico_revendas;
    private ImageView img_historico_revendas;
    private AnalitycsGoogle analitycsGoogle;

    private AdapterTopProdutos adapterTopProdutos;

    private ArrayList<TopProdutosRevenda> topProdutosRevendas30;
    private GregorianCalendar c30;
    private LinearLayoutManager vendasLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_revendas);
        //activity do cliente
        rv_comissoes = (RecyclerView) findViewById(R.id.rv_comissoes);
        rv_comissoes_afiliados = (RecyclerView) findViewById(R.id.rv_comissoes_afiliados);
        rv_comissoes_top_produtos = (RecyclerView) findViewById(R.id.rv_comissoes_top_produtos);
        nestedscroll_historico_vendas = (NestedScrollView) findViewById(R.id.nestedscroll_historico_vendas);

        text_erro_comissao = (TextView) findViewById(R.id.text_erro_comissao);
        titulo_comissoes_afiliados = (TextView) findViewById(R.id.titulo_comissoes_afiliados);
        titulo_comissoes_revendas = (TextView) findViewById(R.id.titulo_comissoes_revendas);
        titulo_top_produtos = (TextView) findViewById(R.id.titulo_top_produtos);

        bt_voltar_historico_revendas = (View) findViewById(R.id.bt_voltar_historico_revendas);
        img_historico_revendas = (ImageView) findViewById(R.id.img_historico_revendas);
        pb_comissoes = (ProgressBar) findViewById(R.id.pb_comissoes);
        scrol_list_minhas_comissoes = (LinearLayout) findViewById(R.id.scrol_list_minhas_comissoes);

        firebaseFirestore = FirebaseFirestore.getInstance();

        analitycsGoogle = new AnalitycsGoogle(this);
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

        String uid = auth.getUid();
        //vision
        //uid = "iRQUihKXA3VXFdDH6kL03qVmwDW2";
        //lucas
        //uid = "Vts6WhKZpmhcCbOeotEOSzJSVe23";

        c30 = new GregorianCalendar();
        c30.add(Calendar.DAY_OF_MONTH, -30);
        long milisg = c30.getTimeInMillis();

        refRevenda = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(uid);

        refComissoes = firebaseFirestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(uid);

        //scrol_list_minhas_comissoes.setVisibility(View.GONE);
        refRevenda.orderBy("hora", Query.Direction.DESCENDING).limit(150).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listaDeComissoes = new ArrayList<>();
                topProdutosRevendas30 = new ArrayList<>();
                total = 0;

                if (queryDocumentSnapshots != null) {

                    if (queryDocumentSnapshots.getDocuments().size() > 0) {



                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                            ObjectRevenda obj = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);

                            if (obj.getStatusCompra() == 5) {
                                //listaDeComissoes.add(obj);
                                if (!obj.isPagamentoRecebido()) {
                                    total = total + obj.getComissaoTotal();
                                }
                            }

                            if (obj.getStatusCompra() != 3) {
                                preencherListaTopProdutos(obj);
                            }

                            listaDeComissoes.add(obj);

                        }





                        Collections.sort(topProdutosRevendas30);



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

            analitycsGoogle.visitaAoPainelRevendedor(nome, idUsuario, path);

        }
    }

    private void getListComissoes() {

        refComissoes.orderBy("hora", Query.Direction.DESCENDING).limit(150).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        titulo_top_produtos.setVisibility(View.GONE);
    }

    private void comComissoesRevendas() {

        adapterMeuHistoricoDeVendas = new AdapterMeuHistoricoDeVendas(HistoricoRevendasActivity.this, listaDeComissoes, HistoricoRevendasActivity.this);
        vendasLayoutManager = new LinearLayoutManager(HistoricoRevendasActivity.this);
        rv_comissoes.setLayoutManager(vendasLayoutManager);
        rv_comissoes.setAdapter(adapterMeuHistoricoDeVendas);

        if (topProdutosRevendas30.size() > 0) {

            titulo_top_produtos.setVisibility(View.VISIBLE);
            adapterTopProdutos = new AdapterTopProdutos(topProdutosRevendas30, HistoricoRevendasActivity.this);
            rv_comissoes_top_produtos.setLayoutManager(new LinearLayoutManager(HistoricoRevendasActivity.this, RecyclerView.HORIZONTAL, false));
            rv_comissoes_top_produtos.setAdapter(adapterTopProdutos);

        }

        rv_comissoes.setVisibility(View.VISIBLE);
        rv_comissoes_top_produtos.setVisibility(View.VISIBLE);

        titulo_comissoes_revendas.setVisibility(View.VISIBLE);

        pb_comissoes.setVisibility(View.GONE);

    }

    private void listaCheia() {
        scrol_list_minhas_comissoes.setVisibility(View.VISIBLE);
        text_erro_comissao.setVisibility(View.GONE);
        pb_comissoes.setVisibility(View.GONE);
    }

    private void listaVazia() {
        rv_comissoes.setVisibility(View.GONE);
        rv_comissoes_top_produtos.setVisibility(View.GONE);
        text_erro_comissao.setVisibility(View.VISIBLE);
        pb_comissoes.setVisibility(View.GONE);
    }


    private void preencherListaTopProdutos(ObjectRevenda rev) {

        for (int j = 0; j < rev.getListaDeProdutos().size(); j++) {
            ObjProdutoRevenda prodRev = rev.getListaDeProdutos().get(j);
            TopProdutosRevenda topPrd = new TopProdutosRevenda(prodRev.getProdutoName(), prodRev.getCaminhoImg(), prodRev.getIdProdut(), prodRev.getQuantidade());

            if (topProdutosRevendas30.size() > 0) {

                boolean produtoJaExiste = false;

                for (int k = 0; k < topProdutosRevendas30.size(); k++) {

                    if (topPrd.getIdProduto().equals(topProdutosRevendas30.get(k).getIdProduto())) {
                        produtoJaExiste = true;
                        topProdutosRevendas30.get(k).setNumeroDeRevendas(topPrd.getNumeroDeRevendas() + topProdutosRevendas30.get(k).getNumeroDeRevendas());
                        break;
                    }

                }

                if (!produtoJaExiste) {
                    topProdutosRevendas30.add(topPrd);
                }

            } else {
                topProdutosRevendas30.add(topPrd);
            }
        }


    }


    @Override
    public void onChangeList(int pagina) {
        //nestedscroll_historico_vendas.smoothScrollTo(0, (int)0, 3000);
        titulo_top_produtos.setVisibility(View.GONE);
        titulo_comissoes_afiliados.setVisibility(View.GONE);
        img_historico_revendas.setVisibility(View.GONE);
        rv_comissoes_afiliados.setVisibility(View.GONE);
        rv_comissoes_top_produtos.setVisibility(View.GONE);
        titulo_comissoes_revendas.setVisibility(View.VISIBLE);
        titulo_comissoes_revendas.setText("Meu historico  /  Parte " + pagina);
        nestedscroll_historico_vendas.smoothScrollTo(0, 0, 3500);
    }
}