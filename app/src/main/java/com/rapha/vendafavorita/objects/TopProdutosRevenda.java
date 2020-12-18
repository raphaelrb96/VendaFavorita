package com.rapha.vendafavorita.objects;

public class TopProdutosRevenda implements Comparable<TopProdutosRevenda> {

    String nomeProduto;
    String pathProduto;
    String idProduto;
    int numeroDeRevendas;

    public TopProdutosRevenda(String nomeProduto, String pathProduto, String idProduto, int numeroDeRevendas) {
        this.nomeProduto = nomeProduto;
        this.pathProduto = pathProduto;
        this.idProduto = idProduto;
        this.numeroDeRevendas = numeroDeRevendas;
    }

    public TopProdutosRevenda() {

    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getPathProduto() {
        return pathProduto;
    }

    public void setPathProduto(String pathProduto) {
        this.pathProduto = pathProduto;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getNumeroDeRevendas() {
        return numeroDeRevendas;
    }

    public void setNumeroDeRevendas(int numeroDeRevendas) {
        this.numeroDeRevendas = numeroDeRevendas;
    }

    public TopProdutosParcelable getParcelable() {
        return new TopProdutosParcelable(nomeProduto, pathProduto, idProduto, numeroDeRevendas);
    }

    @Override
    public int compareTo (TopProdutosRevenda topProdutosRevenda) {
        if (this.numeroDeRevendas > topProdutosRevenda.getNumeroDeRevendas()) {
            return -1;
        } if (this.numeroDeRevendas < topProdutosRevenda.getNumeroDeRevendas()) {
            return 1;
        }
        return 0;
    }
}
