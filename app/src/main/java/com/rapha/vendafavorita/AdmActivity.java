package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.rapha.vendafavorita.adapter.RevendaActivity;
import com.rapha.vendafavorita.informacoes.AnalisarDadosActivity;
import com.rapha.vendafavorita.rankings.RankingListActivity;

import javax.annotation.Nullable;

public class AdmActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firestore;
    private DocumentReference statusRef;
    private TextView status;
    private ExtendedFloatingActionButton abrir_fechar;
    private boolean aberto = false;
    private boolean acessoRestrito = false;
    private LinearLayout container_3_menu_adm, container_2_menu_adm;
    private CardView bt_mensagem_menu_adm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm);
        status = (TextView) findViewById(R.id.tv_drogaria_aberta_fechada);
        bt_mensagem_menu_adm = (CardView) findViewById(R.id.bt_mensagem_menu_adm);
        container_3_menu_adm = (LinearLayout) findViewById(R.id.container_3_menu_adm);
        container_2_menu_adm = (LinearLayout) findViewById(R.id.container_2_menu_adm);
        abrir_fechar = (ExtendedFloatingActionButton) findViewById(R.id.efab_abrir_fechar);
        firestore = FirebaseFirestore.getInstance();
        if (acessoRestrito) {
            container_3_menu_adm.setVisibility(View.GONE);
            container_2_menu_adm.setVisibility(View.GONE);
            bt_mensagem_menu_adm.setVisibility(View.GONE);
        }
        statusRef = firestore.collection("statusDrogaria").document("chave");
        statusRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        aberto = documentSnapshot.toObject(StatusAbrirFecharLoja.class).isAberto();
                    }
                }

                if (aberto) {
                    status.setText("está aberta");
                } else {
                    status.setText("está fechada");
                }

                abrir_fechar.setOnClickListener(AdmActivity.this);

            }
        });
    }

    public void verProdutos(View view) {
        Intent intent = new Intent(this, InventarioActivity.class);
        startActivity(intent);
    }

    public void verMensagens(View view) {
        Intent intent = new Intent(this, MensagemActivity.class);
        startActivity(intent);
    }

    public void verVendas(View view) {
        Intent intent = new Intent(this, RankingListActivity.class);
        startActivity(intent);
    }

    public void verAnalytics(View view) {
        Intent intent = new Intent(this, AnalisarDadosActivity.class);
        startActivity(intent);
    }

    private void abrirFechar() {
        status.setText("Aguarde...");
        if (aberto) {
            StatusAbrirFecharLoja fecharr = new StatusAbrirFecharLoja(false);
            statusRef.set(fecharr);
        } else {
            StatusAbrirFecharLoja abrirr = new StatusAbrirFecharLoja(true);
            statusRef.set(abrirr);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.efab_abrir_fechar) {
            abrirFechar();
        }
    }

    public void verCupons(View view) {
    }

    public void verClientes(View view) {
        Intent intent = new Intent(this, ClientesActivity.class);
        startActivity(intent);
    }

    public void solicitacoesRevendedores(View view) {
        Intent intent = new Intent(this, SolicitacoesRevendedoresActivity.class);
        startActivity(intent);
    }

    public void verRevendas(View view) {
        Intent intent = new Intent(this, RevendaActivity.class);
        startActivity(intent);
    }

    public void verFaltas(View view) {
        Intent intent = new Intent(this, ListaDeFaltas.class);
        startActivity(intent);
    }
}

