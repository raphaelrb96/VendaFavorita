package com.rapha.vendafavorita.objects;

public class ComissaoAfiliados {

    String id;
    int comissao;
    String titulo;
    String descricao;
    long hora;
    String uidVendedor;
    String nomeVendedor;
    String pathVendedor;
    boolean pagamentoRecebido;
    int statusComissao;

    public ComissaoAfiliados(String id, int comissao, String titulo, String descricao, long hora, String uidVendedor, String nomeVendedor, String pathVendedor, boolean pagamentoRecebido, int statusComissao) {
        this.id = id;
        this.comissao = comissao;
        this.titulo = titulo;
        this.descricao = descricao;
        this.hora = hora;
        this.uidVendedor = uidVendedor;
        this.nomeVendedor = nomeVendedor;
        this.pathVendedor = pathVendedor;
        this.pagamentoRecebido = pagamentoRecebido;
        this.statusComissao = statusComissao;
    }

    public ComissaoAfiliados() {

    }

    public String getId() {
        return id;
    }

    public int getComissao() {
        return comissao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public long getHora() {
        return hora;
    }

    public String getUidVendedor() {
        return uidVendedor;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public String getPathVendedor() {
        return pathVendedor;
    }

    public boolean isPagamentoRecebido() {
        return pagamentoRecebido;
    }

    public int getStatusComissao() {
        return statusComissao;
    }
}
