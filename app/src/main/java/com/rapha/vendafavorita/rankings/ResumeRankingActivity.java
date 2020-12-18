package com.rapha.vendafavorita.rankings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.rankings.adapters.AdapterResume;
import com.rapha.vendafavorita.rankings.objcts.RankingObj;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ResumeRankingActivity extends AppCompatActivity implements AdapterResume.ResumeRankingListener {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private RecyclerView rv;

    private ProgressBar pb;

    private View voltar_ranking;
    private AdapterResume adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_ranking);

        rv = (RecyclerView) findViewById(R.id.rv_resume_ranking);
        voltar_ranking = (View) findViewById(R.id.voltar_ranking_resume);
        pb = (ProgressBar) findViewById(R.id.pb_ranking);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        CollectionReference ref = firestore.collection("ranking");

        Log.d("RankingInterface", "onCreate");

        voltar_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ref.orderBy("inicio", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pb.setVisibility(View.GONE);

                Log.d("RankingInterface", "onEvent");

                if (queryDocumentSnapshots == null) {

                    Log.d("RankingInterface", "nulo");
                    return;
                }

                if (queryDocumentSnapshots.size() > 0) {

                    ArrayList<RankingObj> rankings = new ArrayList<>();

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        RankingObj o = queryDocumentSnapshots.getDocuments().get(i).toObject(RankingObj.class);
                        if (o.isAtiva()) {
                            rankings.add(o);
                        }
                    }

                    Log.d("RankingInterface", rankings.size() + " ");

                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new AdapterResume(rankings,ResumeRankingActivity.this, auth.getUid(), ResumeRankingActivity.this);
                    rv.setAdapter(adapter);
                    rv.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    public void printar(String x) {
        Log.d("RankingInterface", x);
    }
}