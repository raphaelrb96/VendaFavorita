package com.rapha.vendafavorita.objects;

import java.util.ArrayList;

public class ObjectRevenda {

    private String adress;
    private int comissaoTotal;
    private String complemento;
    private int compraValor;
    private String detalhePag;
    private int formaDePagar;
    private int frete;
    private long hora;
    private String idCompra;
    private double lat;
    private ArrayList<ObjProdutoRevenda> listaDeProdutos;
    private double lng;
    private String nomeCliente;
    private boolean pagamentoRecebido;
    private String pathFotoUserRevenda;
    private String phoneCliente;
    private int statusCompra;
    private int tipoDeEntrega;
    private String uidUserRevendedor;
    private String userNomeRevendedor;
    private int valorTotal;
    private boolean vendaConcluida;

    private boolean existeComissaoAfiliados;

    private String uidAdm;

    public ObjectRevenda () {

    }

    public ObjectRevenda(String adress, int comissaoTotal, String complemento, int compraValor, String detalhePag, int formaDePagar, int frete, long hora, String idCompra, double lat, ArrayList<ObjProdutoRevenda> listaDeProdutos, double lng, String nomeCliente, boolean pagamentoRecebido, String pathFotoUserRevenda, String phoneCliente, int statusCompra, int tipoDeEntrega, String uidUserRevendedor, String userNomeRevendedor, int valorTotal, boolean vendaConcluida, boolean existeComissaoAfiliados, String uidAdm) {
        this.adress = adress;
        this.comissaoTotal = comissaoTotal;
        this.complemento = complemento;
        this.compraValor = compraValor;
        this.detalhePag = detalhePag;
        this.formaDePagar = formaDePagar;
        this.frete = frete;
        this.hora = hora;
        this.idCompra = idCompra;
        this.lat = lat;
        this.listaDeProdutos = listaDeProdutos;
        this.lng = lng;
        this.nomeCliente = nomeCliente;
        this.pagamentoRecebido = pagamentoRecebido;
        this.pathFotoUserRevenda = pathFotoUserRevenda;
        this.phoneCliente = phoneCliente;
        this.statusCompra = statusCompra;
        this.tipoDeEntrega = tipoDeEntrega;
        this.uidUserRevendedor = uidUserRevendedor;
        this.userNomeRevendedor = userNomeRevendedor;
        this.valorTotal = valorTotal;
        this.vendaConcluida = vendaConcluida;
        this.existeComissaoAfiliados = existeComissaoAfiliados;
        this.uidAdm = uidAdm;
    }

    public String getAdress() {
        return adress;
    }

    public int getComissaoTotal() {
        return comissaoTotal;
    }

    public String getComplemento() {
        return complemento;
    }

    public int getCompraValor() {
        return compraValor;
    }

    public String getUidAdm() {
        return uidAdm;
    }

    public String getDetalhePag() {
        return detalhePag;
    }

    public int getFormaDePagar() {
        return formaDePagar;
    }

    public int getFrete() {
        return frete;
    }

    public long getHora() {
        return hora;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public double getLat() {
        return lat;
    }

    public ArrayList<ObjProdutoRevenda> getListaDeProdutos() {
        return listaDeProdutos;
    }

    public double getLng() {
        return lng;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public boolean isPagamentoRecebido() {
        return pagamentoRecebido;
    }

    public String getPathFotoUserRevenda() {
        return pathFotoUserRevenda;
    }

    public String getPhoneCliente() {
        return phoneCliente;
    }

    public int getStatusCompra() {
        return statusCompra;
    }

    public int getTipoDeEntrega() {
        return tipoDeEntrega;
    }

    public String getUidUserRevendedor() {
        return uidUserRevendedor;
    }

    public String getUserNomeRevendedor() {
        return userNomeRevendedor;
    }

    public int getValorTotal() {
        return valorTotal;
    }

    public boolean isVendaConcluida() {
        return vendaConcluida;
    }

    public boolean isExisteComissaoAfiliados() {
        return existeComissaoAfiliados;
    }
}
