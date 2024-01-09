package com.rapha.vendafavorita.informacoes;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objectfeed.ProdutoObj;
import com.rapha.vendafavorita.objectfeed.RevendedorObj;
import com.rapha.vendafavorita.objects.TopRevendedores;
import com.rapha.vendafavorita.informacoes.ui.main.SectionsPagerAdapter;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.TopProdutosRevenda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Nullable;

public class AnalisarDadosActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabs;
    private FirebaseFirestore firestore;
    private ProgressBar pb;
    private ExtendedFloatingActionButton efab_analytics_atualizar_home;

    private Toast mToats;

    private ArrayList<ObjectRevenda> revendas30;
    private ArrayList<ObjectRevenda> revendas7;
    private ArrayList<ObjectRevenda> revendas1;
    private ArrayList<ObjectRevenda> revendasHoje;

    private Query query;
    private GregorianCalendar c7;
    private GregorianCalendar c1;

    private ArrayList<TopProdutosRevenda> topProdutosRevendas30;
    private ArrayList<TopProdutosRevenda> topProdutosRevendas7;
    private ArrayList<TopProdutosRevenda> topProdutosRevendas1;
    private ArrayList<TopProdutosRevenda> topProdutosRevendasHoje;

    private ArrayList<TopRevendedores> topRevendedores30;
    private ArrayList<TopRevendedores> topRevendedores7;
    private ArrayList<TopRevendedores> topRevendedores1;
    private ArrayList<TopRevendedores> topRevendedoresHoje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisar_dados);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pb = (ProgressBar) findViewById(R.id.pb_analise);
        efab_analytics_atualizar_home = (ExtendedFloatingActionButton) findViewById(R.id.efab_analytics_atualizar_home);

        firestore = FirebaseFirestore.getInstance();

        Calendar c = new GregorianCalendar();
        c7 = new GregorianCalendar();
        c1 = new GregorianCalendar();
        c.add(Calendar.DAY_OF_MONTH, -30);
        c7.add(Calendar.DAY_OF_MONTH, -7);
        c1.add(Calendar.HOUR_OF_DAY, -24);

        long time30 = c.getTimeInMillis();

        query = firestore.collection("Revendas").whereGreaterThan("hora", time30).orderBy("hora", Query.Direction.DESCENDING);

        viewPager.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        efab_analytics_atualizar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToats != null) {

                    mToats.cancel();

                }

                mToats.makeText(AnalisarDadosActivity.this, "Atualizando Feed...", Toast.LENGTH_LONG).show();

                efab_analytics_atualizar_home.setVisibility(View.GONE);

                ArrayList<ProdutoObj> topsProd = new ArrayList<>();
                ArrayList<ProdutoObj> topsProdMaisVendidos = new ArrayList<>();
                ArrayList<RevendedorObj> topsRev = new ArrayList<>();

                for (int i = 0; i < topProdutosRevendas1.size(); i++) {
                    TopProdutosRevenda ob = topProdutosRevendas1.get(i);
                    ProdutoObj produtoObj = new ProdutoObj(ob.getNomeProduto(), ob.getPathProduto(), ob.getIdProduto());
                    topsProd.add(produtoObj);
                    if (i > 19) break;
                }

                for (int i = 0; i < topProdutosRevendas7.size(); i++) {
                    TopProdutosRevenda ob = topProdutosRevendas7.get(i);
                    ProdutoObj produtoObj = new ProdutoObj(ob.getNomeProduto(), ob.getPathProduto(), ob.getIdProduto());
                    topsProdMaisVendidos.add(produtoObj);
                    if (i > 19) break;
                }

                for (int j = 0; j < topRevendedores7.size(); j++) {
                    TopRevendedores rev = topRevendedores7.get(j);
                    RevendedorObj rObj = new RevendedorObj(rev.getNomeRevendedor(), rev.getPathFotoRevendedor(), rev.getUidRevendedor(), rev.getNumeroItensReveendas());
                    topsRev.add(rObj);
                    if (j > 5) break;
                }



                firestore.collection("Feed").document("Main").update("topRevendedores", topsRev, "topProdutos", topsProd, "timeStamp", System.currentTimeMillis(), "topMaisVendidos", topsProdMaisVendidos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        efab_analytics_atualizar_home.setVisibility(View.VISIBLE);
                        if (mToats != null) {

                            mToats.cancel();

                        }
                        mToats.makeText(AnalisarDadosActivity.this, "Feed atualizado", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        efab_analytics_atualizar_home.setVisibility(View.VISIBLE);
                        if (mToats != null) {

                            mToats.cancel();

                        }
                        mToats.makeText(AnalisarDadosActivity.this, "Falha ao atualizar Feed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                viewPager.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);


                if (queryDocumentSnapshots == null) {
                    return;
                }

                if (queryDocumentSnapshots.size() == 0) {
                    return;
                }

                revendas30 = new ArrayList<>();
                revendas7 = new ArrayList<>();
                revendas1 = new ArrayList<>();
                revendasHoje = new ArrayList<>();

                topProdutosRevendas1 = new ArrayList<>();
                topProdutosRevendas7 = new ArrayList<>();
                topProdutosRevendas30 = new ArrayList<>();
                topProdutosRevendasHoje = new ArrayList<>();

                topRevendedores30 = new ArrayList<>();
                topRevendedores7 = new ArrayList<>();
                topRevendedores1 = new ArrayList<>();
                topRevendedoresHoje = new ArrayList<>();

                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                    ObjectRevenda rev = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);
                    revendas30.add(rev);

                    if (rev.getHora() > c7.getTimeInMillis()) {
                        revendas7.add(rev);
                    }

                    if (rev.getHora() > c1.getTimeInMillis()) {
                        revendas1.add(rev);
                    }

                    String dataRev = DateFormatacao.getDiaStringSmall(new Date(rev.getHora()));
                    String dataAgora = DateFormatacao.getDiaStringSmall(new Date(System.currentTimeMillis()));

                    if (dataAgora.equals(dataRev)) {
                        revendasHoje.add(rev);
                    }

                }

                for (int i = 0; i < revendas30.size(); i++) {

                    ObjectRevenda rev = revendas30.get(i);

                    if (rev.getStatusCompra() != 3) {


                        preencherLista30(rev);

                    }
                }

                for (int i = 0; i < revendas7.size(); i++) {

                    ObjectRevenda rev = revendas7.get(i);

                    if (rev.getStatusCompra() != 3) {


                        preencherLista7(rev);

                    }
                }

                for (int i = 0; i < revendas1.size(); i++) {

                    ObjectRevenda rev = revendas1.get(i);

                    if (rev.getStatusCompra() != 3) {


                        preencherLista1(rev);

                    }
                }


                for (int i = 0; i < revendasHoje.size(); i++) {

                    ObjectRevenda rev = revendasHoje.get(i);

                    if (rev.getStatusCompra() != 3) {


                        preencherListaHoje(rev);

                    }
                }

                Collections.sort(topProdutosRevendas30);
                Collections.sort(topRevendedores30);
                Collections.sort(revendas30, new Comparator<ObjectRevenda>() {
                    @Override
                    public int compare(ObjectRevenda objectRevenda, ObjectRevenda t1) {
                        return Long.compare(objectRevenda.getHora(), t1.getHora());
                    }
                });

                Collections.sort(topProdutosRevendas7);
                Collections.sort(topRevendedores7);
                Collections.sort(revendas7, new Comparator<ObjectRevenda>() {
                    @Override
                    public int compare(ObjectRevenda objectRevenda, ObjectRevenda t1) {
                        return Long.compare(objectRevenda.getHora(), t1.getHora());
                    }
                });

                Collections.sort(topProdutosRevendas1);
                Collections.sort(topRevendedores1);
                Collections.sort(revendas1, new Comparator<ObjectRevenda>() {
                    @Override
                    public int compare(ObjectRevenda objectRevenda, ObjectRevenda t1) {
                        return Long.compare(objectRevenda.getHora(), t1.getHora());
                    }
                });

                Collections.sort(topProdutosRevendasHoje);
                Collections.sort(topRevendedoresHoje);
                Collections.sort(revendasHoje, new Comparator<ObjectRevenda>() {
                    @Override
                    public int compare(ObjectRevenda objectRevenda, ObjectRevenda t1) {
                        return Long.compare(objectRevenda.getHora(), t1.getHora());
                    }
                });

                efab_analytics_atualizar_home.setVisibility(View.VISIBLE);

                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(AnalisarDadosActivity.this, getSupportFragmentManager(), topProdutosRevendas30, topRevendedores30, revendas30, topProdutosRevendas7, topRevendedores7, revendas7, topProdutosRevendas1, topRevendedores1, revendas1, topProdutosRevendasHoje, topRevendedoresHoje, revendasHoje);
                viewPager.setAdapter(sectionsPagerAdapter);
                tabs.setupWithViewPager(viewPager);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
            }
        });

    }

    private void preencherLista30(ObjectRevenda rev) {
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


        TopRevendedores topRevn = new TopRevendedores(rev.getUserNomeRevendedor(), rev.getPathFotoUserRevenda(), rev.getUidUserRevendedor(), rev.getListaDeProdutos().size());

        if (topRevendedores30.size() > 0) {

            boolean revendedorJaExiste = false;

            for (int x = 0; x < topRevendedores30.size(); x++) {

                if (topRevendedores30.get(x).getUidRevendedor().equals(topRevn.getUidRevendedor())) {
                    revendedorJaExiste = true;
                    topRevendedores30.get(x).setNumeroItensReveendas(topRevendedores30.get(x).getNumeroItensReveendas() + topRevn.getNumeroItensReveendas());
                    break;
                }

            }

            if (!revendedorJaExiste) {
                topRevendedores30.add(topRevn);
            }

        } else {
            topRevendedores30.add(topRevn);
        }


    }

    private void preencherLista7(ObjectRevenda rev) {

        for (int j = 0; j < rev.getListaDeProdutos().size(); j++) {
            ObjProdutoRevenda prodRev = rev.getListaDeProdutos().get(j);
            TopProdutosRevenda topPrd = new TopProdutosRevenda(prodRev.getProdutoName(), prodRev.getCaminhoImg(), prodRev.getIdProdut(), prodRev.getQuantidade());

            if (topProdutosRevendas7.size() > 0) {

                boolean produtoJaExiste = false;

                for (int k = 0; k < topProdutosRevendas7.size(); k++) {

                    if (topPrd.getIdProduto().equals(topProdutosRevendas7.get(k).getIdProduto())) {
                        produtoJaExiste = true;
                        topProdutosRevendas7.get(k).setNumeroDeRevendas(topPrd.getNumeroDeRevendas() + topProdutosRevendas7.get(k).getNumeroDeRevendas());
                        break;
                    }

                }

                if (!produtoJaExiste) {
                    topProdutosRevendas7.add(topPrd);
                }

            } else {
                topProdutosRevendas7.add(topPrd);
            }
        }


        TopRevendedores topRevn = new TopRevendedores(rev.getUserNomeRevendedor(), rev.getPathFotoUserRevenda(), rev.getUidUserRevendedor(), rev.getListaDeProdutos().size());

        if (topRevendedores7.size() > 0) {

            boolean revendedorJaExiste = false;

            for (int x = 0; x < topRevendedores7.size(); x++) {

                if (topRevendedores7.get(x).getUidRevendedor().equals(topRevn.getUidRevendedor())) {
                    revendedorJaExiste = true;
                    topRevendedores7.get(x).setNumeroItensReveendas(topRevendedores7.get(x).getNumeroItensReveendas() + topRevn.getNumeroItensReveendas());
                    break;
                }

            }

            if (!revendedorJaExiste) {
                topRevendedores7.add(topRevn);
            }

        } else {
            topRevendedores7.add(topRevn);
        }


    }

    private void preencherLista1(ObjectRevenda rev) {

        for (int j = 0; j < rev.getListaDeProdutos().size(); j++) {
            ObjProdutoRevenda prodRev = rev.getListaDeProdutos().get(j);
            TopProdutosRevenda topPrd = new TopProdutosRevenda(prodRev.getProdutoName(), prodRev.getCaminhoImg(), prodRev.getIdProdut(), prodRev.getQuantidade());

            if (topProdutosRevendas1.size() > 0) {

                boolean produtoJaExiste = false;

                for (int k = 0; k < topProdutosRevendas1.size(); k++) {

                    if (topPrd.getIdProduto().equals(topProdutosRevendas1.get(k).getIdProduto())) {
                        produtoJaExiste = true;
                        topProdutosRevendas1.get(k).setNumeroDeRevendas(topPrd.getNumeroDeRevendas() + topProdutosRevendas1.get(k).getNumeroDeRevendas());
                        break;
                    }

                }

                if (!produtoJaExiste) {
                    topProdutosRevendas1.add(topPrd);
                }

            } else {
                topProdutosRevendas1.add(topPrd);
            }
        }


        TopRevendedores topRevn = new TopRevendedores(rev.getUserNomeRevendedor(), rev.getPathFotoUserRevenda(), rev.getUidUserRevendedor(), rev.getListaDeProdutos().size());

        if (topRevendedores1.size() > 0) {

            boolean revendedorJaExiste = false;

            for (int x = 0; x < topRevendedores1.size(); x++) {

                if (topRevendedores1.get(x).getUidRevendedor().equals(topRevn.getUidRevendedor())) {
                    revendedorJaExiste = true;
                    topRevendedores1.get(x).setNumeroItensReveendas(topRevendedores1.get(x).getNumeroItensReveendas() + topRevn.getNumeroItensReveendas());
                    break;
                }

            }

            if (!revendedorJaExiste) {
                topRevendedores1.add(topRevn);
            }

        } else {
            topRevendedores1.add(topRevn);
        }


    }

    private void preencherListaHoje(ObjectRevenda rev) {

        for (int j = 0; j < rev.getListaDeProdutos().size(); j++) {
            ObjProdutoRevenda prodRev = rev.getListaDeProdutos().get(j);
            TopProdutosRevenda topPrd = new TopProdutosRevenda(prodRev.getProdutoName(), prodRev.getCaminhoImg(), prodRev.getIdProdut(), prodRev.getQuantidade());

            if (topProdutosRevendasHoje.size() > 0) {

                boolean produtoJaExiste = false;

                for (int k = 0; k < topProdutosRevendasHoje.size(); k++) {

                    if (topPrd.getIdProduto().equals(topProdutosRevendasHoje.get(k).getIdProduto())) {
                        produtoJaExiste = true;
                        topProdutosRevendasHoje.get(k).setNumeroDeRevendas(topPrd.getNumeroDeRevendas() + topProdutosRevendasHoje.get(k).getNumeroDeRevendas());
                        break;
                    }

                }

                if (!produtoJaExiste) {
                    topProdutosRevendasHoje.add(topPrd);
                }

            } else {
                topProdutosRevendasHoje.add(topPrd);
            }
        }


        TopRevendedores topRevn = new TopRevendedores(rev.getUserNomeRevendedor(), rev.getPathFotoUserRevenda(), rev.getUidUserRevendedor(), rev.getListaDeProdutos().size());

        if (topRevendedoresHoje.size() > 0) {

            boolean revendedorJaExiste = false;

            for (int x = 0; x < topRevendedoresHoje.size(); x++) {

                if (topRevendedoresHoje.get(x).getUidRevendedor().equals(topRevn.getUidRevendedor())) {
                    revendedorJaExiste = true;
                    topRevendedoresHoje.get(x).setNumeroItensReveendas(topRevendedoresHoje.get(x).getNumeroItensReveendas() + topRevn.getNumeroItensReveendas());
                    break;
                }

            }

            if (!revendedorJaExiste) {
                topRevendedoresHoje.add(topRevn);
            }

        } else {
            topRevendedoresHoje.add(topRevn);
        }


    }

}