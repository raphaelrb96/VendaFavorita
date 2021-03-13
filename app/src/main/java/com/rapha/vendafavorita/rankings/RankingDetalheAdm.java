package com.rapha.vendafavorita.rankings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjProdutoRevenda;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.rankings.adapters.AdapterNaoRankeadas;
import com.rapha.vendafavorita.rankings.adapters.AdapterReankeadas;
import com.rapha.vendafavorita.rankings.objcts.Participantes;
import com.rapha.vendafavorita.rankings.objcts.RankingObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class RankingDetalheAdm extends AppCompatActivity implements AdapterNaoRankeadas.NaoRankeadasListener, AdapterReankeadas.RankeadasListener {

    private FirebaseFirestore firestore;
    private ArrayList<ObjectRevenda> revendas, revendasPreContabilizadas, preRemovidas, contabilizadas;
    private RecyclerView rvNaoRankeadas, rv_rankeadas;

    private String tituloString, descricao, fim, inicio, id, premio;
    private int meta;
    private DocumentReference ref;

    private ProgressBar pb;
    private TextView title_rankeadas, tv_titulo_ranking_adm_detalhe, tv_premio_ranking_adm_detalhe, tv_inicio_ranking_adm, tv_fim_ranking_adm_detalhe, tv_participantes_ranking_adm_descricao, tv_vendas_ranking_adm_detalhe;
    private NestedScrollView scrol_ranking_detalhe_adm;
    private LinearLayout bt_criar_ranking;

    private AdapterNaoRankeadas adapterNaoRankeadas;
    private AdapterReankeadas adapterRankeadas;

    private RankingObj rankingObjOfc;

    private int vendasParaEXCLUIR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_detalhe_adm);
        scrol_ranking_detalhe_adm = (NestedScrollView)findViewById(R.id.scrol_ranking_detalhe_adm);
        pb = (ProgressBar) findViewById(R.id.pb_ranking_adm);
        bt_criar_ranking = (LinearLayout) findViewById(R.id.bt_criar_ranking);
        title_rankeadas = (TextView) findViewById(R.id.title_rankeadas);
        tv_titulo_ranking_adm_detalhe = (TextView) findViewById(R.id.tv_titulo_ranking_adm_detalhe);
        tv_premio_ranking_adm_detalhe = (TextView) findViewById(R.id.tv_premio_ranking_adm_detalhe);
        tv_inicio_ranking_adm = (TextView) findViewById(R.id.tv_inicio_ranking_adm_detalhe);
        tv_fim_ranking_adm_detalhe = (TextView) findViewById(R.id.tv_fim_ranking_adm_detalhe);
        tv_vendas_ranking_adm_detalhe = (TextView) findViewById(R.id.tv_vendas_ranking_adm_detalhe);
        tv_participantes_ranking_adm_descricao = (TextView) findViewById(R.id.tv_participantes_ranking_adm_descricao);
        firestore = FirebaseFirestore.getInstance();
        rvNaoRankeadas = (RecyclerView) findViewById(R.id.rv_nao_rankeadas);
        rv_rankeadas = (RecyclerView) findViewById(R.id.rv_rankeadas);

        id = getIntent().getStringExtra("id");

        if (id == null) {
            finish();
            return;
        }

        ref = firestore.collection("ranking").document(id);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot == null) {
                    finish();
                    return;
                }



                rankingObjOfc = documentSnapshot.toObject(RankingObj.class);
                contabilizadas = rankingObjOfc.getRevendasContabilizadas();
                getRevendas();

            }
        });

        bt_criar_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RankingObj objOk = atualizar();

                if (objOk != null) {

                    pb.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firestore.collection("ranking").document(objOk.getId());

                    documentReference.set(objOk).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    });

                }

            }
        });

    }

    private void verificarRevenda(ObjectRevenda objectRevenda) {

        if (contabilizadas == null) {
            revendas.add(objectRevenda);
            return;
        }

        if (contabilizadas.size() == 0) {
            revendas.add(objectRevenda);
            return;
        }

        for (int i = 0; i < contabilizadas.size(); i++) {
            ObjectRevenda ob = contabilizadas.get(i);
            if (ob.getIdCompra().equals(objectRevenda.getIdCompra())) {
                return;
            }
        }

        revendas.add(objectRevenda);

    }

    private void getRevendas() {
        firestore.collection("Revendas").orderBy("hora", Query.Direction.DESCENDING).limit(700).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.size() == 0) {
                    return;
                }

                revendas = new ArrayList<>();
                vendasParaEXCLUIR = 0;

                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                    ObjectRevenda rev = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);
                    if(rev.getStatusCompra() != 3) {
                        verificarRevenda(rev);
                    }

                }

                setList(revendas);

                pb.setVisibility(View.GONE);
                scrol_ranking_detalhe_adm.setVisibility(View.VISIBLE);

                if (rankingObjOfc.getRevendasContabilizadas() == null) {
                    title_rankeadas.setVisibility(View.GONE);
                    rv_rankeadas.setVisibility(View.GONE);
                } else {
                    if (rankingObjOfc.getRevendasContabilizadas().size() > 0) {
                        title_rankeadas.setVisibility(View.VISIBLE);
                        rv_rankeadas.setVisibility(View.VISIBLE);
                        adapterRankeadas = new AdapterReankeadas(contabilizadas, RankingDetalheAdm.this, RankingDetalheAdm.this);
                        rv_rankeadas.setLayoutManager(new LinearLayoutManager(RankingDetalheAdm.this, RecyclerView.HORIZONTAL, false));
                        rv_rankeadas.setAdapter(adapterRankeadas);
                    }
                }

                tv_titulo_ranking_adm_detalhe.setText(rankingObjOfc.getTitulo());
                tv_premio_ranking_adm_detalhe.setText("R$" + rankingObjOfc.getPremio());
                tv_inicio_ranking_adm.setText(rankingObjOfc.getInicioString());
                tv_fim_ranking_adm_detalhe.setText(rankingObjOfc.getFimString());

                if (rankingObjOfc.getRevendasContabilizadas() != null) {

                    ArrayList<ObjectRevenda> revendas = rankingObjOfc.getRevendasContabilizadas();
                    ArrayList<Participantes> revendedores = new ArrayList<>();

                    for (int i = 0; i < revendas.size(); i++) {

                        ObjectRevenda objrev = revendas.get(i);

                        ArrayList<ObjProdutoRevenda> oprs = objrev.getListaDeProdutos();

                        String uidVendaAtual = objrev.getUidUserRevendedor();
                        String nomeVendAtual = objrev.getUserNomeRevendedor();
                        String pathVendAtual = objrev.getPathFotoUserRevenda();

                        int vendas = 1;
                        int itens = 0;
                        int totalVendido = 0;

                        for (int j = 0; j < oprs.size(); j++) {

                            ObjProdutoRevenda obRev = oprs.get(j);

                            itens = itens + obRev.getQuantidade();

                            totalVendido = totalVendido + obRev.getValorTotal();


                        }

                        Participantes participantes = new Participantes(uidVendaAtual, nomeVendAtual, pathVendAtual, vendas, itens, totalVendido);
                        boolean jaExiste = false;
                        int indiceNaLista = 0;

                        for (int part = 0; part < revendedores.size(); part++) {

                            Participantes atual = revendedores.get(part);

                            if (atual.getUid().equals(participantes.getUid())) {

                                jaExiste = true;
                                indiceNaLista = part;
                                part = revendedores.size();

                            }

                        }

                        if (jaExiste) {

                            revendedores.get(indiceNaLista).setItensVendidos(itens);
                            revendedores.get(indiceNaLista).setTotalVendido(totalVendido);
                            revendedores.get(indiceNaLista).setVendas(vendas);

                        } else {
                            revendedores.add(participantes);
                        }


                    }

                    Collections.sort(revendedores, new Comparator<Participantes>() {
                        @Override
                        public int compare(Participantes participantes, Participantes t1) {
                            return Integer.compare(participantes.getItensVendidos(), t1.getItensVendidos());
                        }
                    });

                    Collections.reverse(revendedores);

                    tv_participantes_ranking_adm_descricao.setText(revendedores.size() + " Vendedores");
                }

                if (rankingObjOfc.getRevendasContabilizadas() != null) {
                    tv_vendas_ranking_adm_detalhe.setText(rankingObjOfc.getRevendasContabilizadas().size() + " Vendas");
                }

            }
        });
    }

    private void setList(ArrayList<ObjectRevenda> revendas) {
        rvNaoRankeadas.setLayoutManager(new LinearLayoutManager(this));
        adapterNaoRankeadas = new AdapterNaoRankeadas(revendas, this, this);
        rvNaoRankeadas.setAdapter(adapterNaoRankeadas);
    }


    @Override
    public void rankear(ObjectRevenda revenda, int p) {
        precontabilizar(revenda, p);
    }

    private void precontabilizar(ObjectRevenda revenda, int p) {
        if (revendasPreContabilizadas == null) {
            revendasPreContabilizadas = new ArrayList<>();
        }

        revendasPreContabilizadas.add(revenda);

        revendas.remove(p);
        adapterNaoRankeadas.atualizarAdapter(revendas);
    }

    public RankingObj atualizar () {

        if (revendasPreContabilizadas != null) {
            if (revendasPreContabilizadas.size() > 0) {

                for (int i = 0; i < revendasPreContabilizadas.size(); i++) {
                    ObjectRevenda rpc = revendasPreContabilizadas.get(i);
                    atualizarNaLista(rpc);
                }

                RankingObj obj = rankingObjOfc;

                Collections.sort(contabilizadas, new Comparator<ObjectRevenda>() {
                    @Override
                    public int compare(ObjectRevenda objectRevenda, ObjectRevenda t1) {
                        return Long.compare(objectRevenda.getHora(), t1.getHora());
                    }
                });

                Collections.reverse(contabilizadas);

                long time = System.currentTimeMillis();
                String agora = DateFormatacao.getDiaString(new Date(time));

                return new RankingObj(obj.getInicio(), obj.getInicioString(), obj.getFimString(), obj.getTitulo(), obj.getDescricao(), obj.getRegras(), obj.getPremio(), obj.getTipo(), obj.getTipoRakingAfiliado(), obj.getTipoRakingVenda(), obj.getMeta(), time, agora, obj.getGanhadores(), contabilizadas, obj.getId(), true);
            }
        }


        if (vendasParaEXCLUIR > 0) {

            RankingObj obj = rankingObjOfc;

            long time = System.currentTimeMillis();
            String agora = DateFormatacao.getDiaString(new Date(time));

            return new RankingObj(obj.getInicio(), obj.getInicioString(), obj.getFimString(), obj.getTitulo(), obj.getDescricao(), obj.getRegras(), obj.getPremio(), obj.getTipo(), obj.getTipoRakingAfiliado(), obj.getTipoRakingVenda(), obj.getMeta(), time, agora, obj.getGanhadores(), contabilizadas, obj.getId(), true);

        }


        return null;
    }


    private boolean atualizarNaLista (ObjectRevenda r) {

        if (contabilizadas == null) {

            contabilizadas = new ArrayList<>();

            contabilizadas.add(r);

            return true;

        }

        if (contabilizadas.size() == 0) {
            contabilizadas = new ArrayList<>();

            contabilizadas.add(r);

            return true;
        }

        for (int i = 0; i < contabilizadas.size(); i++) {
            ObjectRevenda contb = contabilizadas.get(i);
            if (contb.getIdCompra() == r.getIdCompra()) {
                return false;
            }

        }

        contabilizadas.add(r);

        return true;
    }

    @Override
    public void deletar(ObjectRevenda objectRevenda, int p) {
        contabilizadas.remove(p);
        vendasParaEXCLUIR++;
        adapterRankeadas.notifyDataSetChanged();
    }
}