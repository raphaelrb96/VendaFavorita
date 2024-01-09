package com.rapha.vendafavorita.objectfeed;


import com.rapha.vendafavorita.objects.ObjectBonus;

import java.util.ArrayList;

public class ObjectRecompensas {
    private ArrayList<ObjectBonus> vendas;
    private ArrayList<ObjectBonus> recrutamento;
    String regrasVendas;
    String regrasRecrutamento;

    public ObjectRecompensas(ArrayList<ObjectBonus> vendas, ArrayList<ObjectBonus> recrutamento, String regrasVendas, String regrasRecrutamento) {
        this.vendas = vendas;
        this.recrutamento = recrutamento;
        this.regrasVendas = regrasVendas;
        this.regrasRecrutamento = regrasRecrutamento;
    }

    public ObjectRecompensas() {
    }

    public ArrayList<ObjectBonus> getRecrutamento() {
        return recrutamento;
    }

    public ArrayList<ObjectBonus> getVendas() {
        return vendas;
    }

    public String getRegrasVendas() {
        return regrasVendas;
    }

    public String getRegrasRecrutamento() {
        return regrasRecrutamento;
    }
}
