package com.rapha.vendafavorita.rankings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.DateFormatacao;
import com.rapha.vendafavorita.R;

import java.util.Date;

public class RankingCreator extends AppCompatActivity {

    private TextInputEditText etTitulo, etPremio, etRegras, etDescricao, etMeta, etVencimento;

    private LinearLayout bt_criar_ranking;

    private ProgressBar progressBar;

    private Toast mToast = null;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_creator);
        etTitulo = (TextInputEditText) findViewById(R.id.et_titulo_ranking);
        etPremio = (TextInputEditText) findViewById(R.id.et_premio_ranking);
        etRegras = (TextInputEditText) findViewById(R.id.et_regras_ranking);
        etDescricao = (TextInputEditText) findViewById(R.id.et_descricao_ranking);
        etMeta = (TextInputEditText) findViewById(R.id.et_meta_ranking);
        etVencimento = (TextInputEditText) findViewById(R.id.et_vencimento_ranking);
        bt_criar_ranking = (LinearLayout) findViewById(R.id.bt_criar_ranking);

        progressBar = (ProgressBar) findViewById(R.id.pb_criar_ranking);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        bt_criar_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarRanking();
            }
        });

    }

    private void criarRanking() {

        RankingObj obj = getDadosRanking();

        if (obj == null) return;

        progressBar.setVisibility(View.VISIBLE);

        DocumentReference documentReference = firestore.collection("ranking").document();
        documentReference.set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });


    }

    private RankingObj getDadosRanking() {
        long time = System.currentTimeMillis();
        String inicioString = DateFormatacao.getDiaString(new Date(time));

        String fimString = pegarTexto(etVencimento);
        if (fimString == null) {
            toast("Insira o dia da premiação");
            return null;
        }

        String titulo = pegarTexto(etTitulo);
        if (titulo == null) {
            toast("Insira um titulo");
            return null;
        }

        String premio = pegarTexto(etPremio);
        if (premio == null) {
            toast("Insira um prêmio");
            return null;
        }

        String regras = pegarTexto(etRegras);
        if (regras == null) {
            toast("Insira as regras");
            return null;
        }

        String descricao = pegarTexto(etDescricao);
        if (descricao == null) {
            toast("Insira uma descrição");
            return null;
        }

        String meta = pegarTexto(etMeta);
        if (meta == null) {
            toast("Qual meta de vendas ?");
            return null;
        }
        int metaItens = Integer.valueOf(meta);

        RankingObj rankingObj = new RankingObj(time, inicioString, fimString, titulo, descricao, regras, premio, 0, 0, 0, metaItens, time, inicioString, null, null, null);

        return rankingObj;
    }

    private String pegarTexto(TextInputEditText inputEditText) {
        String t = inputEditText.getText().toString();
        if (t.length() > 0) {
            return t;
        } else {
            return null;
        }
    }

    private void toast(String s) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }
}