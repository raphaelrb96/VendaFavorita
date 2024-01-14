package com.rapha.vendafavorita.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatoString {
    public static String formartarPreco(int v) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String pattern = "R$#,##0.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        String formatado = decimalFormat.format(v);
        return formatado;
    }
}
