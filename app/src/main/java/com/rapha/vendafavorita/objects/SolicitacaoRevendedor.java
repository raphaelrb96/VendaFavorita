package com.rapha.vendafavorita.objects;

public class SolicitacaoRevendedor {

    private String email, foto, nome, zap, obs, userName, uid;
    private long timestamp;

    private int aReceber;

    public SolicitacaoRevendedor () {

    }

    public SolicitacaoRevendedor(String email, String foto, String nome, String zap, String obs, String userName, long timestamp, String uid, int aReceber) {
        this.email = email;
        this.foto = foto;
        this.nome = nome;
        this.zap = zap;
        this.obs = obs;
        this.userName = userName;
        this.timestamp = timestamp;
        this.uid = uid;
        this.aReceber = aReceber;
    }

    public int getaReceber() {
        return aReceber;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }

    public String getNome() {
        return nome;
    }

    public String getZap() {
        return zap;
    }

    public String getObs() {
        return obs;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
