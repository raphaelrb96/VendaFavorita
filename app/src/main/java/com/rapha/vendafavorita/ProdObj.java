package com.rapha.vendafavorita;

import java.util.ArrayList;
import java.util.Map;

public class ProdObj {

    Map<String, Boolean> categorias;
    String descr;
    boolean disponivel;
    String idProduto;
    String imgCapa;
    ArrayList<String> imagens;
    String fabricante;
    int nivel;
    String prodName;
    float prodValor;
    float valorAntigo;
    boolean promocional;
    Map<String, Boolean> tag;
    Map<String, Double> fornecedores;
    int quantidade;

    long timeUpdate;

    int comissao;

    private ArrayList<String> cores;

    private float prodValorPromocional;
    private float prodValorAtacarejo;
    private float prodValorAtacado;

    private ArrayList<String> tamanhos;
    private ArrayList<String> numeros;

    private boolean destaque;

    private String variante1;
    private String variante2;
    private String variante3;
    private ArrayList<String> variaveis1;
    private ArrayList<String> variaveis2;
    private ArrayList<String> variaveis3;

    public ProdObj() {

    }


    public ProdObj(Map<String, Boolean> categorias, String descr, boolean disponivel, String idProduto, String imgCapa, ArrayList<String> imagens, String fabricante, int nivel, String prodName, float prodValor, float valorAntigo, boolean promocional, Map<String, Boolean> tag, Map<String, Double> fornecedores, int quantidade, long timeUpdate, int comissao, ArrayList<String> cores, float prodValorPromocional, float prodValorAtacarejo, float prodValorAtacado) {
        this.categorias = categorias;
        this.descr = descr;
        this.disponivel = disponivel;
        this.idProduto = idProduto;
        this.imgCapa = imgCapa;
        this.imagens = imagens;
        this.fabricante = fabricante;
        this.nivel = nivel;
        this.prodName = prodName;
        this.prodValor = prodValor;
        this.valorAntigo = valorAntigo;
        this.promocional = promocional;
        this.tag = tag;
        this.fornecedores = fornecedores;
        this.quantidade = quantidade;
        this.timeUpdate = timeUpdate;
        this.comissao = comissao;
        this.cores = cores;
        this.prodValorPromocional = prodValorPromocional;
        this.prodValorAtacarejo = prodValorAtacarejo;
        this.prodValorAtacado = prodValorAtacado;
    }

    public ArrayList<String> getCores() {
        return cores;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Map<String, Double> getFornecedores() {
        return fornecedores;
    }

    public Map<String, Boolean> getTag() {
        return this.tag;
    }

    public String getIdProduto() {
        return this.idProduto;
    }

    public Map<String, Boolean> getCategorias() {
        return categorias;
    }

    public ArrayList<String> getImagens() {
        return imagens;
    }

    public float getValorAntigo() {
        return valorAntigo;
    }

    public boolean isDisponivel() {
        return this.disponivel;
    }

    public int getNivel() {
        return this.nivel;
    }

    public boolean isPromocional() {
        return this.promocional;
    }

    public String getImgCapa() {
        return this.imgCapa;
    }

    public String getProdName() {
        return this.prodName;
    }

    public String getFabricante() {
        return this.fabricante;
    }

    public float getProdValor() {
        return this.prodValor;
    }

    public String getDescr() {
        return this.descr;
    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public int getComissao() {
        return comissao;
    }

    public float getProdValorPromocional() {
        return prodValorPromocional;
    }

    public float getProdValorAtacarejo() {
        return prodValorAtacarejo;
    }

    public float getProdValorAtacado() {
        return prodValorAtacado;
    }
}
