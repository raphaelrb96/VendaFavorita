package com.rapha.vendafavorita.rankings.objcts;

import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.Usuario;

import java.util.ArrayList;

public class RankingObj {

    long inicio;
    String inicioString;
    String fimString;

    String titulo;
    String descricao;
    String regras;
    String premio;

    int tipo;
    int tipoRakingAfiliado;
    int tipoRakingVenda;

    int meta;

    long ultimaAtualizacao;
    String ultimaAtualizacaoString;

    ArrayList<String> ganhadores;

    ArrayList<ObjectRevenda> revendasContabilizadas;

    String id;
    boolean ativa;

    public RankingObj() {

    }

    public RankingObj(long inicio, String inicioString, String fimString, String titulo, String descricao, String regras, String premio, int tipo, int tipoRakingAfiliado, int tipoRakingVenda, int meta, long ultimaAtualizacao, String ultimaAtualizacaoString, ArrayList<String> ganhadores, ArrayList<ObjectRevenda> revendasContabilizadas, String id, boolean ativo) {
        this.inicio = inicio;
        this.inicioString = inicioString;
        this.fimString = fimString;
        this.titulo = titulo;
        this.descricao = descricao;
        this.regras = regras;
        this.premio = premio;
        this.tipo = tipo;
        this.tipoRakingAfiliado = tipoRakingAfiliado;
        this.tipoRakingVenda = tipoRakingVenda;
        this.meta = meta;
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.ultimaAtualizacaoString = ultimaAtualizacaoString;
        this.ganhadores = ganhadores;
        this.revendasContabilizadas = revendasContabilizadas;
        this.id = id;
        this.ativa = ativo;
    }

    public String getPremio() {
        return premio;
    }

    public long getInicio() {
        return inicio;
    }

    public String getInicioString() {
        return inicioString;
    }

    public String getFimString() {
        return fimString;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getRegras() {
        return regras;
    }

    public int getTipo() {
        return tipo;
    }

    public int getTipoRakingAfiliado() {
        return tipoRakingAfiliado;
    }

    public int getTipoRakingVenda() {
        return tipoRakingVenda;
    }

    public int getMeta() {
        return meta;
    }

    public long getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public String getUltimaAtualizacaoString() {
        return ultimaAtualizacaoString;
    }

    public ArrayList<String> getGanhadores() {
        return ganhadores;
    }

    public ArrayList<ObjectRevenda> getRevendasContabilizadas() {
        return revendasContabilizadas;
    }

    public String getId() {
        return id;
    }

    public boolean isAtiva() {
        return ativa;
    }
}
