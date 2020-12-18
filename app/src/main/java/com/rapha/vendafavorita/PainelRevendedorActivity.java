package com.rapha.vendafavorita;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rapha.vendafavorita.adapter.AdapterMinhasRevendas;
import com.rapha.vendafavorita.adapter.AdapterProdutosPainelRevendedor;
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.rankings.ResumeRankingActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;

public class PainelRevendedorActivity extends AppCompatActivity implements AdapterProdutosPainelRevendedor.ProdutoPainelListener {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private CollectionReference queryProd;
    private DocumentReference revendedorDocRef;
    private EventListener<DocumentSnapshot> listenerRevendedor;
    private ArrayList<ProdObj> resultadoPesquisa;
    private ListenerRegistration registroRevendedor;
    private ProgressBar pb;
    private RecyclerView rvProdutos, rvMinhasRevendas;
    private Query minhasRevendasQuery;
    private ArrayList<ProdObj> listProds;
    private ArrayList<ObjectRevenda> listMinhasRevendas;
    private ArrayList<ObjectRevenda> aReceber;
    private View voltar, bt_ranking;
    private CardView carteira, bt_afiliados_painel_revenda;
    private TextView statusCarteira, btCarteira, title_rv_produtos_painel_revenda, link_historico_revendas;
    private ExtendedFloatingActionButton efab_painel_revendedor;
    private NestedScrollView scrolRevend;

    private EditText et_pesquisar_painel_revendedor;
    private ImageButton bt_pesquisar_painel_revendedor;
    private String idUsuario, nome, path, zap;
    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_painel_revendedor);
        rvMinhasRevendas = (RecyclerView) findViewById(R.id.rv_ultimas_revendas);
        scrolRevend = (NestedScrollView) findViewById(R.id.scrol_revendedor);
        rvProdutos = (RecyclerView) findViewById(R.id.rv_produtos_painel_revenda);
        pb = (ProgressBar) findViewById(R.id.pb_painel_revenda);
        voltar = (View) findViewById(R.id.voltar_painel_revendedor);
        bt_ranking = (View) findViewById(R.id.bt_ranking);
        bt_pesquisar_painel_revendedor = (ImageButton) findViewById(R.id.bt_pesquisar_painel_revendedor);
        //totalCarteira = (TextView) findViewById(R.id.total_carteira_painel);
        statusCarteira = (TextView) findViewById(R.id.status_carteira_painel);
        link_historico_revendas = (TextView) findViewById(R.id.link_historico_revendas);
        carteira = (CardView) findViewById(R.id.card_carteira_painel_revenda);
        bt_afiliados_painel_revenda = (CardView) findViewById(R.id.bt_afiliados_painel_revenda);
        efab_painel_revendedor = (ExtendedFloatingActionButton) findViewById(R.id.efab_painel_revendedor);
        et_pesquisar_painel_revendedor = (EditText) findViewById(R.id.et_pesquisar_painel_revendedor);
        btCarteira = (TextView) findViewById(R.id.bt_carteira_painel);
        title_rv_produtos_painel_revenda = (TextView) findViewById(R.id.title_rv_produtos_painel_revenda);
        firestore = FirebaseFirestore.getInstance();
        idUsuario = getIntent().getStringExtra("id");
        nome = getIntent().getStringExtra("nome");
        path = getIntent().getStringExtra("path");
        zap = getIntent().getStringExtra("zap");
        auth = FirebaseAuth.getInstance();
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

        bt_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelRevendedorActivity.this, ResumeRankingActivity.class);
                startActivity(intent);
            }
        });

        btCarteira.setOnClickListener(new View.OnClickListener() {
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

        bt_pesquisar_painel_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoPesquisa = et_pesquisar_painel_revendedor.getText().toString();
                if (textoPesquisa.length() > 0) {

                    pesquisar(textoPesquisa);

                }
            }
        });

        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);


        et_pesquisar_painel_revendedor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_pesquisar_painel_revendedor.setFocusable(true);
                et_pesquisar_painel_revendedor.setFocusableInTouchMode(true);
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            et_pesquisar_painel_revendedor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String pesq = et_pesquisar_painel_revendedor.getText().toString();
                        if (pesq.length() > 0) {
                            pesquisar(pesq);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

        efab_painel_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PainelRevendedorActivity.this, ListaRevendaActivity.class);
                startActivity(intent);
            }
        });

        link_historico_revendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainelRevendedorActivity.this, HistoricoRevendasActivity.class);

                intent.putExtra("id", idUsuario);
                intent.putExtra("nome", nome);
                intent.putExtra("path", path);
                intent.putExtra("zap", zap);

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!ADMINISTRADOR) {

            analitycsFacebook.visitaAoPainelRevendedor(user.getDisplayName(), user.getUid(), pathFotoUser);
            analitycsGoogle.visitaAoPainelRevendedor(user.getDisplayName(), user.getUid(), pathFotoUser);

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

    private void pesquisar(final String st) {

        String busca = "tag." + st.toLowerCase();
        telaInicialLoadding();
        esconderTeclado();

        resultadoPesquisa = new ArrayList<>();

        queryProd.whereEqualTo(busca, true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                        if (prod.isDisponivel()) {
                            resultadoPesquisa.add(prod);
                        }
                    }

                    telaSucess();

                    if (listMinhasRevendas == null) {

                        rvMinhasRevendas.setVisibility(View.GONE);
                        carteira.setVisibility(View.GONE);
                        //title_rv_minhas_ultimas_revendas.setVisibility(View.GONE);

                    }


                    AdapterProdutosPainelRevendedor adapter = new AdapterProdutosPainelRevendedor(resultadoPesquisa, PainelRevendedorActivity.this, PainelRevendedorActivity.this);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    rvProdutos.setLayoutManager(layoutManager);
                    rvProdutos.setAdapter(adapter);

                    scrolRevend.smoothScrollTo(0,0);


                } else {
                    Toast.makeText(PainelRevendedorActivity.this, "Nenhum resultado para sua pesquisa", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void esconderTeclado() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(et_pesquisar_painel_revendedor.getWindowToken(), 0);
        }
    }

    private void telaInicialLoadding() {
        pb.setVisibility(View.VISIBLE);
        rvProdutos.setVisibility(View.GONE);
        title_rv_produtos_painel_revenda.setVisibility(View.GONE);
        bt_afiliados_painel_revenda.setVisibility(View.GONE);
        rvMinhasRevendas.setVisibility(View.GONE);
        carteira.setVisibility(View.GONE);
        link_historico_revendas.setVisibility(View.GONE);
    }

    private void telaSucess() {
        pb.setVisibility(View.GONE);
        rvProdutos.setVisibility(View.VISIBLE);
        title_rv_produtos_painel_revenda.setVisibility(View.VISIBLE);
        rvMinhasRevendas.setVisibility(View.VISIBLE);
        carteira.setVisibility(View.VISIBLE);
        bt_afiliados_painel_revenda.setVisibility(View.VISIBLE);
        link_historico_revendas.setVisibility(View.VISIBLE);
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

                    if (soma < 100) {
                        statusCarteira.setText("Ao completar 100 reais você poderá retirar seu dinheiro");
                    } else {
                        statusCarteira.setText("Você ja pode receber suas comissões");
                    }

                    AdapterMinhasRevendas adapterMinhasRevendas = new AdapterMinhasRevendas(listMinhasRevendas, PainelRevendedorActivity.this);
                    rvMinhasRevendas.setLayoutManager(new LinearLayoutManager(PainelRevendedorActivity.this, RecyclerView.HORIZONTAL, false));
                    rvMinhasRevendas.setAdapter(adapterMinhasRevendas);
                    carteira.setVisibility(View.VISIBLE);
                    rvMinhasRevendas.setVisibility(View.VISIBLE);
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

        queryProd.whereEqualTo("disponivel", true).limit(200).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                listProds = new ArrayList<>();


                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                    listProds.add(prod);
                }

                Collections.sort(listProds, new Comparator<ProdObj>() {
                    @Override
                    public int compare(ProdObj obj, ProdObj t1) {
                        return Integer.compare(obj.getComissao(), t1.getComissao());
                    }
                });

                Collections.reverse(listProds);

                AdapterProdutosPainelRevendedor adapter = new AdapterProdutosPainelRevendedor(listProds, PainelRevendedorActivity.this, PainelRevendedorActivity.this);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                rvProdutos.setLayoutManager(layoutManager);
                rvProdutos.setAdapter(adapter);
                telaSucess();
                scrolRevend.smoothScrollTo(0,0);

            }
        });

    }

    @Override
    public void clickProduto(ProdObj obj) {

        ProdObjParcelable objParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores());
        Intent intent = new Intent(this, ProdutoRevendaActivity.class);

        intent.putExtra("prod", objParcelable);

        startActivity(intent);
    }
}
