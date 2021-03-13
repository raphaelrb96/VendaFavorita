package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterCliente;
import com.rapha.vendafavorita.adapter.AdapterTopAdm;
import com.rapha.vendafavorita.objects.TopAdms;
import com.rapha.vendafavorita.objects.Usuario;
import com.rapha.vendafavorita.objects.UsuarioParcelable;
import com.rapha.vendafavorita.vendedor.VendedorActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nullable;


public class ClientesActivity extends AppCompatActivity implements AdapterCliente.ClienteListener, AdapterTopAdm.AdmListener {

    private FirebaseFirestore firestore;
    private RecyclerView rv, rv_top_adms;

    private AdapterCliente adapterCliente;
    private View voltar_clientes;
    private EditText et_pesquisar_cliente;
    private ImageButton bt_pesquisar_cliente;

    private boolean pesquisa = false;
    private TextView tv_titulo_top_adms, tv_titulo_vendedores;
    private ProgressBar pb_vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        rv = (RecyclerView) findViewById(R.id.rv_clientes);
        rv_top_adms = (RecyclerView) findViewById(R.id.rv_top_adms);
        voltar_clientes = (View) findViewById(R.id.voltar_clientes);
        pb_vendedor = (ProgressBar) findViewById(R.id.pb_vendedor);

        tv_titulo_top_adms = (TextView) findViewById(R.id.tv_titulo_top_adms);
        tv_titulo_vendedores = (TextView) findViewById(R.id.tv_titulo_vendedores);

        et_pesquisar_cliente = (EditText) findViewById(R.id.et_pesquisar_cliente);
        bt_pesquisar_cliente = (ImageButton) findViewById(R.id.bt_pesquisar_cliente);

        rv.setLayoutManager(new GridLayoutManager(this, 2));

        firestore = FirebaseFirestore.getInstance();

        //colocar um pb


        firestore.collection("Usuario").orderBy("ultimoLogin", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) return;

                ArrayList<Usuario> usuarioArrayList = new ArrayList<>();
                ArrayList<TopAdms> adms = new ArrayList<>();


                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                    Usuario u = queryDocumentSnapshots.getDocuments().get(i).toObject(Usuario.class);
                    if (usuarioArrayList.size() < 100) {
                        usuarioArrayList.add(u);
                    }

                    if (u != null && u.isAdmConfirmado()) {

                        TopAdms adm = new TopAdms(u.getNomeAdm(), u.getPathFotoAdm(), u.getUidAdm(), 1);

                        if (adms.size() > 0) {

                            boolean admExiste = false;

                            for (int j = 0; j < adms.size(); j++) {
                                if (adm.getUidRevendedor().equals(adms.get(j).getUidRevendedor())) {
                                    admExiste = true;
                                    adms.get(j).setNumAfiliados(adms.get(j).getNumAfiliados() + 1);
                                    break;
                                }
                            }

                            if (!admExiste) {
                                if (adms.size() < 40) {
                                    adms.add(adm);
                                }
                            }

                        } else {
                            if (adms.size() < 40) {
                                adms.add(adm);
                            }
                        }

                    }

                }

                Collections.sort(adms, new Comparator<TopAdms>() {
                    @Override
                    public int compare(TopAdms topAdms, TopAdms t1) {
                        return Integer.compare(topAdms.getNumAfiliados(), t1.getNumAfiliados());
                    }
                });



                if (adms.size() > 0) {
                    rv_top_adms.setVisibility(View.VISIBLE);
                    tv_titulo_top_adms.setVisibility(View.VISIBLE);

                    Collections.reverse(adms);

                    AdapterTopAdm adapterTopAdm = new AdapterTopAdm(ClientesActivity.this, adms, ClientesActivity.this);
                    rv_top_adms.setLayoutManager(new LinearLayoutManager(ClientesActivity.this, RecyclerView.HORIZONTAL, false));
                    rv_top_adms.setAdapter(adapterTopAdm);

                } else {
                    rv_top_adms.setVisibility(View.GONE);
                    tv_titulo_top_adms.setVisibility(View.GONE);
                }

                adapterCliente = new AdapterCliente(usuarioArrayList,ClientesActivity.this, ClientesActivity.this);
                rv.setAdapter(adapterCliente);
                pb_vendedor.setVisibility(View.GONE);
            }
        });

        et_pesquisar_cliente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_pesquisar_cliente.setFocusable(true);
                et_pesquisar_cliente.setFocusableInTouchMode(true);
                return false;
            }
        });

        bt_pesquisar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pesquisa) {
                    return;
                }

                pb_vendedor.setVisibility(View.VISIBLE);
                rv_top_adms.setVisibility(View.GONE);
                tv_titulo_top_adms.setVisibility(View.GONE);

                pesquisa = true;
                String s = et_pesquisar_cliente.getText().toString().toLowerCase();
                firestore.collection("Usuario").whereEqualTo("userName", s).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        pesquisa = false;
                        if (queryDocumentSnapshots == null) return;

                        ArrayList<Usuario> usuarioArrayList = new ArrayList<>();

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                            Usuario u = queryDocumentSnapshots.getDocuments().get(i).toObject(Usuario.class);
                            usuarioArrayList.add(u);
                        }

                        adapterCliente = new AdapterCliente(usuarioArrayList,ClientesActivity.this, ClientesActivity.this);
                        rv.setAdapter(adapterCliente);
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void detalhesCliente(Usuario usuario) {
        //UsuarioParcelable up = new UsuarioParcelable(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm());
        //Intent intent = new Intent(ClientesActivity.this, ClienteDetalhesActivity.class);
        //intent.putExtra("user", up);
        //startActivity(intent);

        Intent intent = new Intent(this, VendedorActivity.class);

        intent.putExtra("uid", usuario.getUid());

        startActivity(intent);
    }

    @Override
    public void verAdm(String uid) {
        Intent intent = new Intent(this, VendedorActivity.class);

        intent.putExtra("uid", uid);

        startActivity(intent);
    }
}
