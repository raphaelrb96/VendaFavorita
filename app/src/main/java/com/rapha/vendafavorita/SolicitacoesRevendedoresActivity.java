package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rapha.vendafavorita.adapter.AdapterSolicitacoesRevendas;
import com.rapha.vendafavorita.adapter.RevendedoresAtivosAdapter;
import com.rapha.vendafavorita.objects.SolicitacaoRevendedor;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class SolicitacoesRevendedoresActivity extends AppCompatActivity implements AdapterSolicitacoesRevendas.SolicitacoesClickListener, RevendedoresAtivosAdapter.RevendedorAtivoListener {

    private FirebaseFirestore firestore;
    private RecyclerView rv, rvAtivos;

    private AdapterSolicitacoesRevendas adapterSolicitacoesRevendas;
    private RevendedoresAtivosAdapter revendedoresAtivosAdapter;
    private ArrayList<SolicitacaoRevendedor> revendedores, revededoresAtivos;

    private ProgressBar pb;

    private TextView titulo_solicitacao_revendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacoes_revendedores);
        rv = (RecyclerView) findViewById(R.id.rv_solicitacoes_revendedores);
        pb = (ProgressBar) findViewById(R.id.pb_revendedor);
        rvAtivos = (RecyclerView) findViewById(R.id.rv_revendedores_ativos);
        titulo_solicitacao_revendedor = (TextView) findViewById(R.id.titulo_solicitacao_revendedor);
        pb.setVisibility(View.VISIBLE);
        firestore = FirebaseFirestore.getInstance();

        rvAtivos.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection("Revendedores").document("amacompras").collection("solicitacao").addSnapshotListener(SolicitacoesRevendedoresActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                firestore.collection("Revendedores").document("amacompras").collection("ativos").addSnapshotListener(SolicitacoesRevendedoresActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        pb.setVisibility(View.GONE);

                        if (queryDocumentSnapshots.size() == 0) {
                            return;
                        }

                        revededoresAtivos = new ArrayList<>();

                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                            SolicitacaoRevendedor rev = queryDocumentSnapshots.getDocuments().get(i).toObject(SolicitacaoRevendedor.class);
                            revededoresAtivos.add(rev);

                        }

                        revendedoresAtivosAdapter = new RevendedoresAtivosAdapter(revededoresAtivos, SolicitacoesRevendedoresActivity.this, SolicitacoesRevendedoresActivity.this);
                        rvAtivos.setAdapter(revendedoresAtivosAdapter);



                    }
                });

                if (queryDocumentSnapshots.size() == 0) {
                    titulo_solicitacao_revendedor.setVisibility(View.GONE);
                    return;
                }

                revendedores = new ArrayList<>();

                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {

                    SolicitacaoRevendedor rev = queryDocumentSnapshots.getDocuments().get(i).toObject(SolicitacaoRevendedor.class);
                    revendedores.add(rev);

                }

                adapterSolicitacoesRevendas = new AdapterSolicitacoesRevendas(revendedores, SolicitacoesRevendedoresActivity.this, SolicitacoesRevendedoresActivity.this);
                rv.setAdapter(adapterSolicitacoesRevendas);

            }
        });
    }

    @Override
    public void liberarRevendedor(SolicitacaoRevendedor solicitacaoRevendedor, int position) {
        WriteBatch batch = firestore.batch();

        DocumentReference ativos = firestore.collection("Revendedores").document("amacompras").collection("ativos").document(solicitacaoRevendedor.getUid());
        DocumentReference solicitacoes = firestore.collection("Revendedores").document("amacompras").collection("solicitacao").document(solicitacaoRevendedor.getUid());

        batch.delete(solicitacoes);
        batch.set(ativos, solicitacaoRevendedor);

        batch.commit();

        revendedores.remove(position);

        adapterSolicitacoesRevendas = new AdapterSolicitacoesRevendas(revendedores, SolicitacoesRevendedoresActivity.this, SolicitacoesRevendedoresActivity.this);
        rv.setAdapter(adapterSolicitacoesRevendas);

    }

    @Override
    public void verRevendedor(SolicitacaoRevendedor revendedor) {

        Intent intent = new Intent(this, ComissoesActivity.class);

        intent.putExtra("id", revendedor.getUid());
        intent.putExtra("nome", revendedor.getNome());
        intent.putExtra("path", revendedor.getFoto());
        intent.putExtra("zap", revendedor.getZap());
        intent.putExtra("time", revendedor.getTimestamp());

        startActivity(intent);
    }
}
