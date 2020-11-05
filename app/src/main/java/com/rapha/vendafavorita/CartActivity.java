package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.analitycs.AnalitycsFacebook;
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

public class CartActivity extends AppCompatActivity implements AdapterCart.AnalizarClickPayFinal {

    private TickerView economiaTV, totalTV, itensTV;

    private View btVoltar, closeHelp;
    private ProgressBar pb;
    public static ArrayList<Float> valores = new ArrayList();
    private int somo = 0;

    public static ArrayList<CarComprasActivy> produtoss = new ArrayList<>();

    private RecyclerView rv;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private CollectionReference cart;

    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;
    private DocumentReference usuarioRef;
    private CollectionReference enderecosColecao;
    private AdapterCart adapter;

    private LinearLayout efabComprar;
    private TextView economia;
    private View volar_cart_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart);
        totalTV = (TickerView) findViewById(R.id.total_cart);
        economiaTV = (TickerView) findViewById(R.id.economia_cert);
        itensTV = (TickerView) findViewById(R.id.itens_cart);
        economia = (TextView) findViewById(R.id.tv_economia_cart);
        rv = (RecyclerView) findViewById(R.id.rv_cart_main);
        pb = (ProgressBar) findViewById(R.id.pb_cart_main);
        efabComprar = (LinearLayout) findViewById(R.id.efab_cart_main);

        volar_cart_main = (View) findViewById(R.id.volar_cart_main);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);
        usuarioRef = firestore.collection("Usuario").document(user.getUid());
        enderecosColecao = firestore.collection("Enderecos").document("Clientes").collection(user.getUid());
        cart = firestore.collection("carComprasActivy").document("usuario").collection(auth.getCurrentUser().getUid());
        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (produtoss !=null && ids !=null) {

                    int p = viewHolder.getAdapterPosition();
                    String str = produtoss.get(p).getIdProdut();
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
        };
        totalTV.setCharacterList(TickerUtils.getDefaultNumberList());
        economiaTV.setCharacterList(TickerUtils.getDefaultNumberList());
        itensTV.setCharacterList(TickerUtils.getDefaultNumberList());

        rv.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        adapter = new AdapterCart(CartActivity.this, CartActivity.this);
        rv.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        volar_cart_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        efabComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, ConfirmarCompraActivity.class);
                if (somo != 0) {

                    int itens = 0;

                    for (int i = 0; i < produtoss.size(); i++) {
                        int quant = produtoss.get(i).getQuantidade();
                        itens = itens + quant;
                    }


                    CompraFinalParcelable cfp = new CompraFinalParcelable("", 0, 0, itens , user.getUid(), user.getDisplayName(), pathFotoUser, 0, somo);
                    intent.putExtra("cfp", cfp);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cart.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                pb.setVisibility(View.GONE);
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

                itensTV.setText(String.valueOf(produtoss.size()));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(somo));
                stringBuilder.append(",00");
                totalTV.setText(stringBuilder.toString());

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
            analitycsFacebook.logUserVisitaCarrinhoEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
            analitycsGoogle.logUserVisitaCarrinhoEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
            UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
            firestore.collection("Eventos").document("stream").collection("cart").document(user.getUid()).set(userStreamView);
        }
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
