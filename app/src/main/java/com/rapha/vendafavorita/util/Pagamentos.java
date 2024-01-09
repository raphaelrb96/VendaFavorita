package com.rapha.vendafavorita.util;

import java.util.ArrayList;

public class Pagamentos {
    public static ArrayList<String> simularParcelamento(int valor) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int taxa = (2*i) + 3;
            int taxaTotal = (valor * taxa) / 100;
            int total = valor+taxaTotal;
            String newString = "R$"+total;
            list.add(newString);
        }
        return list;
    }
}
