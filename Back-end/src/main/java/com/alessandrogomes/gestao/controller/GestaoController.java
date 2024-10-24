package com.alessandrogomes.gestao.controller;

import com.alessandrogomes.gestao.dto.VendasMensaisDTO;
import com.alessandrogomes.gestao.model.Funcionario;
import com.alessandrogomes.gestao.model.FuncionarioDestaque;
import com.alessandrogomes.gestao.service.RelatorioService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/gestao")
public class GestaoController {

    @Autowired
    private RelatorioService relatorioService;

    // Endpoint para retornar a quantidade total de funcionarios
    @GetMapping("/quantidade-funcionarios")
    public int getQuantidadeFuncionarios() {
        return relatorioService.calcularQuantidadeFuncionarios();
    }

    // Endpoint para retornar a quantidade total de clientes
    @GetMapping("/quantidade-clientes")
    public int getQuantidadeClientes() {
        return relatorioService.calcularQuantidadeClientes();
    }

    // Endpoint para retornar a quantidade total de vendas
    @GetMapping("/quantidade-vendas")
    public int getQuantidadeVendas() {
        return relatorioService.calcularQuantidadeVendas();
    }

    // Endpoint para retornar o valor total de vendas
    @GetMapping("/total-vendas")
    public BigDecimal getTotalVendas() {
        return relatorioService.calcularTotalVendas();
    }

    // Endpoint para retornar o valor total de vendas mensal
    @GetMapping("/total-vendas-mensal")
    public List<VendasMensaisDTO> getTotalVendasMensal() {
        return relatorioService.calcularTotalVendasPorMes();
    }

    // Endpoint para retornar o funcionário destaque de cada mês
    @GetMapping("/funcionarios-destaque")
    public List<FuncionarioDestaque> getFuncionariosDestaque() {
        return relatorioService.calcularFuncionarioDestaquePorMes();
    }

    // Endpoint para retornar os clientes aniversariantes do mês
    @GetMapping("/aniversariantes-do-mes")
    public List<String> listarAniversariantes() {
        return relatorioService.listarClientesAniversariantesDoMes();
    }

    // Endpoint para retornar o valor total de vendas por cliente
    @GetMapping("/vendas-por-cliente")
    public List<String> listarTotalVendasPorCliente() {
        return relatorioService.calcularTotalVendasPorCliente();
    }

    // Endpoint para retornar o valor total de vendas por funcionários
    @GetMapping("/vendas-por-funcionarios")
    public List<String> listarTotalVendasPorFuncionario() {
        return relatorioService.calcularTotalVendasPorFuncionario();
    }

    // Endpoint para retornar o nome do cliente que mais comprou na história da empresa
    @GetMapping("/cliente-maior-comprador")
    public String obterClienteQueMaisComprou() {
        return relatorioService.clienteQueMaisComprou();
    }

    // Endpoint para retornar os 3 funcionários que possuem mais tempo de trabalho na empresa
    @GetMapping("/funcionarios-mais-tempo")
    public List<Funcionario> listarTresFuncionariosComMaisTempo() {
        return relatorioService.listarTresFuncionariosComMaisTempo();
    }

    // Endpoint para retornar o nome dos funcionários que mais venderam
    @GetMapping("/funcionarios-mais-vendedores")
    public List<Funcionario> listarFuncionariosMaisVendedores() {
        return relatorioService.listarFuncionariosMaisVendedores();
    }

}
