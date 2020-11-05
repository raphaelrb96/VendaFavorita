package com.rapha.vendafavorita.objects;

import com.rapha.vendafavorita.ProdObj;

import java.util.ArrayList;

public class FeedMainObj {

    //versao 1
    private long timeUpdate;
    private int controleVersao;

    //3
    private ArrayList<ProdObj> smartWatchs;

    //17
    private ArrayList<ProdObj> caixasDeSom;

    //4
    private ArrayList<ProdObj> eletronicos;

    //31
    private ArrayList<ProdObj> maquinaDeCabelo;

    //19
    private ArrayList<ProdObj> acessoriosAutomotivos;

    //11
    private ArrayList<ProdObj> acessoriosGamer;

    //2
    private ArrayList<ProdObj> acessoriosPc;

    //12
    private ArrayList<ProdObj> ferramentas;

    //100
    private ArrayList<ProdObj> combos;


    public FeedMainObj(long timeUpdate, int controleVersao, ArrayList<ProdObj> smartWatchs, ArrayList<ProdObj> caixasDeSom, ArrayList<ProdObj> eletronicos, ArrayList<ProdObj> maquinaDeCabelo, ArrayList<ProdObj> acessoriosAutomotivos, ArrayList<ProdObj> acessoriosGamer, ArrayList<ProdObj> acessoriosPc, ArrayList<ProdObj> ferramentas, ArrayList<ProdObj> combos) {
        this.timeUpdate = timeUpdate;
        this.controleVersao = controleVersao;
        this.smartWatchs = smartWatchs;
        this.caixasDeSom = caixasDeSom;
        this.eletronicos = eletronicos;
        this.maquinaDeCabelo = maquinaDeCabelo;
        this.acessoriosAutomotivos = acessoriosAutomotivos;
        this.acessoriosGamer = acessoriosGamer;
        this.acessoriosPc = acessoriosPc;
        this.ferramentas = ferramentas;
        this.combos = combos;
    }

    public FeedMainObj() {

    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public int getControleVersao() {
        return controleVersao;
    }

    public ArrayList<ProdObj> getSmartWatchs() {
        return smartWatchs;
    }

    public ArrayList<ProdObj> getCaixasDeSom() {
        return caixasDeSom;
    }

    public ArrayList<ProdObj> getEletronicos() {
        return eletronicos;
    }

    public ArrayList<ProdObj> getMaquinaDeCabelo() {
        return maquinaDeCabelo;
    }

    public ArrayList<ProdObj> getAcessoriosAutomotivos() {
        return acessoriosAutomotivos;
    }

    public ArrayList<ProdObj> getAcessoriosGamer() {
        return acessoriosGamer;
    }

    public ArrayList<ProdObj> getAcessoriosPc() {
        return acessoriosPc;
    }

    public ArrayList<ProdObj> getFerramentas() {
        return ferramentas;
    }

    public ArrayList<ProdObj> getCombos() {
        return combos;
    }
}
