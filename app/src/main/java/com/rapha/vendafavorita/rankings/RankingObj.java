package com.rapha.vendafavorita.rankings;

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

    ArrayList<Usuario> vendedoresParticipantes;
    ArrayList<Usuario> ganhadores;

    ArrayList<ObjectRevenda> revendasContabilizadas;

    public RankingObj() {

    }

    public RankingObj(long inicio, String inicioString, String fimString, String titulo, String descricao, String regras, String premio, int tipo, int tipoRakingAfiliado, int tipoRakingVenda, int meta, long ultimaAtualizacao, String ultimaAtualizacaoString, ArrayList<Usuario> vendedoresParticipantes, ArrayList<Usuario> ganhadores, ArrayList<ObjectRevenda> revendasContabilizadas) {
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
        this.vendedoresParticipantes = vendedoresParticipantes;
        this.ganhadores = ganhadores;
        this.revendasContabilizadas = revendasContabilizadas;
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

    public ArrayList<Usuario> getVendedoresParticipantes() {
        return vendedoresParticipantes;
    }

    public ArrayList<Usuario> getGanhadores() {
        return ganhadores;
    }

    public ArrayList<ObjectRevenda> getRevendasContabilizadas() {
        return revendasContabilizadas;
    }
}
