package com.rapha.vendafavorita;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.objects.SolicitacaoRevendedor;

import static com.rapha.vendafavorita.FragmentMain.user;

public class CadastroRevendedorActivity extends AppCompatActivity {

    private LinearLayout bt;
    private ProgressBar pb;
    private TextInputEditText etNome, etWhatsapp, etObs;
    private String nomeString, whatsappString, obsString;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_revendedor);
        etNome = (TextInputEditText) findViewById(R.id.et_nome_cadastro_revenda);
        etWhatsapp = (TextInputEditText) findViewById(R.id.et_whats_cadastro_revendedor);
        etObs = (TextInputEditText) findViewById(R.id.et_obs_cadastro_revendedor);
        bt = (LinearLayout) findViewById(R.id.efab_cadrastro_revenda);
        pb = (ProgressBar) findViewById(R.id.pb_cadastro_revenda);

        firestore = FirebaseFirestore.getInstance();

        etNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nomeString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etWhatsapp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                whatsappString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etObs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                obsString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
    }

    private boolean verificarCampos() {
        if (nomeString.length() < 1) {
            Toast.makeText(this, "Insira seu nome", Toast.LENGTH_LONG).show();
            return false;
        }
        if (whatsappString.length() < 1) {
            Toast.makeText(this, "Insira seu whatsapp", Toast.LENGTH_LONG).show();
            return false;
        }
        if (obsString.length() < 1) {
            Toast.makeText(this, "Precisamos saber mais sobre você...", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void cadastrar() {
        if (!verificarCampos()) {
            return;
        }

        pb.setVisibility(View.VISIBLE);
        bt.setVisibility(View.GONE);

        SolicitacaoRevendedor obj = new SolicitacaoRevendedor(user.getEmail(), user.getPhotoUrl().getPath(), nomeString, whatsappString, obsString, user.getDisplayName(), System.currentTimeMillis(), user.getUid(), 0);

        DocumentReference doc = firestore.collection("Revendedores").document("amacompras").collection("ativos").document(user.getUid());
        doc.set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CadastroRevendedorActivity.this, "Cadastro realizado com sucesso. Aguarde o acesso ser liberado", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bt.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        });

    }

    public void fechar(View view) {
        onBackPressed();
    }
}
