package com.rapha.vendafavorita.rankings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.rankings.adapters.AdapterRankingList;
import com.rapha.vendafavorita.rankings.objcts.RankingObj;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class RankingListActivity extends AppCompatActivity implements AdapterRankingList.RankingListener {

    private LinearLayout bt_criar_ranking;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private RecyclerView rv;
    private ArrayList<RankingObj> rankings;
    private AdapterRankingList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        bt_criar_ranking = (LinearLayout) findViewById(R.id.bt_criar_ranking);
        rv = (RecyclerView) findViewById(R.id.rv_ranking_list);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        CollectionReference ref = firestore.collection("ranking");

        ref.orderBy("inicio", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {

                    rankings = new ArrayList<>();

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        RankingObj o = queryDocumentSnapshots.getDocuments().get(i).toObject(RankingObj.class);
                        rankings.add(o);
                    }

                    rv.setLayoutManager(new LinearLayoutManager(RankingListActivity.this));
                    adapter = new AdapterRankingList(RankingListActivity.this, rankings, RankingListActivity.this);
                    rv.setAdapter(adapter);

                }
            }
        });

        bt_criar_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankingListActivity.this, RankingCreator.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void verRanking(RankingObj rankingObj) {

        Log.d("Ranking", "ver " + rankingObj.getTitulo());
        Intent intent = new Intent(this, RankingDetalheAdm.class);

        intent.putExtra("id", rankingObj.getId());

        startActivity(intent);

    }

    @Override
    public void excluir(String id, int p) {
        DocumentReference documentReference = firestore.collection("ranking").document(id);
        rankings.remove(p);
        adapter.notifyDataSetChanged();
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RankingListActivity.this, "Excluida com sucesso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void concluir(String id) {
        DocumentReference documentReference = firestore.collection("ranking").document(id);
        documentReference.update("ativa", false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RankingListActivity.this, "Concluida com sucesso", Toast.LENGTH_SHORT).show();
            }
        });
    }
}