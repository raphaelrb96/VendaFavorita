package com.rapha.vendafavorita.objects;

import com.rapha.vendafavorita.util.Listas;

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

    private int idCancelamento;
    private String detalheCancelamento;

    private GarantiaObj garantiaFinal;
    private EntregaObj entregaFinal;
    private PagamentosObj pagamentoFinal;
    private ParcelamentoObj parcelaFinal;

    private String cep;
    private String cidade;
    private String estado;
    private String obs;



    public ObjectRevenda () {

    }


    public ObjectRevenda(String adress, int comissaoTotal, String complemento, int compraValor, String detalhePag, int formaDePagar, int frete, long hora, String idCompra, double lat, ArrayList<ObjProdutoRevenda> listaDeProdutos, double lng, String nomeCliente, boolean pagamentoRecebido, String pathFotoUserRevenda, String phoneCliente, int statusCompra, int tipoDeEntrega, String uidUserRevendedor, String userNomeRevendedor, int valorTotal, boolean vendaConcluida, boolean existeComissaoAfiliados, String uidAdm, int idCancelamento, String detalheCancelamento, GarantiaObj garantiaFinal, EntregaObj entregaFinal, PagamentosObj pagamentoFinal, ParcelamentoObj parcelaFinal, String cep, String cidade, String estado, String obs) {
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
        this.idCancelamento = idCancelamento;
        this.detalheCancelamento = detalheCancelamento;
        this.garantiaFinal = garantiaFinal;
        this.entregaFinal = entregaFinal;
        this.pagamentoFinal = pagamentoFinal;
        this.parcelaFinal = parcelaFinal;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.obs = obs;
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getObs() {
        return obs;
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

    public int getIdCancelamento() {
        return idCancelamento;
    }

    public String getDetalheCancelamento() {
        return detalheCancelamento;
    }

    public GarantiaObj getGarantiaFinal() {
        return garantiaFinal;
    }

    public EntregaObj getEntregaFinal() {
        return entregaFinal;
    }

    public PagamentosObj getPagamentoFinal() {
        return pagamentoFinal;
    }

    public ParcelamentoObj getParcelaFinal() {
        return parcelaFinal;
    }
}
