package com.rapha.vendafavorita.rankings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objectfeed.ObjectRecompensas;
import com.rapha.vendafavorita.objects.ObjectBonus;
import com.rapha.vendafavorita.rankings.adapters.AdapterResume;
import com.rapha.vendafavorita.rankings.objcts.RankingObj;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import static com.rapha.vendafavorita.DateFormatacao.getDiaString;

public class ResumeRankingActivity extends AppCompatActivity implements AdapterResume.ResumeRankingListener {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;


    private ProgressBar pb;

    private View voltar_ranking;
    private AdapterResume adapter;

    private NestedScrollView scroll_bonus;

    private TextView tv_titulo_bonus_scren, meta_recrutamento_2, meta_recrutamento_1, msg_erro_bonus,
            meta_vendas_1, meta_vendas_2, meta_vendas_3, meta_vendas_4, meta_vendas_5, meta_vendas_6,
            tv_bonus_vendas_1, tv_bonus_vendas_2, tv_bonus_vendas_13, tv_bonus_vendas_4, tv_bonus_vendas_5,
            tv_bonus_vendas_6, tv_bonus_recrutamento_1, tv_bonus_recrutamento_2, tv_regras_bonus_vendas,
            tv_regras_bonus_recrutamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_ranking);

        voltar_ranking = (View) findViewById(R.id.voltar_ranking_resume);
        scroll_bonus = (NestedScrollView) findViewById(R.id.scroll_bonus);
        pb = (ProgressBar) findViewById(R.id.pb_ranking);
        tv_titulo_bonus_scren = (TextView) findViewById(R.id.tv_titulo_bonus_scren);
        meta_recrutamento_2 = (TextView) findViewById(R.id.meta_recrutamento_2);
        meta_recrutamento_1 = (TextView) findViewById(R.id.meta_recrutamento_1);
        meta_recrutamento_1 = (TextView) findViewById(R.id.meta_recrutamento_1);
        meta_vendas_1 = (TextView) findViewById(R.id.meta_vendas_1);
        meta_vendas_2 = (TextView) findViewById(R.id.meta_vendas_2);
        meta_vendas_3 = (TextView) findViewById(R.id.meta_vendas_3);
        meta_vendas_4 = (TextView) findViewById(R.id.meta_vendas_4);
        meta_vendas_5 = (TextView) findViewById(R.id.meta_vendas_5);
        meta_vendas_6 = (TextView) findViewById(R.id.meta_vendas_6);
        tv_bonus_vendas_1 = (TextView) findViewById(R.id.tv_bonus_vendas_1);
        tv_bonus_vendas_2 = (TextView) findViewById(R.id.tv_bonus_vendas_2);
        tv_bonus_vendas_13 = (TextView) findViewById(R.id.tv_bonus_vendas_13);
        tv_bonus_vendas_4 = (TextView) findViewById(R.id.tv_bonus_vendas_4);
        tv_bonus_vendas_5 = (TextView) findViewById(R.id.tv_bonus_vendas_5);
        tv_bonus_vendas_6 = (TextView) findViewById(R.id.tv_bonus_vendas_6);
        tv_regras_bonus_vendas = (TextView) findViewById(R.id.tv_regras_bonus_vendas);
        tv_regras_bonus_recrutamento = (TextView) findViewById(R.id.tv_regras_bonus_recrutamento);
        tv_bonus_recrutamento_1 = (TextView) findViewById(R.id.tv_bonus_recrutamento_1);
        tv_bonus_recrutamento_2 = (TextView) findViewById(R.id.tv_bonus_recrutamento_2);
        msg_erro_bonus = (TextView) findViewById(R.id.msg_erro_bonus);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        firestore.collection("Feed").document("Bonus").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pb.setVisibility(View.GONE);

                if (documentSnapshot == null) {
                    msg_erro_bonus.setVisibility(View.VISIBLE);
                    scroll_bonus.setVisibility(View.GONE);

                } else {
                    ObjectRecompensas objBonus = documentSnapshot.toObject(ObjectRecompensas.class);
                    setView(objBonus);
                    scroll_bonus.setVisibility(View.VISIBLE);
                    msg_erro_bonus.setVisibility(View.GONE);

                    //showScreenMain(documentSnapshot);
                }
            }
        });

        Log.d("RankingInterface", "onCreate");

        voltar_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void setView(ObjectRecompensas objBonus) {
        String dataString = getDiaString(new Date());
        dataString = "Recompensas de Hoje\n" + dataString;
        tv_titulo_bonus_scren.setText(dataString);

        ArrayList<ObjectBonus> recompensasVendas = objBonus.getVendas();

        meta_vendas_3.setText(recompensasVendas.get(3).getMeta());
        meta_vendas_4.setText(recompensasVendas.get(2).getMeta());
        meta_vendas_5.setText(recompensasVendas.get(1).getMeta());
        meta_vendas_6.setText(recompensasVendas.get(0).getMeta());

        tv_bonus_vendas_13.setText(recompensasVendas.get(3).getBonus());
        tv_bonus_vendas_4.setText(recompensasVendas.get(2).getBonus());
        tv_bonus_vendas_5.setText(recompensasVendas.get(1).getBonus());
        tv_bonus_vendas_6.setText(recompensasVendas.get(0).getBonus());

        ArrayList<ObjectBonus> recompensasRecrutamento = objBonus.getRecrutamento();
        meta_recrutamento_1.setText(recompensasRecrutamento.get(1).getMeta());
        meta_recrutamento_2.setText(recompensasRecrutamento.get(0).getMeta());
        tv_bonus_recrutamento_1.setText(recompensasRecrutamento.get(1).getBonus());
        tv_bonus_recrutamento_2.setText(recompensasRecrutamento.get(0).getBonus());

        tv_regras_bonus_vendas.setText(objBonus.getRegrasVendas());
        tv_regras_bonus_recrutamento.setText(objBonus.getRegrasRecrutamento());
    }

    @Override
    public void printar(String x) {
        Log.d("RankingInterface", x);
    }
}