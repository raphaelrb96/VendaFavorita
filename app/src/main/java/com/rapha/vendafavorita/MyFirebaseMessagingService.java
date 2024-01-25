package com.rapha.vendafavorita;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.rapha.vendafavorita.FragmentMain.ADMINISTRADOR;
import static com.rapha.vendafavorita.MainActivity.MEU_CANAL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //getSharedPreferences(Constantes.TOKEN_NOTIFICACAO, MODE_PRIVATE).edit().putString(Constantes.TOKEN, s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //notificacaoSimple(remoteMessage);
    }


    private void notificacaoSimple(RemoteMessage message) {

        /*

        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String action = message.getData().get("clickAction");

        Intent intent = new Intent(this, MensagemDetalheActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intentCentMsg = new Intent(this, MensagemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intentCentralCompras = new Intent(this, CentralComprasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intentMinhasCompras = new Intent(this, MinhasComprasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntentCentMsg = PendingIntent.getActivity(this, 1, intentCentMsg, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent centralCompras = PendingIntent.getActivity(this, 2, intentCentralCompras, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent minhasCompras = PendingIntent.getActivity(this, 3, intentMinhasCompras, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MEU_CANAL);

        builder.setSmallIcon(R.drawable.ic_star_black_24dp);
        builder.setColor(Color.RED);
        builder.setContentTitle(title);
        builder.setContentText(body);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        if (action.equals("mensagem")) {
            if (ADMINISTRADOR) {
                builder.setContentIntent(pendingIntentCentMsg);
            } else {
                builder.setContentIntent(pendingIntent);
            }
            //todo abrir central de mensagens quando for a compilacao do admin
        } else if (action.equals("compra")) {
            //todo abrir central de compras quando for a compilacao do admin
            builder.setContentIntent(centralCompras);
        } else if (action.equals("statusCompra")){
            builder.setContentIntent(minhasCompras);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());


         */

    }
}
