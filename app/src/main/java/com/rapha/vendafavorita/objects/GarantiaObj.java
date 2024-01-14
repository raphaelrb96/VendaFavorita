package com.rapha.vendafavorita.objects;

public class GarantiaObj {

    private String titulo;
    private String descricao;
    private String valorString;
    int valor;
    private int id;

    public GarantiaObj(String titulo, String descricao, String valorString, int valor, int id) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.valorString = valorString;
        this.valor = valor;
        this.id = id;
    }

    public GarantiaObj() {

    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getValorString() {
        return valorString;
    }

    public int getValor() {
        return valor;
    }

    public int getId() {
        return id;
    }
}
