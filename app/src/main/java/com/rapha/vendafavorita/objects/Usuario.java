package com.rapha.vendafavorita.objects;

public class Usuario {

    private String nome;
    private String email;
    private String celular;
    private int controleDeVersao;
    private String uid;
    private String pathFoto;
    private int tipoDeUsuario;
    private String provedor;
    private long ultimoLogin;
    private long primeiroLogin;
    private String tokenFcm;

    private Endereco endereco;

    private String userName;

    private String uidAdm;
    private String usernameAdm;
    private String nomeAdm;
    private String pathFotoAdm;

    private boolean admConfirmado;

    private boolean vipDiamante;

    public Usuario() {

    }


    public Usuario(boolean vipDiamante, String nome, String email, String celular, int controleDeVersao, String uid, String pathFoto, int tipoDeUsuario, String provedor, long ultimoLogin, long primeiroLogin, String tokenFcm, Endereco endereco, String userName, String uidAdm, String usernameAdm, String nomeAdm, String pathFotoAdm, boolean admConfirmado) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.controleDeVersao = controleDeVersao;
        this.uid = uid;
        this.pathFoto = pathFoto;
        this.tipoDeUsuario = tipoDeUsuario;
        this.provedor = provedor;
        this.ultimoLogin = ultimoLogin;
        this.primeiroLogin = primeiroLogin;
        this.tokenFcm = tokenFcm;
        this.endereco = endereco;
        this.userName = userName;
        this.uidAdm = uidAdm;
        this.usernameAdm = usernameAdm;
        this.nomeAdm = nomeAdm;
        this.pathFotoAdm = pathFotoAdm;
        this.admConfirmado = admConfirmado;
        this.vipDiamante = vipDiamante;
    }

    public boolean isVipDiamante() {
        return vipDiamante;
    }

    public String getUserName() {
        return userName;
    }

    public String getUidAdm() {
        return uidAdm;
    }

    public String getUsernameAdm() {
        return usernameAdm;
    }

    public String getNomeAdm() {
        return nomeAdm;
    }

    public String getPathFotoAdm() {
        return pathFotoAdm;
    }

    public boolean isAdmConfirmado() {
        return admConfirmado;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public int getControleDeVersao() {
        return controleDeVersao;
    }

    public String getUid() {
        return uid;
    }

    public long getPrimeiroLogin() {
        return primeiroLogin;
    }

    public long getUltimoLogin() {
        return ultimoLogin;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public String getProvedor() {
        return provedor;
    }

    public int getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}
