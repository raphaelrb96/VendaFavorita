package com.rapha.vendafavorita.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.rapha.vendafavorita.R;

public class Alertas {

    public static void showAlert(Context context, String title, String messagem) {
        AlertDialog.Builder dialogOffline = new AlertDialog.Builder(context);
        dialogOffline.setTitle(title);
        dialogOffline.setMessage(messagem);
        dialogOffline.setPositiveButton("Continuar", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialogOff = dialogOffline.create();
        alertDialogOff.show();
        alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorSecondary));

    }

    public static void showAlertButtonSimples(Context context, String title, String messagem, String botao) {
        AlertDialog.Builder dialogOffline = new AlertDialog.Builder(context);
        dialogOffline.setTitle(title);
        dialogOffline.setMessage(messagem);
        dialogOffline.setPositiveButton(botao, (dialog, which) -> dialog.cancel());

        AlertDialog alertDialogOff = dialogOffline.create();
        alertDialogOff.show();
        alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorSecondary));

    }
}
