package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterInterfaceMain;
import com.rapha.vendafavorita.adapter.AdapterMinhasRevendas;
import com.rapha.vendafavorita.adapter.AdapterProdutosPainelRevendedor;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.carteira.CarteiraUsuario;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.rankings.ResumeRankingActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;

public class PainelRevendedorActivity extends AppCompatActivity implements AdapterProdutosPainelRevendedor.ProdutoPainelListener, AdapterInterfaceMain.ListenerPrincipal {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private CollectionReference queryProd;
    private DocumentReference revendedorDocRef;
    private EventListener<DocumentSnapshot> listenerRevendedor;
    private ArrayList<ProdObj> resultadoPesquisa;
    private ListenerRegistration registroRevendedor;
    private ProgressBar pb;
    private RecyclerView rvProdutos;
    private Query minhasRevendasQuery;
    private ArrayList<ProdObj> listProds;
    private ArrayList<ObjectRevenda> listMinhasRevendas;
    private ArrayList<ObjectRevenda> aReceber;
    private View voltar;
    private CardView bt_afiliados_painel_revenda;
    private ExtendedFloatingActionButton efab_painel_revendedor;
    private NestedScrollView scrolRevend;

    private LinearLayout meu_historico_rendededor, minhas_comissoes_revendedor, botao_campeonatos;

    private String idUsuario, nome, path, zap;
    private AnalitycsGoogle analitycsGoogle;
    private TextView titulo_painel_revendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_revendedor);
        scrolRevend = (NestedScrollView) findViewById(R.id.scrol_revendedor);
        rvProdutos = (RecyclerView) findViewById(R.id.rv_produtos_painel_revenda);
        pb = (ProgressBar) findViewById(R.id.pb_painel_revenda);
        voltar = (View) findViewById(R.id.voltar_painel_revendedor);
        //totalCarteira = (TextView) findViewById(R.id.total_carteira_painel);
        titulo_painel_revendedor = (TextView) findViewById(R.id.titulo_painel_revendedor);
        minhas_comissoes_revendedor = (LinearLayout) findViewById(R.id.minhas_comissoes_revendedor);
        meu_historico_rendededor = (LinearLayout) findViewById(R.id.meu_historico_rendededor);
        botao_campeonatos = (LinearLayout) findViewById(R.id.botao_campeonatos);
        bt_afiliados_painel_revenda = (CardView) findViewById(R.id.bt_afiliados_painel_revenda);
        efab_painel_revendedor = (ExtendedFloatingActionButton) findViewById(R.id.efab_painel_revendedor);
        firestore = FirebaseFirestore.getInstance();

        idUsuario = getIntent().getStringExtra("id");
        nome = getIntent().getStringExtra("nome");
        path = getIntent().getStringExtra("path");
        zap = getIntent().getStringExtra("zap");
        auth = FirebaseAuth.getInstance();

        if (nome == null) {
            nome = auth.getCurrentUser().getDisplayName();
        }

        //titulo_painel_revendedor.setText("Categorias");

        queryProd = firestore.collection("produtos");
        revendedorDocRef = firestore.collection("Revendedores").document("amacompras").collection("ativos").document(auth.getCurrentUser().getUid());
        minhasRevendasQuery = firestore.collection("MinhasRevendas").document("Usuario").collection(auth.getCurrentUser().getUid()).orderBy("hora", Query.Direction.DESCENDING);

        bt_afiliados_painel_revenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelRevendedorActivity.this, PainelDeAfiliados.class);
                startActivity(intent);
            }
        });

        botao_campeonatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelRevendedorActivity.this, ResumeRankingActivity.class);
                startActivity(intent);
            }
        });

        minhas_comissoes_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelRevendedorActivity.this, CarteiraUsuario.class);
                startActivity(intent);
            }
        });

        meu_historico_rendededor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelRevendedorActivity.this, HistoricoRevendasActivity.class);
                startActivity(intent);
            }
        });

        listenerRevendedor = new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    //minhasRevendas();
                    if (listProds != null) {

                        if (listProds.size() < 1) {
                            listarProdutos();
                        }
                    } else {
                        listarProdutos();
                    }
                } else {
                    cadastrar();
                    finish();
                }
            }
        };

        revendedorDocRef.addSnapshotListener(this, listenerRevendedor);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        analitycsGoogle = new AnalitycsGoogle(this);




        efab_painel_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PainelRevendedorActivity.this, ListaRevendaActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!ADMINISTRADOR) {

            if (nome == null) {
                nome = auth.getCurrentUser().getDisplayName();
            }

            if (idUsuario == null) {
                idUsuario = auth.getCurrentUser().getUid();
            }

            if (path == null) {
                path = auth.getCurrentUser().getPhotoUrl().getPath();
            }


            analitycsGoogle.visitaAoPainelRevendedor(nome, idUsuario, path);

        }
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (documentoPrincipalDoUsuario != null) {
            if (documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() < 1) {
                cadastrar();
            }
        } else {

            cadastrar();
            finish();

        }
    }



    private void telaSucess() {
        pb.setVisibility(View.GONE);
        rvProdutos.setVisibility(View.VISIBLE);
        minhas_comissoes_revendedor.setVisibility(View.VISIBLE);
        meu_historico_rendededor.setVisibility(View.VISIBLE);
        botao_campeonatos.setVisibility(View.VISIBLE);
        bt_afiliados_painel_revenda.setVisibility(View.VISIBLE);
    }

    private void cadastrar() {
        Intent intent = new Intent(PainelRevendedorActivity.this, MeuPerfilActivity.class);
        intent.putExtra("alert", 2);
        startActivity(intent);
        finish();
    }

    private void minhasRevendas() {
        minhasRevendasQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    listMinhasRevendas = new ArrayList<>();
                    aReceber = new ArrayList<>();
                    int soma = 0;

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        ObjectRevenda objt = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);
                        listMinhasRevendas.add(objt);

                        if (!objt.isPagamentoRecebido() && objt.getStatusCompra() != 3) {
                            aReceber.add(objt);
                            soma = soma + objt.getComissaoTotal();
                        }
                    }


                    //totalCarteira.setText("R$ " + soma + ",00");

                    AdapterMinhasRevendas adapterMinhasRevendas = new AdapterMinhasRevendas(listMinhasRevendas, PainelRevendedorActivity.this);
                    minhas_comissoes_revendedor.setVisibility(View.VISIBLE);
                    meu_historico_rendededor.setVisibility(View.VISIBLE);
                    botao_campeonatos.setVisibility(View.VISIBLE);
                    scrolRevend.smoothScrollTo(0,0);

                }

                if (listProds != null) {

                    if (listProds.size() < 1) {
                        listarProdutos();
                    }
                } else {
                    listarProdutos();
                }

                scrolRevend.smoothScrollTo(0,0);
            }
        });
    }

    private void listarProdutos() {

        queryProd.orderBy("timeUpdate", Query.Direction.DESCENDING).limit(300).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                listProds = new ArrayList<>();


                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                    if (prod.isDisponivel()) {
                        listProds.add(prod);
                    }
                }

                Collections.sort(listProds, new Comparator<ProdObj>() {
                    @Override
                    public int compare(ProdObj obj, ProdObj t1) {
                        return Integer.compare(obj.getComissao(), t1.getComissao());
                    }
                });

                Collections.reverse(listProds);


                AdapterInterfaceMain mAdapter = new AdapterInterfaceMain(PainelRevendedorActivity.this, listProds, PainelRevendedorActivity.this);
                AdapterProdutosPainelRevendedor adapter = new AdapterProdutosPainelRevendedor(listProds, PainelRevendedorActivity.this, PainelRevendedorActivity.this);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvProdutos.setLayoutManager(new LinearLayoutManager(PainelRevendedorActivity.this));
                rvProdutos.setAdapter(mAdapter);


                telaSucess();
                scrolRevend.smoothScrollTo(0,0);

                //AdapterInterfaceMain mAdapter = new AdapterInterfaceMain(PainelRevendedorActivity.this, listProds, PainelRevendedorActivity.this);
                //rvProdutos.setLayoutManager(new LinearLayoutManager(PainelRevendedorActivity.this));
                //rvProdutos.setAdapter(mAdapter);

            }
        });

    }

    @Override
    public void clickCategoria(ArrayList<ProdObj> produtos) {

    }

    @Override
    public void clickProduto(ProdObj obj) {

        ProdObjParcelable objParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores(), obj.getProdValorPromocional(), obj.getProdValorAtacarejo(), obj.getProdValorAtacado(), obj.isAtacado(), obj.getUrlVideo(), obj.getNumVendas(), obj.getAvaliacao());
        Intent intent = new Intent(this, ProdutoRevendaActivity.class);

        intent.putExtra("prod", objParcelable);

        startActivity(intent);
    }

}
