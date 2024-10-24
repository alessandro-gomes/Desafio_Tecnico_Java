package com.alessandrogomes.gestao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario {
    
    private int id;
    private String nome;
    private LocalDate dataContratacao;
    private BigDecimal totalVendas;

    public Funcionario(int id, String nome, LocalDate dataContratacao) {
        this.id = id;
        this.nome = nome;
        this.dataContratacao = dataContratacao;
        this.totalVendas = BigDecimal.ZERO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public BigDecimal getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(BigDecimal totalVendas) {
        this.totalVendas = totalVendas;
    }

    public void adicionarVendas(BigDecimal valor) {
        this.totalVendas = this.totalVendas.add(valor);
    }
    
}
