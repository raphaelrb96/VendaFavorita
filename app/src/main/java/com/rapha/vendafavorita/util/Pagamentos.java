package com.rapha.vendafavorita.util;

import java.util.ArrayList;

public class Pagamentos {

    public static ArrayList<String> simularParcelamento(int valor) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 13; i++) {

            int taxa = (2*i) + 2;
            int taxaTotal = (valor * taxa) / 100;

            int total = valor+taxaTotal;
            int valorParcela = total/i;
            String newString = "R$"+valorParcela;

            list.add(newString);
        }
        return list;
    }

}
