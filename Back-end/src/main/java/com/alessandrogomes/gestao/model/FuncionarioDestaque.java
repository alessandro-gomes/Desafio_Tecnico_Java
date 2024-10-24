package com.alessandrogomes.gestao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuncionarioDestaque extends Funcionario{

    private String mes;

    public FuncionarioDestaque(String mes, int id, String nome, LocalDate dataContratacao, BigDecimal totalVendas) {
        super(id, nome, dataContratacao);
        this.mes = mes;
        adicionarVendas(totalVendas);
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

}
