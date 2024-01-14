package com.rapha.vendafavorita;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.adapter.AdapterEmAlta;
import com.rapha.vendafavorita.adapter.AdapterInterfaceMain;
import com.rapha.vendafavorita.adapter.AdapterLancamentos;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.carteira.CarteiraUsuario;
import com.rapha.vendafavorita.objectfeed.ProdutoObj;
import com.rapha.vendafavorita.objects.FeedPrincipalObj;
import com.rapha.vendafavorita.objects.TokenFcm;
import com.rapha.vendafavorita.objects.UserStreamView;
import com.rapha.vendafavorita.objects.Usuario;
import com.rapha.vendafavorita.rankings.ResumeRankingActivity;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.rapha.vendafavorita.MainActivity.getUidShareLink;
import static com.rapha.vendafavorita.MainActivity.ids;
import static com.rapha.vendafavorita.MainActivity.setUidShareLink;

public class FragmentMain extends Fragment implements AdapterInterfaceMain.ListenerPrincipal, AdapterProdutos.ClickProdutoCliente, AdapterLancamentos.HandlerProdAtalho, AdapterEmAlta.EmaltaListener {

    private AnalitycsGoogle analitycsGoogle;

    private static final int RC_SIGN_IN = 124;
    private static final int RC_SIGN_IN_ADM = 567;

    public static Usuario documentoPrincipalDoUsuario;

    //private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private FirebaseFirestore firestore;

    public static FrameLayout containerFrag;
    public static RecyclerView mListMercadorias;
    public static FragmentManager manager;
    private LinearLayout containerLogin;
    private HorizontalScrollView containerMenu;
    private FirebaseAuth auth;
    //private ExtendedFloatingActionButton efabCart;
    private CollectionReference carrinhoDoUsuario;
    private AdapterInterfaceMain mAdapter;
    private CarComprasActivy mComprasActivy;
    private String myUserAtual;
    private int referencia = 0;
    private ArrayList<ProdObj> prodObjs;
    private ArrayList<ProdObj> resultadoPesquisa;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser user = null;
    public static String pathFotoUser = "";
    private ProgressBar pb;
    private ImageButton btPesquisar;
    private TextView tvErro;
    private Toolbar toolbar;

    private CardView botao_topo;

    private FrameLayout scrol_main;

    private RecyclerView rv_novidades, rv_em_alta, rv_mais_vendidos;

    private LinearLayout btZap, btSair, btMensagem, btMinhasCompras, container_resumo_principal;
    //private ExtendedFloatingActionButton btMeuCarrinho;
    private EditText etpesquisar;

    private FloatingActionButton efabCart;

    private TextView nome_top_vend_1, nome_top_vend_2, nome_top_vend_3, nome_top_vend_4, nome_top_vend_5, nome_top_vend_6;
    private TextView nome_top_adm_1, nome_top_adm_2, nome_top_adm_3;


    private View whatsappBt, faceBt, instaBt, chatBt, botaoCartToolbar;

    private int tipoReferencia = 0;
    private Query query;
    public static final boolean ADMINISTRADOR = true;
    private ImageView fundo;

    private FrameLayout bt_homeBottombar, bt_meu_perfil_main, bt_afiliados_main, bt_mensagem_main, bt_carrinho_bottom_main;
    private NestedScrollView lista_principal;

    private CardView bt_categ_11_fones;
    private CardView bt_categ_2_caixa;

    private FeedPrincipalObj feedPrincipalObj;
    private CardView historico_home, bonus_home, categorias_home, comissoes_home;
    private CardView bt_categ_1_smartwatch;
    private CardView bt_categ_5_automotivos;
    private CardView bt_categ_6_games;
    private CardView bt_categ_7_informatica;
    private CardView bt_categ_8_ferramentas;
    private CardView bt_categ_9_brinquedos;
    private CardView bt_categ_13_cameras;
    private CardView bt_categ_14_salao;
    private CardView bt_categ_18_microfones;
    private CardView bt_categ_50_cozinha;

    //private FrameLayout  bt_painel_revendedor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_v2, container, false);

        //FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        //whatsappBt = (View) view.findViewById(R.id.bt_whats);
        //faceBt = (View) view.findViewById(R.id.bt_face);
        //instaBt = (View) view.findViewById(R.id.bt_insta);

        chatBt = (View) view.findViewById(R.id.bt_chat);
        botaoCartToolbar = (View) view.findViewById(R.id.bt_perfil);

        botao_topo = (CardView) view.findViewById(R.id.botao_topo);

        bt_categ_11_fones = (CardView) view.findViewById(R.id.bt_categ_11_fones);
        bt_categ_1_smartwatch = (CardView) view.findViewById(R.id.bt_categ_1_smartwatch);
        bt_categ_2_caixa = (CardView) view.findViewById(R.id.bt_categ_2_caixa);
        bt_categ_5_automotivos = (CardView) view.findViewById(R.id.bt_categ_5_automotivos);
        bt_categ_6_games = (CardView) view.findViewById(R.id.bt_categ_6_games);
        bt_categ_7_informatica = (CardView) view.findViewById(R.id.bt_categ_7_informatica);
        bt_categ_8_ferramentas = (CardView) view.findViewById(R.id.bt_categ_8_ferramentas);
        bt_categ_9_brinquedos = (CardView) view.findViewById(R.id.bt_categ_9_brinquedos);
        bt_categ_13_cameras = (CardView) view.findViewById(R.id.bt_categ_13_cameras);
        bt_categ_14_salao = (CardView) view.findViewById(R.id.bt_categ_14_salao);
        bt_categ_18_microfones = (CardView) view.findViewById(R.id.bt_categ_18_microfones);
        bt_categ_50_cozinha = (CardView) view.findViewById(R.id.bt_categ_50_cozinha);

        historico_home = (CardView) view.findViewById(R.id.historico_home);
        bonus_home = (CardView) view.findViewById(R.id.bonus_home);
        categorias_home = (CardView) view.findViewById(R.id.categorias_home);
        comissoes_home = (CardView) view.findViewById(R.id.comissoes_home);

        scrol_main = (FrameLayout) view.findViewById(R.id.scrol_main);

        rv_novidades = (RecyclerView) view.findViewById(R.id.rv_novidades);
        rv_em_alta = (RecyclerView) view.findViewById(R.id.rv_em_alta);
        rv_mais_vendidos = (RecyclerView) view.findViewById(R.id.rv_mais_vendidos);

        bt_homeBottombar = (FrameLayout) view.findViewById(R.id.bt_carrinho_revenda_main);
        bt_afiliados_main = (FrameLayout) view.findViewById(R.id.bt_afiliados_main);
        bt_mensagem_main = (FrameLayout) view.findViewById(R.id.bt_mensagem_main);
        bt_meu_perfil_main = (FrameLayout) view.findViewById(R.id.bt_meu_perfil_main);
        bt_carrinho_bottom_main = (FrameLayout) view.findViewById(R.id.bt_carrinho_bottom_main);
        //mensagem_main = (FrameLayout) view.findViewById(R.id.mensagem_main);
        //bt_painel_revendedor = (FrameLayout) view.findViewById(R.id.bt_painel_revendedor);

        efabCart = (FloatingActionButton) view.findViewById(R.id.efab_main);

        categoriaFindView(view);

        mListMercadorias = (RecyclerView) view.findViewById(R.id.rv_fragment_main);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_main);
        etpesquisar= (EditText) view.findViewById(R.id.et_pesquisar);
        btZap = (LinearLayout) view.findViewById(R.id.ll_bt_zap_menu);
        btSair = (LinearLayout) view.findViewById(R.id.ll_bt_sair);
        btMensagem = (LinearLayout) view.findViewById(R.id.ll_bt_mensagem_menu);
        btMinhasCompras = (LinearLayout) view.findViewById(R.id.ll_bt_minhas_compras_menu);
        container_resumo_principal = (LinearLayout) view.findViewById(R.id.container_resumo_principal);

        lista_principal = (NestedScrollView) view.findViewById(R.id.lista_principal);

        containerMenu = (HorizontalScrollView) view.findViewById(R.id.container_menu);
        btPesquisar = (ImageButton) view.findViewById(R.id.bt_pesquisar);

        //filterIcon = coordinatorLayout.findViewById(R.id.filterIcon);

        pb = (ProgressBar) view.findViewById(R.id.pb_main);
        //efabCart = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_meu_carrinho);
        tvErro = (TextView) view.findViewById(R.id.tv_error_main);

        nome_top_vend_1 = (TextView) view.findViewById(R.id.nome_top_vend_1);
        nome_top_vend_2 = (TextView) view.findViewById(R.id.nome_top_vend_2);
        nome_top_vend_3 = (TextView) view.findViewById(R.id.nome_top_vend_3);
        nome_top_vend_4 = (TextView) view.findViewById(R.id.nome_top_vend_4);
        nome_top_vend_5 = (TextView) view.findViewById(R.id.nome_top_vend_5);
        nome_top_vend_6 = (TextView) view.findViewById(R.id.nome_top_vend_6);

        nome_top_adm_1 = (TextView) view.findViewById(R.id.nome_top_adm_1);
        nome_top_adm_2 = (TextView) view.findViewById(R.id.nome_top_adm_2);
        nome_top_adm_3 = (TextView) view.findViewById(R.id.nome_top_adm_3);


        //View btChat = (View) view.findViewById(R.id.bt_abrir_chat);
        //View btcar = (View) view.findViewById(R.id.bt_abrir_carrinho);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        query = firestore.collection("produtos");


        //sheetBehavior.setFitToContents(false);
        //sheetBehavior.setHideable(false);
        //prevents the boottom sheet from completely hiding off the screen

        //initially state to fully expanded

        prodObjs = new ArrayList<>();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("237281954777-45kb6q3j06k2mejnef0cbfn3jpf46f43.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //LoginManager.getInstance().registerCallback(callbackManager, FragmentMain.this);

        ligarOuvintes();

        analitycsGoogle = new AnalitycsGoogle(getActivity());

//        telaInicialLoadding();
//        auth.addAuthStateListener(mAuthStateListener);


        Log.d("TestFragmentMain", "OnCreateView");




        return view;
    }

    private void ligarOuvintes() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                auth = firebaseAuth;
                user = firebaseAuth.getCurrentUser();

                Log.d("TesteLogin", "onAuthStateChanged( )");



                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getActivity().getIntent())
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                // Get deep link from result (may be null if no link is found)
                                Uri deepLink = null;
                                if (pendingDynamicLinkData != null) {
                                    deepLink = pendingDynamicLinkData.getLink();
                                }

                                if(deepLink != null) {

                                    String parameterId = deepLink.getQueryParameter("id");
                                    String parameterAdm = deepLink.getQueryParameter("adm");
                                    String pathLink = deepLink.getLastPathSegment();

                                    if(pathLink != null) {

                                        if(pathLink.length() > 0 && pathLink.equals("produto")) {
                                            if (parameterId != null && user != null) {
                                                Intent intent = new Intent(getActivity(), ProdutoRevendaActivity.class);
                                                intent.putExtra("id", parameterId);
                                                startActivity(intent);
                                            }
                                        }

                                        if(pathLink.length() > 0 && pathLink.equals("cadastro")) {
                                            if(parameterAdm != null) {
                                                setUidShareLink(parameterAdm);
                                                if(user == null) {
                                                    Log.d("TesteLogin", "User null: Ir para login");

                                                    Intent intent = new Intent(getActivity(), LoginMainActivity.class);
                                                    intent.putExtra("adm", parameterAdm);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else {
                                                    //Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    //intent.putExtra("adm", parameterAdm);
                                                    //startActivity(intent);
                                                }

                                            }
                                        }
                                    }


                                }

                                logicaPrincipal();

                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("DynamicLink", "getDynamicLink:onFailure", e);
                                logicaPrincipal();
                            }
                        });




            }
        };

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista_principal.scrollTo(0,0);
                String pesq = etpesquisar.getText().toString();
                if (pesq.length() > 0) {
                    pesquisar(pesq);
                }
            }
        });

        //atalhos

        categorias_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCateg bottomSheetCateg = BottomSheetCateg.newInstance();
                bottomSheetCateg.setContext(getActivity());
                bottomSheetCateg.setListener(new BottomSheetCateg.ListenerBottomSheetCategoria() {
                    @Override
                    public void clickBottomSheetCategoria(String s, int pos) {
                        bottomSheetCateg.dismiss();
                        lista_principal.scrollTo(0, 0);
                        telaInicialLoadding(null);
                        myQuery(firestore.collection("produtos").whereEqualTo("categorias."+pos, true), false, s, -1);
                    }
                });
                bottomSheetCateg.show(getParentFragmentManager(), "Categoria");
            }
        });

        bonus_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResumeRankingActivity.class);
                startActivity(intent);
            }
        });

        comissoes_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CarteiraUsuario.class);
                startActivity(intent);
            }
        });

        historico_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoricoRevendasActivity.class);
                startActivity(intent);
            }
        });


        //categorias

        bt_categ_1_smartwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.1", true), false, "", -1);
            }
        });

        bt_categ_2_caixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.2", true), false, "", -1);
            }
        });

        bt_categ_5_automotivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.5", true), false, "", -1);
            }
        });

        bt_categ_6_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.6", true), false, "", -1);
            }
        });

        bt_categ_7_informatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.7", true), false, "", -1);
            }
        });

        bt_categ_8_ferramentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.8", true), false, "", -1);
            }
        });

        bt_categ_9_brinquedos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.9", true), false, "", -1);
            }
        });

        bt_categ_11_fones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.11", true), false, "", -1);
            }
        });

        bt_categ_13_cameras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.13", true), false, "", -1);
            }
        });

        bt_categ_14_salao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.4", true), false, "", -1);
            }
        });

        bt_categ_18_microfones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.18", true), false, "", -1);
            }
        });

        bt_categ_50_cozinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding(null);
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.50", true), false, "", -1);
            }
        });


        //navegacao

        bt_afiliados_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PainelDeAfiliados.class);
                intent.putExtra("id", user.getUid());
                intent.putExtra("nome", user.getDisplayName());
                intent.putExtra("path", user.getPhotoUrl());
                intent.putExtra("zap", user.getPhoneNumber());
                startActivity(intent);
            }
        });

        bt_carrinho_bottom_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListaRevendaActivity.class);
                startActivity(intent);
            }
        });


        bt_mensagem_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ADMINISTRADOR) {
                    return;
                }

                startActivity(new Intent(getActivity(), MensagemDetalheActivity.class));
            }
        });

        bt_homeBottombar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prodObjs.size() < 50) {

                    obterListaDeProdutos(null);
                    return;

                }

                obterListaDeProdutos(null);

            }
        });


        efabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null) return;

                Intent intent = new Intent(getActivity(), PainelRevendedorActivity.class);
                intent.putExtra("id", user.getUid());
                intent.putExtra("nome", user.getDisplayName());
                intent.putExtra("path", user.getPhotoUrl());
                intent.putExtra("zap", user.getPhoneNumber());
                startActivity(intent);
            }
        });

        botao_topo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null) return;

                Intent intent = new Intent(getActivity(), PainelRevendedorActivity.class);
                intent.putExtra("id", user.getUid());
                intent.putExtra("nome", user.getDisplayName());
                intent.putExtra("path", user.getPhotoUrl());
                intent.putExtra("zap", user.getPhoneNumber());
                startActivity(intent);
            }
        });

        bt_meu_perfil_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                painelAdm();

            }
        });

        bt_meu_perfil_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getActivity(), MeuPerfilActivity.class));
                return true;
            }
        });

        categoriaListeners();

        /*
        mensagem_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMensagens();
            }
        });

        mensagem_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                auth.signOut();
                return false;
            }
        });

        bt_painel_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


         */



        /*

        faceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://page/101644948088992";
                String urlPage = "http://www.facebook.com/101644948088992";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    //Log.e(TAG, "Aplicación no instalada.");
                    //Abre url de pagina.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
            }
        });

        instaBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/amacompras.com.br");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/amacompras.com.br")));
                }
            }
        });

        whatsappBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                String urlWhats = "https://api.whatsapp.com/send?phone=+5592991933525";
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlWhats));
                    getActivity().startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                //sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //sendIntent.setType("text/plain");
                //sendIntent.setPackage("com.whatsapp");
                //startActivity(sendIntent);
            }
        });

         */

        /*

        btMinhasCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (user.isAnonymous()) {
                        showDialog(1, "Faça login para ver suas compras");
                        return;
                    }
                    Intent intent = new Intent(getActivity(), MinhasComprasActivity.class);
                    intent.putExtra("uid", user.getUid());
                    startActivity(intent);
                }
            }
        });


        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (user.isAnonymous()) {
                        showDialog(1, "Faça login para poder enviar uma mensagem");
                        return;
                    }
                    startActivity(new Intent(getActivity(), MensagemDetalheActivity.class).putExtra("id", user.getUid()));
                }
            }
        });


        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (user.isAnonymous()) {
                        showDialog(1, "Você ainda não fez Login");
                        return;
                    }
                    auth.signOut();
                    getActivity().recreate();
                }
            }
        });

*/

        etpesquisar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etpesquisar.setFocusable(true);
                etpesquisar.setFocusableInTouchMode(true);
                return false;
            }
        });

        etpesquisar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String pesq = etpesquisar.getText().toString();
                    if (pesq.length() > 0) {
                        lista_principal.scrollTo(0, 0);
                        pesquisar(pesq);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void logicaPrincipal() {
        if (user != null) {


            onSignedInInitialize();

            if (user.isAnonymous()) {
                //efabCart.setVisibility(View.GONE);
                //toggleBackContainer(false);
                obterListaDeProdutos(feedPrincipalObj);
                return;
            } else {
                //toggleBackContainer(true);
                carregarFotoPerfil();
                if (!ADMINISTRADOR) {
                    analitycsGoogle.logUserStreamViewEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
                    UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
                    firestore.collection("Eventos").document("stream").collection("app").document(user.getUid()).set(userStreamView);
                }

                obterListaDeProdutos(feedPrincipalObj);
                //getListCart();
                //verificarUsuario();
                //getTokenNoificacoes();
                checkDadosUsuario();
            }

        } else {

            //toggleBackContainer(false);
            ids.clear();
            ids = new ArrayList();
            if (isDeviceOnline()) {
                //TODO 001: TROCAR NA COMPILACAO DE ADM
                //loginAdmin();
                //ou
                //loginUsuarioAnonimo();
                String adm = getUidShareLink();

                if(adm != null) {
                    startActivity(new Intent(getActivity(), LoginMainActivity.class).putExtra("adm", adm));
                    getActivity().finish();
                    return;
                } else {
                    startActivity(new Intent(getActivity(), LoginMainActivity.class));
                    getActivity().finish();
                    return;
                }

            } else {
                //exibir interface vazia
                telaInicialErro("Baixa conexão com a internet");
            }
        }
    }

    private void painelAdm() {
        if (ADMINISTRADOR) {
            //TODO DESCOMENTAR NA VERSAO ADM
            startActivity(new Intent(getActivity(), Secretaria.class));
        } else {
            startActivity(new Intent(getActivity(), MeuPerfilActivity.class));
        }
    }

    private void categoriaFindView(View view) {



    }

    private void categoriaListeners() {



    }

    private void verificarUsuario() {


        DocumentReference usuarioRef = firestore.collection("Usuario").document(auth.getUid());
        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {


                if (userDoc.exists()) {

                    documentoPrincipalDoUsuario = userDoc.toObject(Usuario.class);

                    Log.d("TesteCadastroAfiliados", "VerificarUsuario() chamado, Uid Adm: " + documentoPrincipalDoUsuario.getUidAdm());

                    if (documentoPrincipalDoUsuario != null) {

                        if (documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() == 0) {

                            Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                            startActivity(intent);

                        }

                    }

                } else {

                    //getTokenNoificacoes();

                    Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                    startActivity(intent);

                }

            }
        });
    }

    private void checkDadosUsuario() {

        final DocumentReference usuarioRef = firestore.collection("Usuario").document(user.getUid());
        final DocumentReference admRef = firestore.collection("Adm").document(user.getUid());

        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {
                WriteBatch batch = firestore.batch();

                boolean usuarioExiste = false;

                if (ADMINISTRADOR) {
                    //TokenFcm tokenFcmAdmin = new TokenFcm(tokenAtual, user.getDisplayName());
                    //batch.set(admRef, tokenFcmAdmin);
                }

                if (pathFotoUser.equals("")) {
                    pathFotoUser = getFotoUser(user);
                }

                if (userDoc.exists()) {

                    usuarioExiste = true;

                    Usuario usuarioObj = userDoc.toObject(Usuario.class);

                    documentoPrincipalDoUsuario = usuarioObj;


                } else {

                    usuarioExiste = false;

                    //novo
                    String num = "";
                    String provedor = "Google";
                    if (user.getPhoneNumber() != null) {
                        num = user.getPhoneNumber();
                    }


                    long time = System.currentTimeMillis();
                    Usuario noovoUsuario = new Usuario(user.getDisplayName(), user.getEmail(), num, Constantes.CONTROLE_VERSAO_USUARIO, user.getUid(), pathFotoUser, Constantes.USUARIO_TIPO_CLIENTE, provedor, time, time, "", null, "", "", "", "", "", false);
                    batch.set(usuarioRef, noovoUsuario);


                }

                if(!usuarioExiste) {
                    Log.d("TesteLogin", "Doc usuario não existe: Criando um agora");

                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(getActivity(), user.getDisplayName() + ", Ok", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                            String adm = getUidShareLink();


                            if(adm != null) {
                                intent.putExtra("adm", adm);
                            }

                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(), "Erro ao Salvar", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {

                    if (documentoPrincipalDoUsuario != null) {

                        if (documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() == 0) {

                            Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                            String adm = getUidShareLink();
                            if(adm != null) {
                                intent.putExtra("adm", adm);
                            }
                            startActivity(intent);

                        }

                    }

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "Erro ao Salvar", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void onSignedInInitialize() {
        carrinhoDoUsuario = FirebaseFirestore.getInstance().collection("carComprasActivy").document("usuario").collection(user.getUid());
    }

    private void abrirMensagens() {
        Intent intent = new Intent(getActivity(), MensagemDetalheActivity.class);
        intent.putExtra("id", auth.getCurrentUser().getUid());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        } else if (requestCode == RC_SIGN_IN_ADM) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                //ADMINISTRADOR = true;
                //abrirMensagem();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        } else {
        }
    }

    @Override
    public void onResume() {
        Log.d("TestFragmentMain", "OnResume");
        super.onResume();


    }

    @Override
    public void onStart() {
        Log.d("TestFragmentMain", "OnStart");
        //telaInicialLoadding();
        auth.addAuthStateListener(mAuthStateListener);
        super.onStart();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("TestFragmentMain", "OnStop");
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mAuthStateListener != null) {
            //auth.removeAuthStateListener(this.mAuthStateListener);
        }
    }

    private void showDialog(int tipo, String msg) {
        switch (tipo) {
            case 1:
                //R.style.AppCompatAlertDialog
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;

            default:
                break;
        }
    }

    private String getFotoUser (FirebaseUser user) {
        if (user == null) return "";

        for(int xis = 0; xis < user.getProviderData().size(); xis++) {
            try {
                String xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "";

                if (user.getProviderData().get(xis).getProviderId().equals("facebook.com")) {
                    xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "?type=large&redirect=true&width=500&height=500";
                }
                return xs;
            } catch (NullPointerException e) {
                return "";
            }

        }

        return "";

    }

    private void carregarFotoPerfil() {
        if (user == null) {
            return;
        }
        for(int xis = 0; xis < user.getProviderData().size(); xis++) {
            try {
                String xs = user.getProviderData().get(xis).getPhotoUrl().toString();

                if (user.getProviderData().get(xis).getProviderId().equals("facebook.com")) {
                    xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "?type=large&redirect=true&width=500&height=500";
                }

                pathFotoUser = xs;
                //Glide.with(getActivity()).load(xs).into(imgPerfil);
                /*
                Glide.with(getActivity())
                        .asBitmap()
                        .load(xs)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                //imgPerfil.setImageBitmap(resource);
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette p) {
                                        // Use generated instance
                                        //fundo.setBackground(new ColorDrawable(p.getDarkVibrantSwatch().getRgb()));
                                    }
                                });
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                */
            } catch (NullPointerException e) {

            }

        }
    }

    private void toggleFilters(){

    }

    private void loginAdmin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN_ADM);
    }

    private void loginUsuarioAnonimo() {
        auth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG_Sucess", "signInAnonymously:success");
                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void myQuery(Query meuQuery, final boolean isPesquisa, final String sPesquisa, final int tipo) {
        prodObjs.clear();

        if (!isPesquisa) {
            meuQuery = meuQuery.whereEqualTo("disponivel", true);
        }

        lista_principal.scrollTo(0, 0);

        meuQuery.limit(200).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int sizeDoc = queryDocumentSnapshots.size();
                if (!queryDocumentSnapshots.isEmpty()) {

                    ArrayList<ProdObj> list = new ArrayList<>();

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                        if (prod.isDisponivel()) {
                            prodObjs.add(prod);
                        }
                    }


                    if (isPesquisa) {
                        analitycsGoogle.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), true);
                    }


                    if (tipo == 0) {
                        for (int y = 0; y < prodObjs.size(); y++) {
                            if (prodObjs.get(y).isPromocional()) {
                                list.add(prodObjs.get(y));
                            }

                        }
                    } else if(tipo == -1) {
                        for (int y = 0; y < prodObjs.size(); y++) {
                            list.add(prodObjs.get(y));
                        }
                    } else {
                        for (int y = 0; y < prodObjs.size(); y++) {
                            if (prodObjs.get(y).getCategorias().containsKey(String.valueOf(tipo))) {
                                list.add(prodObjs.get(y));
                            }
                        }
                    }

                    Log.d("ListSize", String.valueOf(list.size()));

                    int colunas = 2;

                    if(list.size() > 140) {
                        colunas = 3;
                        list = new ArrayList<>(list.subList(0, 120));
                    }


                    Collections.sort(list, new Comparator<ProdObj>() {
                        @Override
                        public int compare(ProdObj o1, ProdObj o2) {

                            return Long.compare(o2.getTimeUpdate(), o1.getTimeUpdate());
                        }
                    });

                    AdapterProdutos adapterProdutos = new AdapterProdutos(FragmentMain.this, getActivity(), list, true, colunas);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(colunas, StaggeredGridLayoutManager.VERTICAL);
                    mListMercadorias.setLayoutManager(layoutManager);
                    mListMercadorias.setAdapter(adapterProdutos);

                    //mAdapter = new AdapterInterfaceMain(getActivity(), prodObjs, FragmentMain.this);
                    //mListMercadorias.setLayoutManager(new LinearLayoutManager(getActivity()));
                    //mListMercadorias.setAdapter(mAdapter);
                    telaInicialSucesso();

                    return;
                }



                if (isPesquisa && sizeDoc == 0) {
                    Toast.makeText(getActivity(), "Nenhum resultado para sua busca", Toast.LENGTH_LONG).show();
                    if (!ADMINISTRADOR) {
                        analitycsGoogle.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), false);
                    }
                    obterListaDeProdutos(feedPrincipalObj);
                    telaInicialErro("Nenhum resultado encontrado para sua busca por: " + sPesquisa);
                } else if (sizeDoc == 0) {
                    Toast.makeText(getActivity(), "Nenhum produto encontrado", Toast.LENGTH_LONG).show();
                    obterListaDeProdutos(feedPrincipalObj);
                    telaInicialErro("Nenhum produto encontrado na categoria:" + sPesquisa);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }



    private void obterListaDeProdutos(FeedPrincipalObj feedObj) {


        if(feedObj != null) {
            showScreenMain(feedObj);
            return;
        }

        telaInicialLoadding(feedObj);




        lista_principal.scrollTo(0, 0);
        //myQuery(firestore.collection("produtos").whereEqualTo("disponivel", true), false, "", tipo);

        firestore.collection("Feed").document("Main").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pb.setVisibility(View.GONE);
                if (documentSnapshot == null) {

                } else {
                    FeedPrincipalObj feedData = documentSnapshot.toObject(FeedPrincipalObj.class);
                    showScreenMain(feedData);
                }
            }
        });

    }

    private void esconderTeclado() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(etpesquisar.getWindowToken(), 0);
    }

    private void pesquisar(final String st) {

        StringBuilder nick = new StringBuilder();

        for (int i = 0; i < st.length(); i++) {

            String ch = String.valueOf(st.charAt(i));

            if (ch.equals(".")) {
                ch = "";
            }

            nick.append(ch);

        }

        Log.d("BuscaFormatada", nick.toString());

        String busca = "tag." + nick.toString().toLowerCase();
        telaInicialLoadding(null);
        esconderTeclado();

        resultadoPesquisa = new ArrayList<>();

        /*
        for (int x = 0; x < prodObjs.size(); x++) {
            Log.d("Teste123", String.valueOf(x));
            String nomeProd = prodObjs.get(x).getProdName().toLowerCase();
            boolean palavraIdentica = nomeProd.equals(st.toLowerCase());
            boolean palavraParecida = nomeProd.contains(st.toLowerCase());
            boolean palavraChaveExiste = prodObjs.get(x).getTag().containsKey(st.toLowerCase());
            if (palavraChaveExiste || palavraIdentica || palavraParecida) {
                resultadoPesquisa.add(prodObjs.get(x));
                //Log.d("Teste123", String.valueOf(x) + " adicionada a liste");
                //Log.d("Teste123", resultadoPesquisa.toString());
            }
        }



        if (resultadoPesquisa.size() < 1) {

            for (int i = 0; i < prodObjs.size(); i++) {

                final ProdObj obj = prodObjs.get(i);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    obj.getTag().forEach(new BiConsumer<String, Boolean>() {
                        @Override
                        public void accept(String s, Boolean aBoolean) {
                            if (s.equals(st) || st.contains(s) || s.contains(st)) {
                                if (!resultadoPesquisa.contains(obj)) {
                                    resultadoPesquisa.add(obj);
                                }
                            }
                        }
                    });

                }

                if (obj.getProdName().contains(st) || obj.getProdName().equals(st) || st.contains(obj.getProdName())) {
                    if (!resultadoPesquisa.contains(obj)) {
                        resultadoPesquisa.add(obj);
                    }
                }
            }


         */

        if (resultadoPesquisa.size() < 1) {
            myQuery(firestore.collection("produtos").whereEqualTo(busca, true), true, st, -1);
        } else {
            mAdapter = new AdapterInterfaceMain(getActivity(), prodObjs, this);
            mListMercadorias.setLayoutManager(new LinearLayoutManager(getActivity()));
            mListMercadorias.setAdapter(mAdapter);
            telaInicialSucesso();

        }


    }

    private void getListCart () {
        carrinhoDoUsuario.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                                String id = querySnapshot.getDocuments().get(i).getId();
                                if (!ids.contains(id)) {
                                    ids.add(id);
                                }
                            }

                            //obterListaDeProdutos(tipoReferencia);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            FirebaseUser user = authResult.getUser();
//                            Log.d("TesteLogin", "Goglle sucess");
                            String foto = getFotoUser(user);
                            analitycsGoogle.logLoginGoogleEvent(true, user.getDisplayName(), user.getUid(), foto, user.getEmail(), user.getPhoneNumber(), Constantes.CONTROLE_VERSAO_USUARIO);
                            updateUI(user);
                        } else {
                            Log.d("TesteLogin", "GoogleErro");
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
//        Timber.d("UpdateUI");
        if (firebaseUser != null) {
//            Log.d("TesteLogin", "UpdateUISucesso");
            //tirar container login e subir a lista de produtos

            carregarFotoPerfil();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isDeviceOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void telaInicialLoadding(FeedPrincipalObj feedObj) {
        if(feedObj != null) return;
        mListMercadorias.setVisibility(View.GONE);
        //efabCart.setVisibility(View.GONE);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    private void telaInicialErro(String erro) {
        mListMercadorias.setVisibility(View.GONE);
        //efabCart.setVisibility(View.GONE);

        tvErro.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        tvErro.setText(erro);
    }

    private void showScreenMain(FeedPrincipalObj feedObj) {
        feedPrincipalObj = feedObj;

        AdapterLancamentos adapterLancamentos = new AdapterLancamentos(feedPrincipalObj.getAtualizacoesProds(), getActivity(), FragmentMain.this);
        rv_novidades.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_novidades.setAdapter(adapterLancamentos);

        nome_top_vend_1.setText(feedPrincipalObj.getTopRevendedores().get(0).getNomeRevendedor());
        nome_top_vend_2.setText(feedPrincipalObj.getTopRevendedores().get(1).getNomeRevendedor());
        nome_top_vend_3.setText(feedPrincipalObj.getTopRevendedores().get(2).getNomeRevendedor());
        nome_top_vend_4.setText(feedPrincipalObj.getTopRevendedores().get(3).getNomeRevendedor());
        nome_top_vend_5.setText(feedPrincipalObj.getTopRevendedores().get(4).getNomeRevendedor());
        nome_top_vend_6.setText(feedPrincipalObj.getTopRevendedores().get(5).getNomeRevendedor());

        if(feedPrincipalObj.getTopAdms() != null) {
            nome_top_adm_1.setText(feedPrincipalObj.getTopAdms().get(0).getNomeRevendedor());
            nome_top_adm_2.setText(feedPrincipalObj.getTopAdms().get(1).getNomeRevendedor());
            nome_top_adm_3.setText(feedPrincipalObj.getTopAdms().get(2).getNomeRevendedor());
        }


        AdapterEmAlta adapterEmAlta = new AdapterEmAlta(getActivity(), FragmentMain.this, feedPrincipalObj.getTopProdutos());
        rv_em_alta.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_em_alta.setAdapter(adapterEmAlta);

        AdapterEmAlta adapterMaisVendidos = new AdapterEmAlta(getActivity(), FragmentMain.this, feedPrincipalObj.getTopMaisVendidos());
        rv_mais_vendidos.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rv_mais_vendidos.setAdapter(adapterMaisVendidos);

        container_resumo_principal.setVisibility(View.VISIBLE);
    }

    private void telaInicialSucesso() {
        mListMercadorias.setVisibility(View.VISIBLE);

        //efabCart.setVisibility(View.VISIBLE);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        scrol_main.scrollTo(0,0);
    }


    @Override
    public void clickCategoria(ArrayList<ProdObj> produtos) {
        int colunas = 2;

        if(produtos.size() > 100) colunas = 3;
        if(produtos.size() > 180) colunas = 4;
        AdapterProdutos adapterProdutos = new AdapterProdutos(this, getActivity(), produtos, true, colunas);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(colunas, StaggeredGridLayoutManager.VERTICAL);
        mListMercadorias.scrollTo(0,0);
        mListMercadorias.setLayoutManager(layoutManager);
        mListMercadorias.setAdapter(adapterProdutos);

    }

    @Override
    public void clickProduto(ProdObj obj) {
        ProdObjParcelable objParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores());
        Intent intent = new Intent(getActivity(), ProdutoRevendaActivity.class);
        ArrayList<ProdObjParcelable> relacionados = new ArrayList<>();
        String st = obj.getProdName().substring(0, 3);
        for (int x = 0; x < prodObjs.size(); x++) {
            Log.d("Teste123", String.valueOf(x));
            String nomeProd = prodObjs.get(x).getProdName().toLowerCase();
            boolean palavraIdentica = nomeProd.equals(st.toLowerCase());
            boolean palavraParecida = nomeProd.contains(st.toLowerCase());
            boolean palavraChaveExiste = prodObjs.get(x).getTag().containsKey(st.toLowerCase());

            if (palavraChaveExiste || palavraIdentica || palavraParecida) {
                ProdObj obj1 = prodObjs.get(x);
                ProdObjParcelable objParc = new ProdObjParcelable(obj1.getCategorias(), obj1.getDescr(),obj1.isDisponivel(), obj1.getIdProduto(), obj1.getImgCapa(),obj1.getImagens() ,obj1.getFabricante(), obj1.getNivel(), obj1.getProdName(), obj1.getProdValor(), obj1.getValorAntigo(), obj1.isPromocional(), obj1.getTag(), obj1.getFornecedores(), obj1.getQuantidade(), obj1.getComissao(), obj1.getCores());
                if (obj.getIdProduto() != obj1.getIdProduto()) {
                    relacionados.add(objParc);
                }
                //Log.d("Teste123", String.valueOf(x) + " adicionada a liste");
                //Log.d("Teste123", resultadoPesquisa.toString());
            }
        }
        intent.putExtra("prod", objParcelable);
        //intent.putParcelableArrayListExtra("relacionados" , relacionados);
        startActivity(intent);
    }


    @Override
    public void openDetalhe(ProdObj obj) {
        ProdObjParcelable objParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores());
        Intent intent = new Intent(getActivity(), ProdutoRevendaActivity.class);
        ArrayList<ProdObjParcelable> relacionados = new ArrayList<>();
        String st = obj.getProdName().substring(0, 3);
        for (int x = 0; x < prodObjs.size(); x++) {
            Log.d("Teste123", String.valueOf(x));
            String nomeProd = prodObjs.get(x).getProdName().toLowerCase();
            boolean palavraIdentica = nomeProd.equals(st.toLowerCase());
            boolean palavraParecida = nomeProd.contains(st.toLowerCase());
            boolean palavraChaveExiste = prodObjs.get(x).getTag().containsKey(st.toLowerCase());

            if (palavraChaveExiste || palavraIdentica || palavraParecida) {
                ProdObj obj1 = prodObjs.get(x);
                ProdObjParcelable objParc = new ProdObjParcelable(obj1.getCategorias(), obj1.getDescr(),obj1.isDisponivel(), obj1.getIdProduto(), obj1.getImgCapa(),obj1.getImagens() ,obj1.getFabricante(), obj1.getNivel(), obj1.getProdName(), obj1.getProdValor(), obj1.getValorAntigo(), obj1.isPromocional(), obj1.getTag(), obj1.getFornecedores(), obj1.getQuantidade(), obj1.getComissao(), obj1.getCores());
                if (obj.getIdProduto() != obj1.getIdProduto()) {
                    relacionados.add(objParc);
                }
                //Log.d("Teste123", String.valueOf(x) + " adicionada a liste");
                //Log.d("Teste123", resultadoPesquisa.toString());
            }
        }
        intent.putExtra("prod", objParcelable);
        //intent.putParcelableArrayListExtra("relacionados" , relacionados);
        startActivity(intent);
    }

    @Override
    public void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj) {

    }

    @Override
    public void categoria(int categ) {

    }

    @Override
    public void adm() {

    }

    @Override
    public void openChat() {

    }

    @Override
    public void abriProduto(ProdutoObj prod) {
        Intent intent = new Intent(getActivity(), ProdutoRevendaActivity.class);
        intent.putExtra("id", prod.getIdProduto());
        startActivity(intent);
    }

    @Override
    public void verDetalhesProd(String id) {
        Intent intent = new Intent(getActivity(), ProdutoRevendaActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}


//fazer compra do proprio anuncio