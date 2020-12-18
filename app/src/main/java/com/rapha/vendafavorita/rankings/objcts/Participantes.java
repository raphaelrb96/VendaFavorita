package com.rapha.vendafavorita.rankings.objcts;

public class Participantes {

    String uid;
    String nome;
    String pathFoto;
    int vendas;
    int itensVendidos;
    int totalVendido;

    public Participantes(String uid, String nome, String pathFoto, int vendas, int itensVendidos, int totalVendido) {
        this.uid = uid;
        this.nome = nome;
        this.pathFoto = pathFoto;
        this.vendas = vendas;
        this.itensVendidos = itensVendidos;
        this.totalVendido = totalVendido;
    }

    public Participantes() {

    }

    public void setVendas(int v) {
        this.vendas = this.vendas + v;
    }

    public void setItensVendidos(int iv) {
        this.itensVendidos = this.itensVendidos + iv;
    }

    public void setTotalVendido(int tv) {
        this.totalVendido = this.totalVendido + tv;
    }

    public String getUid() {
        return uid;
    }

    public String getNome() {
        return nome;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public int getVendas() {
        return vendas;
    }

    public int getItensVendidos() {
        return itensVendidos;
    }

    public int getTotalVendido() {
        return totalVendido;
    }
}
