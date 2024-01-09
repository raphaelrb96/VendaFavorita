package com.rapha.vendafavorita.objects;

import com.rapha.vendafavorita.objectfeed.ProdutoObj;
import com.rapha.vendafavorita.objectfeed.RevendedorObj;

import java.util.ArrayList;

public class FeedPrincipalObj {

    private ArrayList<RevendedorObj> topRevendedores;
    private ArrayList<ProdutoObj> topProdutos;
    private ArrayList<ProdutoObj> topMaisVendidos;
    private ArrayList<ProdutoObj> atualizacoesProds;
    private ArrayList<TopAdms> topAdms;
    private long timeStamp;


    public FeedPrincipalObj(ArrayList<RevendedorObj> topRevendedores, ArrayList<ProdutoObj> topProdutos, ArrayList<ProdutoObj> atualizacoesProds, long timeStamp, ArrayList<ProdutoObj> topMaisVendidos, ArrayList<TopAdms> topAdms) {
        this.topRevendedores = topRevendedores;
        this.topProdutos = topProdutos;
        this.topMaisVendidos = topMaisVendidos;
        this.atualizacoesProds = atualizacoesProds;
        this.topAdms = topAdms;
        this.timeStamp = timeStamp;
    }

    public FeedPrincipalObj() {

    }

    public ArrayList<RevendedorObj> getTopRevendedores() {
        return topRevendedores;
    }

    public ArrayList<ProdutoObj> getTopProdutos() {
        return topProdutos;
    }

    public ArrayList<ProdutoObj> getAtualizacoesProds() {
        return atualizacoesProds;
    }

    public ArrayList<ProdutoObj> getTopMaisVendidos() {
        return topMaisVendidos;
    }

    public ArrayList<TopAdms> getTopAdms() {
        return topAdms;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
