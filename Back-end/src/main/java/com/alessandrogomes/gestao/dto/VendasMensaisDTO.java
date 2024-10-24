package com.alessandrogomes.gestao.dto;

import java.math.BigDecimal;

public class VendasMensaisDTO {

    private String mes;
    private BigDecimal total;

    public VendasMensaisDTO(String mes, BigDecimal total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    @Override
    public String toString() {
        return "VendasMensaisDTO{"
                + "mes='" + mes + '\''
                + ", total=" + total
                + '}';
    }
}
