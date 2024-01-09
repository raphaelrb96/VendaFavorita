package com.rapha.vendafavorita;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.CompraFinalParcelable;
import com.rapha.vendafavorita.objects.UserStreamView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.FragmentMain.pathFotoUser;
import static com.rapha.vendafavorita.FragmentMain.user;
import static com.rapha.vendafavorita.MainActivity.ids;

public class CarrinhoActivity extends FragmentActivity implements AdapterCart.AnalizarClickPayFinal {

    private static final int REQUEST_CHECK_SETTINGS = 109;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 345;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private float DEFAULT_ZOOM = 17;
    private View btVoltar, closeHelp;
    private ProgressBar pb;

    private boolean proximidadeSelected = false;

    private String ruaMain = null;

    public static ArrayList<Float> valores = new ArrayList();
    private int somo = 0;

    private AdapterCart adapter;

    private TextView tv_nome_rua_cart, btAjuda;

    private LinearLayout content_layout_cart;

    public static ArrayList<CarComprasActivy> produtoss = new ArrayList<>();

    private RecyclerView rv;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Parcelable mCameraPosition;
    private Parcelable mCurrentLocation;
    private CoordinatorLayout coordinator_layout_cart;

    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private CollectionReference cart;
    private int precoEntrega = 0;
    private TickerView taxaEntregaTV, totalTV, ttcomprasTV;

    private TextInputEditText et_numero_casa;

    private FloatingActionButton fab;
    private FrameLayout containerHelp;
    private AnalitycsGoogle analitycsGoogle;
    private DocumentReference usuarioRef;
    private CollectionReference enderecosColecao;

    private boolean ATRASAR = false;
    private int numero = 15;
    private TextInputEditText et_complemento;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winPar = win.getAttributes();
        if (on) {
            winPar.flags |= bits;
        } else {
            winPar.flags &= ~bits;
        }
        win.setAttributes(winPar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        cart.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (querySnapshot == null) return;
                somo = 0;
                valores.clear();
                produtoss.clear();
                for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                    CarComprasActivy carComprasActivy = (CarComprasActivy) querySnapshot.getDocuments().get(i).toObject(CarComprasActivy.class);
                    valores.add(Float.valueOf(carComprasActivy.getValorTotal()));
                    produtoss.add(carComprasActivy);
                }
                for (int j = 0; j < valores.size(); j++) {
                    somo = (int) (((float) somo) + valores.get(j));
                }


                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(somo));
                stringBuilder.append(",00");
                totalTV.setText(stringBuilder.toString());
                ttcomprasTV.setText(String.valueOf(somo) + ",00");
                if (querySnapshot.getDocuments().size() == 0) {
//                    PayFinalActivity.this.emptyCar.setVisibility(0);
//                    PayFinalActivity.this.mPayList.setVisibility(8);
                    finish();
                } else {
//                    PayFinalActivity.this.emptyCar.setVisibility(8);
//                    PayFinalActivity.this.mPayList.setVisibility(0);
                }
                adapter.swapDados(produtoss);
            }
        });

        if (!ADMINISTRADOR) {
            analitycsGoogle.logUserVisitaCarrinhoEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
            UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
            firestore.collection("Eventos").document("stream").collection("cart").document(user.getUid()).set(userStreamView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        coordinator_layout_cart = (CoordinatorLayout) findViewById(R.id.coordinator_layout_cart);

        rv = (RecyclerView) findViewById(R.id.rv_cart);
        containerHelp = (FrameLayout) findViewById(R.id.container_help);
        fab = (FloatingActionButton) findViewById(R.id.fab_carrinho);
        tv_nome_rua_cart = (TextView) findViewById(R.id.tv_nome_rua_cart);
//        tv_exemplo_help = (TextView) findViewById(R.id.tv_exemplo_help);
        btAjuda = (TextView) findViewById(R.id.bt_mudar_endereco);
        taxaEntregaTV = (TickerView) findViewById(R.id.taxa_entrega);
        totalTV = (TickerView) findViewById(R.id.total_cart);
        ttcomprasTV = (TickerView) findViewById(R.id.total_compras);
        btVoltar = (View) findViewById(R.id.bt_voltar_cart);
        closeHelp = (View) findViewById(R.id.close_help);
        pb = (ProgressBar) findViewById(R.id.pb_cart);
        et_numero_casa = (TextInputEditText) findViewById(R.id.et_numero_casa);
        et_complemento = (TextInputEditText) findViewById(R.id.et_complemento);
        taxaEntregaTV.setCharacterLists(TickerUtils.provideNumberList());
        totalTV.setCharacterLists(TickerUtils.provideNumberList());
        ttcomprasTV.setCharacterLists(TickerUtils.provideNumberList());
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_cart);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        analitycsGoogle = new AnalitycsGoogle(this);
        usuarioRef = firestore.collection("Usuario").document(user.getUid());
        enderecosColecao = firestore.collection("Enderecos").document("Clientes").collection(user.getUid());
        cart = firestore.collection("carComprasActivy").document("usuario").collection(auth.getCurrentUser().getUid());

        content_layout_cart = (LinearLayout) findViewById(R.id.content_layout_cart);

        if (savedInstanceState != null) {
//            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        sheetBehavior = BottomSheetBehavior.from(content_layout_cart);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded
        rv.setLayoutManager(new LinearLayoutManager(CarrinhoActivity.this, RecyclerView.HORIZONTAL, false));
        adapter = new AdapterCart(CarrinhoActivity.this, CarrinhoActivity.this);
        rv.setAdapter(adapter);

        btAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerHelp.setVisibility(View.VISIBLE);

                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        et_numero_casa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    esconderTeclado(et_numero_casa);
                    return true;
                }
                return false;
            }
        });

        et_complemento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    esconderTeclado(et_complemento);
                    return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complementoString = et_complemento.getText().toString();
                String numeroString = et_numero_casa.getText().toString();
                if (numeroString.equals("")) {
                    Toast.makeText(CarrinhoActivity.this, "Insira o numero da casa", Toast.LENGTH_LONG).show();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    containerHelp.setVisibility(View.VISIBLE);
                    return;
                }

//                if (complementoString.equals("")) {
//                    Toast.makeText(CarrinhoActivity.this, "Insira o complemento no seu endereço", Toast.LENGTH_LONG).show();
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    return;
//                }
                Intent intent = new Intent(CarrinhoActivity.this, ConfirmarCompraActivity.class);
                if (somo != 0 && ruaMain != null) {

                    int itens = 0;

                    for (int i = 0; i < produtoss.size(); i++) {
                        int quant = produtoss.get(i).getQuantidade();
                        itens = itens + quant;
                    }

                    String sFinal = ruaMain + ", " + numeroString;

                    if (!complementoString.equals("")) {
                        sFinal = sFinal + " - " + complementoString;
                    }

                    CompraFinalParcelable cfp = new CompraFinalParcelable(sFinal, 0, 0, itens , user.getUid(), user.getDisplayName(), pathFotoUser, precoEntrega, somo);
                    intent.putExtra("cfp", cfp);
                    startActivity(intent);
                }
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        closeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerHelp.setVisibility(View.GONE);
            }
        });


    }

    private void esconderTeclado(TextInputEditText et) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(et.getWindowToken(), 0);
    }




    @Override
    public void aumentarQuantidade(CarComprasActivy carComprasActivy, String str, int i) {
        DocumentReference document = cart.document(str);
        WriteBatch batch = firestore.batch();
        batch.update(document, "quantidade", Integer.valueOf(carComprasActivy.getQuantidade() + 1));
        batch.update(document, "valorTotal", Float.valueOf(carComprasActivy.getValorTotal() + carComprasActivy.getValorUni()));
        batch.commit();
    }

    @Override
    public void diminuirQuantidade(CarComprasActivy carComprasActivy, String str, int i) {
        DocumentReference document = cart.document(str);
        if (carComprasActivy.getQuantidade() > 1) {
            WriteBatch batch = firestore.batch();
            batch.update(document, "quantidade", Integer.valueOf(carComprasActivy.getQuantidade() - 1));
            batch.update(document, "valorTotal", Float.valueOf(carComprasActivy.getValorTotal() - carComprasActivy.getValorUni()));
            batch.commit();
        }
    }

    @Override
    public void removerProduto(String str, int p) {
        DocumentReference reference = cart.document(str);
        if (ids.contains(str)) {
            ids.remove(str);
        }
        reference.delete();
        produtoss.remove(p);
        adapter.notifyItemRemoved(p);
        if (produtoss.size() == 0) {
            finish();
        }
    }

}
