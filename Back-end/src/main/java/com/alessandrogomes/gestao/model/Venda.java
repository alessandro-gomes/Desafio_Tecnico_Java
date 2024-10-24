package com.alessandrogomes.gestao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Venda {
    
    private int id;
    private int idFuncionario;
    private int idCliente;
    private BigDecimal valorVenda;
    private LocalDate dataVenda;

    public Venda(int id, int idFuncionario, int idCliente, BigDecimal valorVenda, LocalDate dataVenda) {
        this.id = id;
        this.idFuncionario = idFuncionario;
        this.idCliente = idCliente;
        this.valorVenda = valorVenda;
        this.dataVenda = dataVenda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }
    
}
