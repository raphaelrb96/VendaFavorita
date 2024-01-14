package com.rapha.vendafavorita.objects;

public class ParcelamentoObj {

    private String titulo;
    private String descricao;
    private int id;

    //taxa
    private String valorString;
    private int valor;

    private String totalString;
    private int total;

    public ParcelamentoObj() {
    }

    public ParcelamentoObj(String titulo, String descricao, int id, String valorString, int valor, String totalString, int total) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.id = id;
        this.valorString = valorString;
        this.valor = valor;
        this.totalString = totalString;
        this.total = total;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }

    public String getValorString() {
        return valorString;
    }

    public int getValor() {
        return valor;
    }

    public String getTotalString() {
        return totalString;
    }

    public int getTotal() {
        return total;
    }
}
