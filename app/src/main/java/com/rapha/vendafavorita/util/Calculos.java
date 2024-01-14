package com.rapha.vendafavorita.util;

import com.rapha.vendafavorita.ProdObj;
import com.rapha.vendafavorita.objects.VariantePrecificacao;

import java.util.ArrayList;

public class Calculos {

    public static float getValorVarejo(float valor) {
        float nValor = valor;
        if(valor < 50) {
            nValor = 50;
        }
        return nValor;
    }

    public static float getValorPromocional(float valor) {
        float nValor = valor;
        if(valor < 50) {
            nValor = 50;
        }
        return nValor - 10;
    }

    public static float getValorAtacarejo(float valor) {
        float nValor = valor;
        if(valor < 50) {
            nValor = 50;
        }
        return nValor - 20;
    }

    public static float getValorAtacado(float valor) {
        float nValor = valor;
        if(valor < 50) {
            nValor = 50;
        }
        return nValor - 30;
    }

    public static float getValorCusto(float valor) {
        float nValor = valor;
        if(valor < 50) {
            nValor = 50;
        }
        return nValor - 45;
    }

    public static ArrayList<VariantePrecificacao> getListaPrecificacao(int comissao, float valor) {

        ArrayList<VariantePrecificacao> precificacoes = new ArrayList<>();

        //varejo
        int comissaoVarejo = comissao;
        if(comissao < 15) {
            comissaoVarejo = 15;
        }
        int valorVarejo = (int) getValorVarejo(valor);
        VariantePrecificacao varejo = new VariantePrecificacao(comissaoVarejo, 0, valorVarejo, "VAREJO", 1, "", 0);

        //promocao
        int promoComissao = 10;
        int valorPromo = (int) getValorPromocional(valor);
        VariantePrecificacao promocao = new VariantePrecificacao(promoComissao, 0, valorPromo, "PROMOÇÃO", 1, "", 1);

        //atacarejo
        int atacarejoComissao = 5;
        int valorAtacarejo = (int) getValorAtacarejo(valor);
        VariantePrecificacao atacarejo = new VariantePrecificacao(atacarejoComissao, 0, valorAtacarejo, "ATACAREJO", 1, "", 2);

        //atacado
        int atacadoComissao = 3;
        int valorAtacado = (int) getValorAtacado(valor);
        VariantePrecificacao atacado = new VariantePrecificacao(atacadoComissao, 0, valorAtacado, "ATACADO", 6, "", 3);

        precificacoes.add(varejo);
        precificacoes.add(promocao);
        precificacoes.add(atacarejo);
        precificacoes.add(atacado);


        return precificacoes;

    }

}
