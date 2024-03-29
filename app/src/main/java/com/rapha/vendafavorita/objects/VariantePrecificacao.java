package com.rapha.vendafavorita.objects;

public class VariantePrecificacao {

    private int comissao;
    private int bonusVip;
    private float valor;
    private String nome;
    private int quantidadeMinima;
    private String aviso;
    int id;

    public VariantePrecificacao(int comissao, int bonusVip, float valor, String nome, int quantidadeMinima, String aviso, int id) {
        this.comissao = comissao;
        this.bonusVip = bonusVip;
        this.valor = valor;
        this.nome = nome;
        this.quantidadeMinima = quantidadeMinima;
        this.aviso = aviso;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public int getComissao() {
        return comissao;
    }

    public int getBonusVip() {
        return bonusVip;
    }

    public float getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public String getAviso() {
        return aviso;
    }
}
