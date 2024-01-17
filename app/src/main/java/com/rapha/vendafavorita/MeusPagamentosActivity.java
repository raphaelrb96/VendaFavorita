package com.rapha.vendafavorita;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rapha.vendafavorita.adapter.AdapterSaques;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.SaquesObj;
import com.rapha.vendafavorita.util.FormatoString;
import com.rapha.vendafavorita.util.Status;

import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;

public class MeusPagamentosActivity extends AppCompatActivity {

    private int finalTotal = 0;
    private ArrayList<SaquesObj> historicoPagamentos;
    private ArrayList<SaquesObj> emAndamento;
    private String uid;
    private String userName;
    private String pathFoto;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    private QuerySnapshot querySnapshot;
    private AnalitycsGoogle analitycsGoogle;


    private ImageView gif_retirar_dinheiro, gif_aguardar_dinheiro;
    private LinearLayout container_saque_em_andamento, container_solicitar_saque, container_historico_saques;
    private TextView total_disponivel_carteira;
    private TextView total_em_andamento, previsao_pagamento;
    private MaterialButton btn_status_em_andamento, btn_solicitar_saque_carteira;
    private TextInputEditText et_chave_meus_pagamentos, et_banco_meus_pagamentos, et_nome_meus_pagamentos;
    private RecyclerView rv_historico_saques;
    private ProgressBar pb_pagamentos;
    private CollectionReference refPagamentos;
    private Toast mToast = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pagamentos);

        pb_pagamentos = (ProgressBar) findViewById(R.id.pb_pagamentos);
        rv_historico_saques = (RecyclerView) findViewById(R.id.rv_historico_saques);
        container_historico_saques = (LinearLayout) findViewById(R.id.container_historico_saques);


        container_solicitar_saque = (LinearLayout) findViewById(R.id.container_solicitar_saque);
        gif_retirar_dinheiro = (ImageView) findViewById(R.id.gif_retirar_dinheiro);
        total_disponivel_carteira = (TextView) findViewById(R.id.total_disponivel_carteira);
        et_chave_meus_pagamentos = (TextInputEditText) findViewById(R.id.et_chave_meus_pagamentos);
        et_banco_meus_pagamentos = (TextInputEditText) findViewById(R.id.et_banco_meus_pagamentos);
        et_nome_meus_pagamentos = (TextInputEditText) findViewById(R.id.et_nome_meus_pagamentos);
        btn_solicitar_saque_carteira = (MaterialButton) findViewById(R.id.btn_solicitar_saque_carteira);


        container_saque_em_andamento = (LinearLayout) findViewById(R.id.container_saque_em_andamento);
        gif_aguardar_dinheiro = (ImageView) findViewById(R.id.gif_aguardar_dinheiro);
        total_em_andamento = (TextView) findViewById(R.id.total_em_andamento);
        previsao_pagamento = (TextView) findViewById(R.id.previsao_pagamento);
        btn_status_em_andamento = (MaterialButton) findViewById(R.id.btn_status_em_andamento);


        firebaseFirestore = FirebaseFirestore.getInstance();
        analitycsGoogle = new AnalitycsGoogle(this);
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        userName = auth.getCurrentUser().getDisplayName();
        pathFoto = auth.getCurrentUser().getPhotoUrl().getPath();
        refPagamentos = firebaseFirestore.collection("Pagamentos").document("Usuario").collection(uid);


        Glide.with(this).asGif().diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(R.drawable.retirar_dinheiro).into(gif_retirar_dinheiro);
        Glide.with(this).asGif().diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(R.drawable.aguardar).into(gif_aguardar_dinheiro);

        finalTotal = getIntent().getIntExtra("total", 0);

        onListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refPagamentos.orderBy("timestampCreated", Query.Direction.DESCENDING).addSnapshotListener(this, (snapshots, error) -> {
            querySnapshot = snapshots;
            updateScreen();
        });
    }

    private void updateScreen() {
        pb_pagamentos.setVisibility(View.GONE);

        historicoPagamentos = new ArrayList<>();
        emAndamento = new ArrayList<>();

        if (querySnapshot == null) {
            emptyScreen();
            return;
        }

        if (querySnapshot.getDocuments().size() == 0) {
            emptyScreen();
            return;
        }

        for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
            SaquesObj saquesObj = (SaquesObj) querySnapshot.getDocuments().get(i).toObject(SaquesObj.class);
            if(Status.saqueEmAndamento(saquesObj.getStatus())) {
                emAndamento.add(saquesObj);
            } else {
                historicoPagamentos.add(saquesObj);
            }
        }

        showContent();

    }

    private void onListeners() {
        btn_solicitar_saque_carteira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarSaque();
            }
        });
    }

    private void showContent() {
        if(emAndamento.size() > 0) {
            hidenContainerSolicitacao();
        } else {
            showContainerSolicitacao();
        }

        showHistoricoList();
    }

    private void showHistoricoList() {
        rv_historico_saques.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        AdapterSaques adapterSaques = new AdapterSaques(historicoPagamentos, this);
        rv_historico_saques.setAdapter(adapterSaques);
        container_historico_saques.setVisibility(View.VISIBLE);
    }

    private void showContainerSolicitacao() {
        container_solicitar_saque.setVisibility(View.VISIBLE);
        container_saque_em_andamento.setVisibility(View.GONE);
        emptyInputs();
        total_disponivel_carteira.setText(FormatoString.formartarPreco(finalTotal) + ",00");
    }

    private void emptyInputs() {
        et_banco_meus_pagamentos.setText("");
        et_chave_meus_pagamentos.setText("");
        et_nome_meus_pagamentos.setText("");
    }

    private void hidenContainerSolicitacao() {
        container_solicitar_saque.setVisibility(View.GONE);
        container_saque_em_andamento.setVisibility(View.VISIBLE);
        total_em_andamento.setText(FormatoString.formartarPreco(finalTotal) + ",00");
        SaquesObj obj = emAndamento.get(0);
        int status = obj.getStatus();
        btn_status_em_andamento.setText(Status.getStatusSaque(status));
        if(obj.getPrevisao() != null && obj.getPrevisao().length() > 0) {
            previsao_pagamento.setText(obj.getPrevisao());
            previsao_pagamento.setVisibility(View.VISIBLE);
        } else {
            previsao_pagamento.setVisibility(View.GONE);
        }
    }

    private void emptyScreen() {
        pb_pagamentos.setVisibility(View.GONE);
        container_historico_saques.setVisibility(View.GONE);
        showContainerSolicitacao();
    }

    private void solicitarSaque() {
        SaquesObj novoSaquesObj = getDadosAgendamento();
        if(novoSaquesObj == null) return;
        DocumentReference refSaque = refPagamentos.document();
        String refSaqueId = refSaque.getId();
        novoSaquesObj.setId(refSaqueId);

        refSaque.set(novoSaquesObj);
    }

    private SaquesObj getDadosAgendamento() {


        String nome = et_nome_meus_pagamentos.getText().toString();
        String chave = et_chave_meus_pagamentos.getText().toString();
        String banco = et_banco_meus_pagamentos.getText().toString();


        if (nome.length() < 5) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira o nome completo do Titular da Conta", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (chave.length() < 5) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira sua chave pix", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (banco.length() < 5) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Insira seu banco", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (finalTotal < 1) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "Nenhum valor disponivel para Saque", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        return new SaquesObj("", System.currentTimeMillis(), 0, "", finalTotal, finalTotal, 1, chave, banco, nome);
    }

    public void voltar(View view) {
        onBackPressed();
    }
}