package com.rapha.vendafavorita.objects;

public class ObjProdutoRevenda {

    private String caminhoImg;
    private int comissaoTotal;
    private int comissaoUnidade;
    private String idProdut;
    private String labo;
    private String produtoName;
    private int quantidade;
    private int valorTotal;
    private int valorTotalComComissao;
    private int valorUni;
    private int valorUniComComissao;
    private int idModoPreco;
    private String modoPreco;
    private int quantidadeMinima;

    public ObjProdutoRevenda(String caminhoImg, int comissaoTotal, int comissaoUnidade, String idProdut, String labo, String produtoName, int quantidade, int valorTotal, int valorTotalComComissao, int valorUni, int valorUniComComissao, int idModoPreco, String modoPreco, int quantidadeMinima) {
        this.caminhoImg = caminhoImg;
        this.comissaoTotal = comissaoTotal;
        this.comissaoUnidade = comissaoUnidade;
        this.idProdut = idProdut;
        this.labo = labo;
        this.produtoName = produtoName;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.valorTotalComComissao = valorTotalComComissao;
        this.valorUni = valorUni;
        this.valorUniComComissao = valorUniComComissao;
        this.modoPreco = modoPreco;
        this.idModoPreco = idModoPreco;
        this.quantidadeMinima = quantidadeMinima;
    }

    public ObjProdutoRevenda() {

    }

    public String getCaminhoImg() {
        return caminhoImg;
    }

    public int getComissaoTotal() {
        return comissaoTotal;
    }

    public int getComissaoUnidade() {
        return comissaoUnidade;
    }

    public String getIdProdut() {
        return idProdut;
    }

    public String getLabo() {
        return labo;
    }

    public String getProdutoName() {
        return produtoName;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getValorTotal() {
        return valorTotal;
    }

    public int getValorTotalComComissao() {
        return valorTotalComComissao;
    }

    public int getValorUni() {
        return valorUni;
    }

    public int getValorUniComComissao() {
        return valorUniComComissao;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public String getModoPreco() {
        return modoPreco;
    }

    public int getIdModoPreco() {
        return idModoPreco;
    }
}
