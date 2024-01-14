package com.rapha.vendafavorita.util;

import com.rapha.vendafavorita.objects.EntregaObj;
import com.rapha.vendafavorita.objects.GarantiaObj;
import com.rapha.vendafavorita.objects.PagamentosObj;
import com.rapha.vendafavorita.objects.ParcelamentoObj;

import java.util.ArrayList;

import static com.rapha.vendafavorita.util.FormatoString.formartarPreco;

public class Listas {

    public static ArrayList<GarantiaObj> getListOptionsGarantia(int total) {

        int taxa1 = (total * 15) / 100;
        int taxa2 = (total * 30) / 100;
        int taxa3 = (total * 40) / 100;

        String v1 = formartarPreco(taxa1) + ",00";
        String v2 = formartarPreco(taxa2) + ",00";
        String v3 = formartarPreco(taxa3) + ",00";

        ArrayList<GarantiaObj> garantiaObjs = new ArrayList<>();
        garantiaObjs.add(new GarantiaObj("Garantia de Segurança", "Prazo de 7 dias para troca. Para defeito de fabricação.", "Grátis", 0, 0));
        garantiaObjs.add(new GarantiaObj("Garantia Extra", "Prazo de 30 dias para troca. Apartir da data da compra.", v1, taxa1, 1));
        garantiaObjs.add(new GarantiaObj("Garantia Vip", "Prazo de 3 meses para troca. Apartir da data da compra.", v2, taxa2, 2));
        garantiaObjs.add(new GarantiaObj("Garantia Vitalicia", "Prazo de 6 meses para troca. Apartir da data da compra.", v3, taxa3, 3));

        return garantiaObjs;
    }

    public static ArrayList<EntregaObj> getListOptionsEntregas() {
        ArrayList<EntregaObj> entregaObjs = new ArrayList<>();
        entregaObjs.add(new EntregaObj("1 a 4 horas", "Entrega Local", "Envio para bairros de Manaus. Exceto bairros distantes.", "R$10,00", 10, 0));
        entregaObjs.add(new EntregaObj("2 a 6 horas", "Entrega Distante", "Envio para bairros distantes em Manaus", "R$20,00", 20, 1));
        entregaObjs.add(new EntregaObj("1 a 3 dias", "Entrega Estadual", "Envio para cidades do interior ou passando da barreira", "R$30,00", 30, 2));
        entregaObjs.add(new EntregaObj("30 a 90 minutos", "Entrega Parceira", "Envio para Manaus via app de uber ou 99", "Calcular", 0, 3));
        entregaObjs.add(new EntregaObj("7 a 30 dias", "Entrega Nacional", "Envio para todas as cidades do Brasil", "Calcular", 0, 4));
        return entregaObjs;
    }

    public static ArrayList<PagamentosObj> getListOptionsPagamentos() {
        ArrayList<PagamentosObj> pagamentosObjs = new ArrayList<>();
        pagamentosObjs.add(new PagamentosObj("Débito", "Pagamento Presencial através do motoboy no ato da entrega", "Sem Taxa", 0, 1));
        pagamentosObjs.add(new PagamentosObj("Crédito", "Pagamento Presencial através do motoboy no ato da entrega", "Com Taxa", 0, 2));
        pagamentosObjs.add(new PagamentosObj("Dinheiro", "Pagamento Presencial através do motoboy no ato da entrega", "Sem Taxa", 0, 4));
        pagamentosObjs.add(new PagamentosObj("Pix", "Pagamento Presencial através do motoboy no ato da entrega", "Sem Taxa", 0, 5));
        pagamentosObjs.add(new PagamentosObj("Link", "Pagamento Online através do Link antes do envio", "Com Taxa", 0, 6));
        return pagamentosObjs;
    }

    public static ArrayList<ParcelamentoObj> getListOptionsParcelamento(int total) {
        ArrayList<ParcelamentoObj> parcelamentoObjs = new ArrayList<>();
        for (int i = 1; i < 13; i++) {

            int taxa = (2*i) + 2;
            int taxaTotal = (total * taxa) / 100;

            int totalComTaxa = total+taxaTotal;
            int valorParcela = totalComTaxa/i;

            String pcl = i < 2 ? " Parcela" : " Parcelas";

            String desc = formartarPreco(totalComTaxa) + " em " + i + pcl + " de " + formartarPreco(valorParcela);
            String title = i + pcl;

            parcelamentoObjs.add(new ParcelamentoObj(title, desc, i, "Taxa: " + taxa + "%", taxa, "R$"+totalComTaxa, totalComTaxa));
        }
        return parcelamentoObjs;
    }

}
