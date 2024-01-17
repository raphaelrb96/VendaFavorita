package com.rapha.vendafavorita.util;

import androidx.core.content.ContextCompat;

import com.rapha.vendafavorita.R;

import static com.rapha.vendafavorita.Constantes.getMotivoCancelamento;

public class Status {

    public static boolean saqueEmAndamento(int status) {
        if(status == 5 || status == 3) {
            return false;
        }
        return true;

    }

    public static String getStatusSaque(int status) {
        switch (status) {
            case 1:
            default:
                return  "Aguardando";
            case 2:
                return "Confirmado";
            case 3:
                return "Cancelado";
            case 5:
                return "Concluido";
        }

    }
}
