package com.rapha.vendafavorita;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.adapter.AdapterComissoes;
import com.rapha.vendafavorita.adapter.ComissoesAfiliadosAdapter;
import com.rapha.vendafavorita.objects.ComissaoAfiliados;
import com.rapha.vendafavorita.objects.ObjectRevenda;

import java.util.ArrayList;

public class ComissoesActivity extends AppCompatActivity implements AdapterComissoes.ComissoesListener {

    private RecyclerView rv_comissoes, rv_comissoes_afiliados;
    private ProgressBar pb_comissoes;
    private TextView text_erro_comissao;
    private AdapterComissoes adapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    private String idUsuario, nome, path;
    private int total;
    private Query refRevenda;
    private ArrayList<ObjectRevenda> listaDeComissoes;
    private CollectionReference refComissoesAfiliados;

    private ArrayList<ComissaoAfiliados> listaDeComissaoAfiliados;
    private TextView total_a_receber_comissao;
    private ExtendedFloatingActionButton efab_pagar_comissoes;
    private TextView titulo_comissoes_revendas;
    private TextView titulo_comissoes_afiliados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comissoes);
        //activity usada pelo adm para ver um usuario especifico
        rv_comissoes = (RecyclerView) findViewById(R.id.rv_comissoes);
        rv_comissoes_afiliados = (RecyclerView) findViewById(R.id.rv_comissoes_afiliados);
        text_erro_comissao = (TextView) findViewById(R.id.text_erro_comissao);
        pb_comissoes = (ProgressBar) findViewById(R.id.pb_comissoes);
        total_a_receber_comissao = (TextView) findViewById(R.id.total_a_receber_comissao);
        titulo_comissoes_revendas = (TextView) findViewById(R.id.titulo_comissoes_revendas);
        titulo_comissoes_afiliados = (TextView) findViewById(R.id.titulo_comissoes_afiliados);
        efab_pagar_comissoes = (ExtendedFloatingActionButton) findViewById(R.id.efab_pagar_comissoes);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        idUsuario = getIntent().getStringExtra("id");
        idUsuario = idUsuario == null ? auth.getUid() : idUsuario;
        nome = getIntent().getStringExtra("nome");
        path = getIntent().getStringExtra("path");

        Log.d("ComissoesActivity", "Id: " + idUsuario);

        refRevenda = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(idUsuario);

        refComissoesAfiliados = firebaseFirestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(idUsuario);

        getRevendas();

    }


    private void getRevendas() {

        //pb_comissoes.setVisibility(View.VISIBLE);

        refRevenda.orderBy("hora", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots != null) {

                    if (queryDocumentSnapshots.getDocuments().size() > 0) {


                        listaDeComissoes = new ArrayList<>();

                        total = 0;

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                            ObjectRevenda obj = queryDocumentSnapshots.getDocuments().get(i).toObject(ObjectRevenda.class);

                            if (obj.getStatusCompra() == 5) {
                                //listaDeComissoes.add(obj);
                                if (!obj.isPagamentoRecebido()) {
                                    total = total + obj.getComissaoTotal();
                                }
                            }

                            listaDeComissoes.add(obj);


                        }

                        getListComissoes();

                    } else {
                        getListComissoes();
                    }
                } else {
                    getListComissoes();
                }
            }
        });

    }


    private void getListComissoes() {

        refComissoesAfiliados.orderBy("hora", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                listaDeComissaoAfiliados = new ArrayList<>();

                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {



                    for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                        ComissaoAfiliados comissao = queryDocumentSnapshots.getDocuments().get(i).toObject(ComissaoAfiliados.class);

                        int comiss = comissao.getComissao();

                        if (comissao.getStatusComissao() == 5) {
                            //listaDeComissoes.add(obj);
                            if (!comissao.isPagamentoRecebido()) {
                                total = total + comiss;
                            }

                        }

                        listaDeComissaoAfiliados.add(comissao);

                    }


                    if (listaDeComissoes.size() > 0) {
                        comComissoesAfiliados();
                        comComissoesRevendas();
                    } else {
                        comComissoesAfiliados();
                        semComissoesRevendas();
                    }

                } else {

                    if (listaDeComissoes == null) {
                        semComissoesRevendas();
                        semComissoesAfiliados();
                    } else {

                        if (listaDeComissoes.size() > 0) {

                            //sem comissoes de afiliados mas possui revendas
                            semComissoesAfiliados();
                            comComissoesRevendas();

                        } else {
                            //sem comissoes de afiliados e sem revendas
                            semComissoesRevendas();
                            semComissoesAfiliados();
                        }

                    }


                }

                total_a_receber_comissao.setText(total + ",00");

                pb_comissoes.setVisibility(View.GONE);

                efab_pagar_comissoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pagarTudo(listaDeComissoes, listaDeComissaoAfiliados);
                    }
                });

            }
        });

    }


    private void semComissoesAfiliados() {
        titulo_comissoes_afiliados.setVisibility(View.GONE);
        rv_comissoes_afiliados.setVisibility(View.GONE);
        titulo_comissoes_afiliados.setText("Nenhuma comissão de afiliados");
    }

    private void comComissoesAfiliados() {

        ComissoesAfiliadosAdapter adapter = new ComissoesAfiliadosAdapter(this, listaDeComissaoAfiliados, idUsuario);
        rv_comissoes_afiliados.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv_comissoes_afiliados.setAdapter(adapter);

        titulo_comissoes_afiliados.setText("Comissões de afiliados: ");

        titulo_comissoes_afiliados.setVisibility(View.VISIBLE);
        rv_comissoes_afiliados.setVisibility(View.VISIBLE);
    }

    private void semComissoesRevendas() {
        rv_comissoes.setVisibility(View.GONE);
        titulo_comissoes_revendas.setVisibility(View.VISIBLE);
        titulo_comissoes_revendas.setText("Nenhuma revenda");
    }

    private void comComissoesRevendas() {
        adapter = new AdapterComissoes(ComissoesActivity.this, listaDeComissoes, ComissoesActivity.this);
        rv_comissoes.setLayoutManager(new LinearLayoutManager(ComissoesActivity.this));
        rv_comissoes.setAdapter(adapter);
        titulo_comissoes_revendas.setText("Minhas revendas:");
        titulo_comissoes_revendas.setVisibility(View.VISIBLE);
        rv_comissoes.setVisibility(View.VISIBLE);
    }


    private void listaCheia() {
        rv_comissoes.setVisibility(View.VISIBLE);
        text_erro_comissao.setVisibility(View.GONE);
        pb_comissoes.setVisibility(View.GONE);
    }

    private void listaVazia() {
        rv_comissoes.setVisibility(View.GONE);
        text_erro_comissao.setVisibility(View.VISIBLE);
        pb_comissoes.setVisibility(View.GONE);
    }

    @Override
    public void pagamento(boolean pago, String id) {

        WriteBatch batch = firebaseFirestore.batch();
        CollectionReference userRevendas = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(idUsuario);
        CollectionReference geralRevendas = firebaseFirestore.collection("Revendas");

        DocumentReference docRefUser = userRevendas.document(id);
        DocumentReference docRefGeral = geralRevendas.document(id);

        batch.update(docRefGeral, "pagamentoRecebido", pago);
        batch.update(docRefUser, "pagamentoRecebido", pago);

        batch.commit();
        getRevendas();
        Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();

    }


    private void pagarTudo(ArrayList<ObjectRevenda> listaDeComissoes, ArrayList<ComissaoAfiliados> comissaoAfiliados) {
        Log.d("ComissoesActivity", "Pagar tudo");
        if (listaDeComissoes.size() == 0) {
            Log.d("ComissoesActivity", "lista de comissoes vazia");
            return;
        }

        WriteBatch batch = firebaseFirestore.batch();
        CollectionReference userRevendas = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(idUsuario);
        CollectionReference geralRevendas = firebaseFirestore.collection("Revendas");

        CollectionReference comissaoRef = firebaseFirestore.collection("ComissoesAfiliados");
        CollectionReference minhaComissaoRef = firebaseFirestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(idUsuario);

        for (int i = 0; i < listaDeComissoes.size(); i++) {

            ObjectRevenda obj = listaDeComissoes.get(i);

            if (obj.getStatusCompra() == 5 && !obj.isPagamentoRecebido()) {

                DocumentReference docRefUser = userRevendas.document(obj.getIdCompra());
                DocumentReference docRefGeral = geralRevendas.document(obj.getIdCompra());

                batch.update(docRefGeral, "pagamentoRecebido", true);
                batch.update(docRefUser, "pagamentoRecebido", true);

            }
        }

        for (int i = 0; i < listaDeComissaoAfiliados.size(); i++) {

            ComissaoAfiliados comissAfl = listaDeComissaoAfiliados.get(i);

            if (comissAfl.getStatusComissao() == 5 && !comissAfl.isPagamentoRecebido()) {

                DocumentReference docRefUser = minhaComissaoRef.document(comissAfl.getId());
                DocumentReference docRefGeral = comissaoRef.document(comissAfl.getId());

                batch.update(docRefGeral, "pagamentoRecebido", true);
                batch.update(docRefUser, "pagamentoRecebido", true);

            }

        }

        batch.commit();
        getRevendas();
        Toast.makeText(this, "Pagou tudo com sucesso", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void click() {

    }

    private void updadeState (int state, ObjectRevenda obj) {
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference rev = firebaseFirestore.collection("Revendas").document(obj.getIdCompra());
        DocumentReference rev2 = firebaseFirestore.collection("MinhasRevendas").document("Usuario").collection(obj.getUidUserRevendedor()).document(obj.getIdCompra());


        batch.update(rev, "statusCompra", state);
        batch.update(rev2, "statusCompra", state);

        if (obj.isExisteComissaoAfiliados()) {

            DocumentReference comissaoRef = firebaseFirestore.collection("ComissoesAfiliados").document(obj.getIdCompra());
            DocumentReference minhaComissaoRef = firebaseFirestore.collection("MinhasComissoesAfiliados").document("Usuario").collection(obj.getUidAdm()).document(obj.getIdCompra());

            batch.update(comissaoRef, "statusComissao", state);
            batch.update(minhaComissaoRef, "statusComissao", state);
        }

        batch.commit();
        //getRevendas();
    }

    @Override
    public void cancelar(ObjectRevenda obj) {

        updadeState(3, obj);

    }

    @Override
    public void confirmar(ObjectRevenda obj) {
        updadeState(2, obj);
    }

    @Override
    public void entregar(ObjectRevenda obj) {
        updadeState(4, obj);
    }

    @Override
    public void concluir(ObjectRevenda obj) {
        updadeState(5, obj);
    }

}
