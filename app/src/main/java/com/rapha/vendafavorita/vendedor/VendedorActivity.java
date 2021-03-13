package com.rapha.vendafavorita.vendedor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.ComissoesActivity;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.Usuario;

import java.util.ArrayList;
import java.util.Date;

public class VendedorActivity extends AppCompatActivity implements AdapterAfiliadosVendedor.AfiliadosVendedorListaner {

    private TextView tv_toolbar_vendedor, apelido_vendedor, nome_vendedor;
    private View bt_voltar_vendedor;
    private ImageView img_interface_vendedor, img_adm_vendedor;
    private TextView data_vendedor, numero_vendedor, apelido_adm_vendedor, nome_adm_vendedor;
    private ProgressBar pb_vendedor;
    private RecyclerView rv_afiliados_vendedor;
    private CardView bt_adm_vendedor;
    private FirebaseFirestore firestore;

    private String uidMain;
    private String apelidoMain;
    private FirebaseAuth auth;
    private Usuario usuario;
    private ExtendedFloatingActionButton efab_vendedor;
    private TextView tv_titulo_afiliados_vendedor;
    private View bt_delete_vendedor;
    private ArrayList<Usuario> afiliados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        tv_toolbar_vendedor = (TextView) findViewById(R.id.tv_toolbar_vendedor);
        tv_titulo_afiliados_vendedor = (TextView) findViewById(R.id.tv_titulo_afiliados_vendedor);
        nome_vendedor = (TextView) findViewById(R.id.nome_vendedor);
        apelido_vendedor = (TextView) findViewById(R.id.apelido_vendedor);
        data_vendedor = (TextView) findViewById(R.id.data_vendedor);
        numero_vendedor = (TextView) findViewById(R.id.numero_vendedor);
        apelido_adm_vendedor = (TextView) findViewById(R.id.apelido_adm_vendedor);
        nome_adm_vendedor = (TextView) findViewById(R.id.nome_adm_vendedor);
        bt_voltar_vendedor = (View) findViewById(R.id.bt_voltar_vendedor);
        bt_delete_vendedor = (View) findViewById(R.id.bt_delete_vendedor);
        img_interface_vendedor = (ImageView) findViewById(R.id.img_interface_vendedor);
        img_adm_vendedor = (ImageView) findViewById(R.id.img_adm_vendedor);
        pb_vendedor = (ProgressBar) findViewById(R.id.pb_vendedor);
        rv_afiliados_vendedor = (RecyclerView) findViewById(R.id.rv_afiliados_vendedor);
        bt_adm_vendedor = (CardView) findViewById(R.id.bt_adm_vendedor);
        efab_vendedor = (ExtendedFloatingActionButton) findViewById(R.id.efab_vendedor);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        apelidoMain = getIntent().getStringExtra("apelido");
        uidMain = getIntent().getStringExtra("uid");

        if (uidMain == null) {
            uidMain = auth.getUid();
        }

        if (apelidoMain == null) {
            apelidoMain = auth.getCurrentUser().getDisplayName();
        }
        tv_toolbar_vendedor.setText("@" + apelidoMain);

        bt_adm_vendedor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (usuario == null) return false;

                DocumentReference docAfiliado = firestore.collection("Usuario").document(uidMain);
                
                pb_vendedor.setVisibility(View.VISIBLE);

                docAfiliado.update("uidAdm", null, "usernameAdm", null, "nomeAdm", null, "pathFotoAdm", null, "admConfirmado", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });

                return false;
            }
        });

        bt_delete_vendedor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (afiliados == null) return false;

                if (afiliados.size() < 1) return false;


                DocumentReference docRevendedores = firestore.collection("Revendedores").document("amacompras").collection("ativos").document(uidMain);
                DocumentReference docUsuario = firestore.collection("Usuario").document(uidMain);
                WriteBatch batch = firestore.batch();

                for (int i = 0; i < afiliados.size(); i++) {
                    DocumentReference docAfiliado = firestore.collection("Usuario").document(afiliados.get(i).getUid());
                    batch.update(docAfiliado, "uidAdm", null, "usernameAdm", null, "nomeAdm", null, "pathFotoAdm", null, "admConfirmado", false);
                }

                batch.delete(docRevendedores);
                batch.delete(docUsuario);

                pb_vendedor.setVisibility(View.VISIBLE);

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });

                return false;
            }
        });


        bt_adm_vendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usuario == null) return;

                Intent intent = new Intent(VendedorActivity.this, VendedorActivity.class);

                intent.putExtra("uid", usuario.getUidAdm());

                startActivity(intent);
            }
        });

        efab_vendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usuario == null) return;

                Intent intent = new Intent(VendedorActivity.this, ComissoesActivity.class);

                intent.putExtra("id", usuario.getUid());
                intent.putExtra("nome", usuario.getNome());
                intent.putExtra("path", usuario.getPathFoto());
                intent.putExtra("zap", "");
                intent.putExtra("time", 0);

                startActivity(intent);
            }
        });


        firestore.collection("Usuario").document(uidMain).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    usuario = documentSnapshot.toObject(Usuario.class);
                    pb_vendedor.setVisibility(View.GONE);

                    apelido_vendedor.setText("@" + usuario.getUserName());
                    tv_toolbar_vendedor.setText("@" + usuario.getUserName());
                    nome_vendedor.setText(usuario.getNome());
                    data_vendedor.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuario.getPrimeiroLogin()), new Date()));
                    numero_vendedor.setText(usuario.getCelular());
                    Glide.with(VendedorActivity.this).load(usuario.getPathFoto()).into(img_interface_vendedor);

                    if (usuario.isAdmConfirmado()) {

                        Glide.with(VendedorActivity.this).load(usuario.getPathFotoAdm()).into(img_adm_vendedor);
                        apelido_adm_vendedor.setText("@" + usuario.getUsernameAdm());
                        nome_adm_vendedor.setText(usuario.getNomeAdm());


                    } else {
                        bt_adm_vendedor.setVisibility(View.GONE);
                    }

                    getAfiliados();

                }
            }
        });


    }

    private void getAfiliados() {

        firestore.collection("Usuario").whereEqualTo("uidAdm", uidMain).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {

                    if (queryDocumentSnapshots.getDocuments().size() > 0) {

                        afiliados = new ArrayList<>();

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                            Usuario user = queryDocumentSnapshots.getDocuments().get(i).toObject(Usuario.class);
                            afiliados.add(user);
                        }

                        rv_afiliados_vendedor.setLayoutManager(new LinearLayoutManager(VendedorActivity.this));
                        AdapterAfiliadosVendedor adpter = new AdapterAfiliadosVendedor(VendedorActivity.this, afiliados, VendedorActivity.this);
                        rv_afiliados_vendedor.setAdapter(adpter);

                        rv_afiliados_vendedor.setVisibility(View.VISIBLE);
                        tv_titulo_afiliados_vendedor.setVisibility(View.VISIBLE);
                        tv_titulo_afiliados_vendedor.setText(afiliados.size() + " Afiliados");

                    } else {
                        rv_afiliados_vendedor.setVisibility(View.GONE);
                        tv_titulo_afiliados_vendedor.setVisibility(View.GONE);
                    }

                } else {
                    rv_afiliados_vendedor.setVisibility(View.GONE);
                    tv_titulo_afiliados_vendedor.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public void verAfiliado(Usuario usuario) {
        Intent intent = new Intent(this, VendedorActivity.class);

        intent.putExtra("uid", usuario.getUid());

        startActivity(intent);
    }
}