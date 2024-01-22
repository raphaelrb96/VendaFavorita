package com.rapha.vendafavorita;

import static com.rapha.vendafavorita.FragmentMain.documentoPrincipalDoUsuario;
import static com.rapha.vendafavorita.FragmentMain.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rapha.vendafavorita.objects.Usuario;
import com.rapha.vendafavorita.util.Alertas;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Hours;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class UpgradeContaActivity extends AppCompatActivity {

    private ImageView img_up_conta, icon_up_conta;
    private TextView nivel_conta_upgrade, title_up_conta, text_up_conta;
    private ProgressBar pb_up_conta;
    private MaterialButton btn_up_conta;
    private View.OnClickListener listenerBtn;
    private FirebaseFirestore firestore;
    private DocumentReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_count);
        img_up_conta = (ImageView) findViewById(R.id.img_up_conta);
        icon_up_conta = (ImageView) findViewById(R.id.icon_up_conta);
        nivel_conta_upgrade = (TextView) findViewById(R.id.nivel_conta_upgrade);
        title_up_conta = (TextView) findViewById(R.id.tv_title_up_conta);
        text_up_conta = (TextView) findViewById(R.id.tv_text_up_conta);
        pb_up_conta = (ProgressBar) findViewById(R.id.pb_up_conta);
        btn_up_conta = (MaterialButton) findViewById(R.id.btn_up_conta);

        Glide.with(this).asGif().diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(R.drawable.carreira).into(img_up_conta);

        firestore = FirebaseFirestore.getInstance();

        onListeners();

        showContent();



    }

    private void onListeners() {
        listenerBtn = (View view) -> {
            if (user != null) {
                pb_up_conta.setVisibility(View.VISIBLE);
                btn_up_conta.setClickable(false);

                usuarioRef = firestore.collection("Usuario").document(user.getUid());
                usuarioRef.update("vipDiamante", true).addOnCompleteListener(this, task -> {

                    pb_up_conta.setVisibility(View.GONE);

                    if(task.isSuccessful()) {
                        //finish();
                        showContent();
                    } else {

                        btn_up_conta.setClickable(true);
                    }
                });

            }
        };
    }

    private void showContent() {



        if(documentoPrincipalDoUsuario != null) {

            String title = "Parabens!!";
            String nivel = "Iniciante";
            String subtitle = "Você foi convidado a se promover para VENDEDOR DIAMANTE!";
            int iconId = R.drawable.iniciante;

            if (documentoPrincipalDoUsuario.isVipDiamante()) {
                btn_up_conta.setVisibility(View.GONE);
                title = "Bem vindo!!";
                nivel = "Diamante";
                subtitle = "Você ja é um Vendedor Diamante";
                iconId = R.drawable.diamante;

            } else {

                btn_up_conta.setVisibility(View.VISIBLE);
                btn_up_conta.setOnClickListener(listenerBtn);

                Hours tempo = getTempo();
                int meses = (int) tempo.getHours()/730;
                int semanas = (int) tempo.getHours()/168;

                if(meses < 6) {
                    title = "Falta Pouco!!";
                    nivel = "Iniciante";
                    subtitle = semanas < 22 ? "Continue anunciando e em breve você alcançará o nível Diamante" : "Falta pouco menos de um mês para você ser Promovido!";
                    iconId = R.drawable.iniciante;
                    btnUpgradeOff();
                }

            }


            icon_up_conta.setImageDrawable(ContextCompat.getDrawable(this, iconId));
            title_up_conta.setText(title);
            text_up_conta.setText(subtitle);
            nivel_conta_upgrade.setText(nivel);

        } else {
            finish();
        }




    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private void btnUpgradeOff() {
        btn_up_conta.setVisibility(View.VISIBLE);
        btn_up_conta.setText("Indisponivel Agora");
        btn_up_conta.setClickable(false);
        btn_up_conta.setBackgroundColor(ContextCompat.getColor(this, R.color.back_rv));
        btn_up_conta.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cinza_text)));
        btn_up_conta.setTextColor(ContextCompat.getColor(this, R.color.cinza_text));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn_up_conta.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cinza_text)));
        }
    }

    private Hours getTempo() {
        Date inicio = new Date(documentoPrincipalDoUsuario.getPrimeiroLogin());
        Date fim = new Date();
        return Hours.hoursBetween(new DateTime(inicio), new DateTime(fim));
    }


    public void voltar(View view) {
        onBackPressed();
    }
}