package com.rapha.vendafavorita.objects;

public class SaquesObj {

    private String id;
    private long timestampCreated;
    private long timestampPay;
    private String previsao;
    private int valorTemporario;
    private int valorFinal;
    private int status;
    private String chave;
    private String bank;
    private String titular;

    public SaquesObj() {

    }


    public SaquesObj(String id, long timestampCreated, long timestampPay, String previsao, int valorTemporario, int valorFinal, int status, String chave, String bank, String titular) {
        this.id = id;
        this.timestampCreated = timestampCreated;
        this.timestampPay = timestampPay;
        this.previsao = previsao;
        this.valorTemporario = valorTemporario;
        this.valorFinal = valorFinal;
        this.status = status;
        this.chave = chave;
        this.bank = bank;
        this.titular = titular;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getTimestampCreated() {
        return timestampCreated;
    }

    public long getTimestampPay() {
        return timestampPay;
    }

    public String getPrevisao() {
        return previsao;
    }

    public int getValorTemporario() {
        return valorTemporario;
    }

    public int getValorFinal() {
        return valorFinal;
    }

    public int getStatus() {
        return status;
    }

    public String getChave() {
        return chave;
    }

    public String getBank() {
        return bank;
    }

    public String getTitular() {
        return titular;
    }
}
