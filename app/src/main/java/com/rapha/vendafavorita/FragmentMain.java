package com.rapha.vendafavorita;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rapha.vendafavorita.adapter.AdapterEmAlta;
import com.rapha.vendafavorita.adapter.AdapterInterfaceMain;
import com.rapha.vendafavorita.adapter.AdapterLancamentos;
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objectfeed.ProdutoObj;
import com.rapha.vendafavorita.objects.FeedPrincipalObj;
import com.rapha.vendafavorita.objects.TokenFcm;
import com.rapha.vendafavorita.objects.UserStreamView;
import com.rapha.vendafavorita.objects.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.rapha.vendafavorita.MainActivity.ids;

public class FragmentMain extends Fragment implements FacebookCallback<LoginResult>, AdapterInterfaceMain.ListenerPrincipal, AdapterProdutos.ClickProdutoCliente, AdapterLancamentos.HandlerProdAtalho, AdapterEmAlta.EmaltaListener {

    private AnalitycsGoogle analitycsGoogle;
    private AnalitycsFacebook analitycsFacebook;

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
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser user = null;
    public static String pathFotoUser = "";
    private ProgressBar pb;
    private ImageButton btPesquisar;
    private TextView tvErro;
    private LinearLayout toolbar;

    private CardView botao_topo;

    private FrameLayout scrol_main;

    private RecyclerView rv_novidades, rv_em_alta;

    private LinearLayout btZap, btSair, btMensagem, btMinhasCompras;
    //private ExtendedFloatingActionButton btMeuCarrinho;
    private EditText etpesquisar;

    private FloatingActionButton efabCart;

    private TextView nome_top_vend_1, nome_top_vend_2, nome_top_vend_3;

    private View whatsappBt, faceBt, instaBt, chatBt, perfilBt;

    private int tipoReferencia = 0;
    private Query query;
    public static final boolean ADMINISTRADOR = true;
    private ImageView fundo;

    private FrameLayout bt_carrinho_revenda_main, bt_meu_perfil_main, bt_afiliados_main, bt_mensagem_main;
    private NestedScrollView lista_principal;
    private LinearLayout ll_bt_smart_watch;
    private LinearLayout ll_bt_caixa_som;
    private LinearLayout ll_bt_eletronicos;
    private LinearLayout ll_bt_salao;
    private LinearLayout ll_bt_video_game;
    private LinearLayout ll_bt_comp;
    private LinearLayout ll_bt_ferramentas;
    private LinearLayout ll_bt_brinquedos;
    private LinearLayout ll_bt_acc_tv, ll_bt_fones, ll_bt_oculos, ll_bt_microfones;
    private LinearLayout ll_bt_camera, ll_bt_mochilas, ll_bt_relogios;
    private LinearLayout ll_bt_automotivos;

    //private FrameLayout  bt_painel_revendedor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_v2, container, false);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        //whatsappBt = (View) view.findViewById(R.id.bt_whats);
        //faceBt = (View) view.findViewById(R.id.bt_face);
        //instaBt = (View) view.findViewById(R.id.bt_insta);

        chatBt = (View) view.findViewById(R.id.bt_chat);
        perfilBt = (View) view.findViewById(R.id.bt_perfil);

        botao_topo = (CardView) view.findViewById(R.id.botao_topo);

        scrol_main = (FrameLayout) view.findViewById(R.id.scrol_main);

        rv_novidades = (RecyclerView) view.findViewById(R.id.rv_novidades);
        rv_em_alta = (RecyclerView) view.findViewById(R.id.rv_em_alta);

        bt_carrinho_revenda_main = (FrameLayout) view.findViewById(R.id.bt_carrinho_revenda_main);
        bt_afiliados_main = (FrameLayout) view.findViewById(R.id.bt_afiliados_main);
        bt_mensagem_main = (FrameLayout) view.findViewById(R.id.bt_mensagem_main);
        bt_meu_perfil_main = (FrameLayout) view.findViewById(R.id.bt_meu_perfil_main);
        //mensagem_main = (FrameLayout) view.findViewById(R.id.mensagem_main);
        //bt_painel_revendedor = (FrameLayout) view.findViewById(R.id.bt_painel_revendedor);

        efabCart = (FloatingActionButton) view.findViewById(R.id.efab_main);

        categoriaFindView(view);

        mListMercadorias = (RecyclerView) view.findViewById(R.id.rv_fragment_main);
        toolbar = (LinearLayout) view.findViewById(R.id.toolbar_main);
        etpesquisar= (EditText) view.findViewById(R.id.et_pesquisar);
        btZap = (LinearLayout) view.findViewById(R.id.ll_bt_zap_menu);
        btSair = (LinearLayout) view.findViewById(R.id.ll_bt_sair);
        btMensagem = (LinearLayout) view.findViewById(R.id.ll_bt_mensagem_menu);
        btMinhasCompras = (LinearLayout) view.findViewById(R.id.ll_bt_minhas_compras_menu);

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


        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("237281954777-45kb6q3j06k2mejnef0cbfn3jpf46f43.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        LoginManager.getInstance().registerCallback(callbackManager, FragmentMain.this);

        ligarOuvintes();

        analitycsFacebook = new AnalitycsFacebook(getActivity());
        analitycsGoogle = new AnalitycsGoogle(getActivity());

//        telaInicialLoadding();
//        auth.addAuthStateListener(mAuthStateListener);




        telaInicialLoadding();
        auth.addAuthStateListener(mAuthStateListener);

        return view;
    }

    private void ligarOuvintes() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                auth = firebaseAuth;
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (ADMINISTRADOR) {
                        //startActivity(new Intent(getActivity(), AdmActivity.class));
                        //getActivity().finish();
                        //return;
                    }

                    onSignedInInitialize();

                    if (user.isAnonymous()) {
                        //efabCart.setVisibility(View.GONE);
                        //toggleBackContainer(false);
                        obterListaDeProdutos(tipoReferencia);
                        return;
                    } else {
                        //toggleBackContainer(true);
                        carregarFotoPerfil();
                        if (!ADMINISTRADOR) {
                            analitycsGoogle.logUserStreamViewEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
                            UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
                            firestore.collection("Eventos").document("stream").collection("app").document(user.getUid()).set(userStreamView);
                        }
                        obterListaDeProdutos(tipoReferencia);
                        //getListCart();
                        verificarUsuario();
                        getTokenNoificacoes();
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
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginMainActivity.class));
                        return;
                    } else {
                        //exibir interface vazia
                        telaInicialErro("Baixa conexão com a internet");
                    }
                }
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



        bt_carrinho_revenda_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ListaRevendaActivity.class);
                startActivity(intent);
            }
        });

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


        bt_mensagem_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ADMINISTRADOR) {
                    return;
                }

                startActivity(new Intent(getActivity(), MensagemDetalheActivity.class));
            }
        });

        perfilBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prodObjs.size() < 50) {

                    obterListaDeProdutos(tipoReferencia);
                    return;

                }
                mAdapter = new AdapterInterfaceMain(getActivity(), prodObjs, FragmentMain.this);
                mListMercadorias.setLayoutManager(new LinearLayoutManager(getActivity()));
                mListMercadorias.setAdapter(mAdapter);
                mListMercadorias.scrollTo(0,0);
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

    private void painelAdm() {
        if (ADMINISTRADOR) {
            startActivity(new Intent(getActivity(), AdmActivity.class));
        } else {
            startActivity(new Intent(getActivity(), MeuPerfilActivity.class));
        }
    }

    private void categoriaFindView(View view) {

        ll_bt_smart_watch = (LinearLayout) view.findViewById(R.id.ll_bt_smart_watch);
        ll_bt_caixa_som = (LinearLayout) view.findViewById(R.id.ll_bt_caixa_som);
        ll_bt_eletronicos = (LinearLayout) view.findViewById(R.id.ll_bt_eletronicos);
        ll_bt_salao = (LinearLayout) view.findViewById(R.id.ll_bt_salao);

        ll_bt_video_game = (LinearLayout) view.findViewById(R.id.ll_bt_video_game);
        ll_bt_comp = (LinearLayout) view.findViewById(R.id.ll_bt_comp);
        ll_bt_ferramentas = (LinearLayout) view.findViewById(R.id.ll_bt_ferramentas);
        ll_bt_brinquedos = (LinearLayout) view.findViewById(R.id.ll_bt_brinquedos);

        ll_bt_acc_tv = (LinearLayout) view.findViewById(R.id.ll_bt_acc_tv);
        ll_bt_fones = (LinearLayout) view.findViewById(R.id.ll_bt_fones);
        ll_bt_oculos = (LinearLayout) view.findViewById(R.id.ll_bt_oculos);
        ll_bt_microfones = (LinearLayout) view.findViewById(R.id.ll_bt_microfones);

        ll_bt_automotivos = (LinearLayout) view.findViewById(R.id.ll_bt_automotivos);
        ll_bt_relogios = (LinearLayout) view.findViewById(R.id.ll_bt_relogios);
        ll_bt_mochilas = (LinearLayout) view.findViewById(R.id.ll_bt_mochilas);
        ll_bt_camera = (LinearLayout) view.findViewById(R.id.ll_bt_camera);

    }

    private void categoriaListeners() {

        ll_bt_smart_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.1", true), false, "", -1);
            }
        });

        ll_bt_caixa_som.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.2", true), false, "", -1);
            }
        });

        ll_bt_eletronicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.3", true), false, "", -1);
            }
        });

        ll_bt_salao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.4", true), false, "", -1);
            }
        });

        //linha 2

        ll_bt_video_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.6", true), false, "", -1);
            }
        });

        ll_bt_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.7", true), false, "", -1);
            }
        });

        ll_bt_ferramentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.8", true), false, "", -1);
            }
        });

        ll_bt_brinquedos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.9", true), false, "", -1);
            }
        });

        // linha 3

        ll_bt_acc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.20", true), false, "", -1);
            }
        });

        ll_bt_fones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.11", true), false, "", -1);
            }
        });

        ll_bt_oculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.14", true), false, "", -1);
            }
        });

        ll_bt_microfones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.18", true), false, "", -1);
            }
        });

        //linha 4

        ll_bt_automotivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.5", true), false, "", -1);
            }
        });

        ll_bt_relogios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.10", true), false, "", -1);
            }
        });

        ll_bt_mochilas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.35", true), false, "", -1);
            }
        });

        ll_bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista_principal.scrollTo(0, 0);
                telaInicialLoadding();
                myQuery(firestore.collection("produtos").whereEqualTo("categorias.13", true), false, "", -1);
            }
        });

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

                    getTokenNoificacoes();

                    Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                    startActivity(intent);

                }

            }
        });
    }

    private void checkDadosUsuario(final String tokenAtual) {

        final DocumentReference usuarioRef = firestore.collection("Usuario").document(user.getUid());
        final DocumentReference admRef = firestore.collection("Adm").document(user.getUid());

        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {
                WriteBatch batch = firestore.batch();

                boolean usuarioExiste = false;

                if (ADMINISTRADOR) {
                    TokenFcm tokenFcmAdmin = new TokenFcm(tokenAtual, user.getDisplayName());
                    batch.set(admRef, tokenFcmAdmin);
                }

                if (pathFotoUser.equals("")) {
                    pathFotoUser = getFotoUser(user);
                }

                if (userDoc.exists()) {

                    usuarioExiste = true;

                    boolean mudarFoto = false;
                    boolean mudarToken = false;

                    Usuario usuarioObj = userDoc.toObject(Usuario.class);

                    documentoPrincipalDoUsuario = usuarioObj;

                    if(!pathFotoUser.equals(usuarioObj.getPathFoto())) {
                        mudarFoto = true;
                    }

                    if (!tokenAtual.equals(usuarioObj.getTokenFcm())) {
                        mudarToken = true;
                    }

                    if (mudarFoto && mudarToken) {
                        batch.update(usuarioRef, "tokenFcm", tokenAtual);
                        batch.update(usuarioRef, "pathFoto", pathFotoUser);
                    } else if (!mudarFoto && mudarToken){
                        batch.update(usuarioRef, "tokenFcm", tokenAtual);
                    } else if (!mudarToken && mudarFoto) {
                        batch.update(usuarioRef, "pathFoto", pathFotoUser);
                    }
                } else {

                    usuarioExiste = false;

                    //novo
                    String num = "";
                    String provedor = "Google";
                    if (user.getPhoneNumber() != null) {
                        num = user.getPhoneNumber();
                    }



                    if (user.getProviderId().equals("facebook.com")) {
                        provedor = "Facebook";
                    }
                    long time = System.currentTimeMillis();
                    Usuario noovoUsuario = new Usuario(user.getDisplayName(), user.getEmail(), num, Constantes.CONTROLE_VERSAO_USUARIO, user.getUid(), pathFotoUser, Constantes.USUARIO_TIPO_CLIENTE, provedor, time, time, tokenAtual, null, "", "", "", "", "", false);
                    batch.set(usuarioRef, noovoUsuario);


                }

                final boolean finalUsuarioExiste = usuarioExiste;
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getActivity(), user.getDisplayName() + ", Ok", Toast.LENGTH_LONG).show();

                        if (!finalUsuarioExiste) {
                            Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                            startActivity(intent);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getTokenNoificacoes() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        checkDadosUsuario(token);

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
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mAuthStateListener != null) {
            auth.removeAuthStateListener(this.mAuthStateListener);
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
            case 2:
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 3:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
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


    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException error) {

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
                        analitycsFacebook.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), true, sizeDoc + " resultado(s)");
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

                    AdapterProdutos adapterProdutos = new AdapterProdutos(FragmentMain.this, getActivity(), list, true);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    mListMercadorias.setLayoutManager(layoutManager);
                    mListMercadorias.setAdapter(adapterProdutos);

                    //mAdapter = new AdapterInterfaceMain(getActivity(), prodObjs, FragmentMain.this);
                    //mListMercadorias.setLayoutManager(new LinearLayoutManager(getActivity()));
                    //mListMercadorias.setAdapter(mAdapter);
                    telaInicialSucesso();

                    return;
                }



                if (isPesquisa && sizeDoc == 0){
                    Toast.makeText(getActivity(), "Nenhum resultado para sua pesquisa", Toast.LENGTH_LONG).show();
                    if (!ADMINISTRADOR) {
                        analitycsFacebook.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), false, sizeDoc + " resultado(s)");
                        analitycsGoogle.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), false);
                    }
                    obterListaDeProdutos(4);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }



    private void obterListaDeProdutos(int tipo) {

        telaInicialLoadding();
        lista_principal.scrollTo(0, 0);
        //myQuery(firestore.collection("produtos").whereEqualTo("disponivel", true), false, "", tipo);

        firestore.collection("Feed").document("Main").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pb.setVisibility(View.GONE);
                if (documentSnapshot == null) {

                } else {
                    FeedPrincipalObj feedPrincipalObj = documentSnapshot.toObject(FeedPrincipalObj.class);
                    AdapterLancamentos adapterLancamentos = new AdapterLancamentos(feedPrincipalObj.getAtualizacoesProds(), getActivity(), FragmentMain.this);
                    rv_novidades.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                    rv_novidades.setAdapter(adapterLancamentos);

                    nome_top_vend_1.setText(feedPrincipalObj.getTopRevendedores().get(0).getNomeRevendedor());
                    nome_top_vend_2.setText(feedPrincipalObj.getTopRevendedores().get(1).getNomeRevendedor());
                    nome_top_vend_3.setText(feedPrincipalObj.getTopRevendedores().get(2).getNomeRevendedor());

                    AdapterEmAlta adapterEmAlta = new AdapterEmAlta(getActivity(), FragmentMain.this, feedPrincipalObj.getTopProdutos());
                    rv_em_alta.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_em_alta.setAdapter(adapterEmAlta);
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
        telaInicialLoadding();
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
                            analitycsFacebook.logLoginGoogleEvent(true, user.getDisplayName(), user.getUid());
                            analitycsGoogle.logLoginGoogleEvent(true, user.getDisplayName(), user.getUid(), foto, user.getEmail(), user.getPhoneNumber(), Constantes.CONTROLE_VERSAO_USUARIO);
                            updateUI(user);
                        } else {
                            Log.d("TesteLogin", "GoogleErro");
                            updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            FirebaseUser user = authResult.getUser();
                            String foto = getFotoUser(user);
                            analitycsFacebook.logLoginFaceEvent(true, user.getDisplayName(),user.getUid());
                            analitycsGoogle.logLoginFaceEvent(true, user.getDisplayName(), user.getUid(), foto, user.getEmail(), user.getPhoneNumber(), Constantes.CONTROLE_VERSAO_USUARIO);
                            updateUI(user);
//                            Log.d("TesteLogin", "Facesucess");
                        } else {
                            updateUI(null);
//                            Log.d("TesteLogin", "Faceerro");
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

    private void telaInicialLoadding() {
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

    private void telaInicialSucesso() {
        mListMercadorias.setVisibility(View.VISIBLE);
        //efabCart.setVisibility(View.VISIBLE);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        scrol_main.scrollTo(0,0);
    }


    @Override
    public void clickCategoria(ArrayList<ProdObj> produtos) {
        AdapterProdutos adapterProdutos = new AdapterProdutos(this, getActivity(), produtos, true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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