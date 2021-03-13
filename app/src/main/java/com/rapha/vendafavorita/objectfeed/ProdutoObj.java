package com.rapha.vendafavorita.objectfeed;

public class ProdutoObj {

    String nomeProduto;
    String pathProduto;
    String idProduto;

    public ProdutoObj(String nomeProduto, String pathProduto, String idProduto) {
        this.nomeProduto = nomeProduto;
        this.pathProduto = pathProduto;
        this.idProduto = idProduto;
    }

    public ProdutoObj() {

    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getPathProduto() {
        return pathProduto;
    }

    public String getIdProduto() {
        return idProduto;
    }
}
