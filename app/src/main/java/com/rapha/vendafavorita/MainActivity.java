package com.rapha.vendafavorita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.rapha.vendafavorita.FragmentMain.user;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> ids = new ArrayList();
    public static String MEU_CANAL = "Ocapop";
    public static String uidShareLink = null;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winPar = win.getAttributes();
        if (on) {
            winPar.flags |= bits;
        } else {
            winPar.flags &= ~bits;
        }
        win.setAttributes(winPar);
    }

    private void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.inc.ocapop",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static String getUidShareLink() {
        return uidShareLink;
    }

    public static void setUidShareLink(String uidShareLink) {
        MainActivity.uidShareLink = uidShareLink;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("ADM", uidShareLink);
        super.onSaveInstanceState(outState, outPersistentState);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.inc.ocapop", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }


        //fab = (FloatingActionButton) findViewById(R.id.fab_main);

        //uidShareLink = getIntent().getStringExtra("adm");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("adm");
            //The key argument here must match that used in the other activity
            if(value != null) {
                uidShareLink = value;
                Log.d("TesteLogin", "Main activity Extras: " + value);
            }

        }

        criarCanal();





    }

    private void criarCanal () {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(MEU_CANAL, "VendaFavorita Notificações", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription("Notificações do App VendaFavorita");
            manager1.createNotificationChannel(channel);
        }
    }

    private void abrirMensagem() {
        //Toast.makeText(this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MensagemActivity.class));
    }
}