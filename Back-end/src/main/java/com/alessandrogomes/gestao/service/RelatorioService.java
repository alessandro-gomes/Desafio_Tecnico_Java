package com.alessandrogomes.gestao.service;

import com.alessandrogomes.gestao.dto.VendasMensaisDTO;
import com.alessandrogomes.gestao.model.Cliente;
import com.alessandrogomes.gestao.model.Funcionario;
import com.alessandrogomes.gestao.model.FuncionarioDestaque;
import com.alessandrogomes.gestao.model.Venda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioService {

    @Autowired
    private ArquivoService arquivoService;

    public int calcularQuantidadeFuncionarios() {
        return arquivoService.getFuncionarios().size();
    }

    public int calcularQuantidadeClientes() {
        return arquivoService.getClientes().size();
    }

    public int calcularQuantidadeVendas() {
        return arquivoService.getVendas().size();
    }

    public BigDecimal calcularTotalVendas() {
        return arquivoService.getVendas().stream()
                .map(Venda::getValorVenda)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<VendasMensaisDTO> calcularTotalVendasPorMes() {
        List<Venda> vendas = arquivoService.getVendas();
        List<VendasMensaisDTO> listaMensal = new ArrayList<>();

        for (Venda venda : vendas) {
            // Formatar a chave como "YYYY-MM"
            String mes = venda.getDataVenda().getYear() + "-" + String.format("%02d", venda.getDataVenda().getMonthValue());
            boolean encontrado = false;

            // Verifica se o mês já está na lista
            for (VendasMensaisDTO vendasMensaisDTO : listaMensal) {
                if (vendasMensaisDTO.getMes().equals(mes)) {
                    // Se encontrado, adiciona o valor ao total existente
                    BigDecimal novoTotal = vendasMensaisDTO.getTotal().add(venda.getValorVenda());
                    vendasMensaisDTO.setTotal(novoTotal); // Atualiza o total no DTO existente
                    encontrado = true;
                    break;
                }
            }

            // Se não encontrado, adiciona um novo objeto VendasMensaisDTO com o valor inicial
            if (!encontrado) {
                listaMensal.add(new VendasMensaisDTO(mes, venda.getValorVenda()));
            }
        }

        return listaMensal;
    }

    public List<FuncionarioDestaque> calcularFuncionarioDestaquePorMes() {
        List<Venda> vendas = arquivoService.getVendas();
        List<Funcionario> funcionarios = arquivoService.getFuncionarios();
        List<FuncionarioDestaque> destaquesMensais = new ArrayList<>();

        // Lista para armazenar os meses que já foram processados
        List<String> mesesProcessados = new ArrayList<>();

        // Iterar sobre todas as vendas
        for (Venda venda : vendas) {
            // Extrair o mês da venda (no formato "yyyy-MM")
            String mesAtual = venda.getDataVenda().format(DateTimeFormatter.ofPattern("yyyy-MM"));

            // Verificar se já processamos este mês
            if (!mesesProcessados.contains(mesAtual)) {
                // Se o mês ainda não foi processado, processe-o
                BigDecimal maiorValor = BigDecimal.ZERO;
                Funcionario funcionarioDestaque = null;

                // Iterar novamente sobre as vendas para comparar todas as vendas desse mês
                for (Venda vendaComparada : vendas) {
                    String mesVendaComparada = vendaComparada.getDataVenda().format(DateTimeFormatter.ofPattern("yyyy-MM"));

                    if (mesVendaComparada.equals(mesAtual)) {
                        // Encontrar o funcionário da venda
                        Funcionario funcionario = buscarFuncionarioPorId(funcionarios, vendaComparada.getIdFuncionario());

                        if (funcionario != null) {
                            // Comparar o valor de vendas do funcionário
                            if (vendaComparada.getValorVenda().compareTo(maiorValor) > 0) {
                                maiorValor = vendaComparada.getValorVenda();
                                funcionarioDestaque = funcionario;
                            }
                        }
                    }
                }

                // Se encontramos um funcionário destaque, adicioná-lo à lista
                if (funcionarioDestaque != null) {
                    FuncionarioDestaque destaque = new FuncionarioDestaque(
                            mesAtual,
                            funcionarioDestaque.getId(),
                            funcionarioDestaque.getNome(),
                            funcionarioDestaque.getDataContratacao(),
                            maiorValor
                    );
                    destaquesMensais.add(destaque);
                }

                // Marcar o mês como processado
                mesesProcessados.add(mesAtual);
            }
        }

        return destaquesMensais;
    }

    // metodo auxiliar para buscar o funcionário pelo seu ID
    private Funcionario buscarFuncionarioPorId(List<Funcionario> funcionarios, int id) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId() == id) {
                return funcionario;
            }
        }
        return null; // Retorna null se não encontrar
    }

    public List<String> listarClientesAniversariantesDoMes() {
        List<Cliente> clientes = arquivoService.getClientes();
        List<String> aniversariantes = new ArrayList<>();

        // Obter o mês atual
        int mesAtual = LocalDate.now().getMonthValue();
        String nomeMesAtual = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());

        // Iterar sobre os clientes e filtrar os aniversariantes do mês
        for (Cliente cliente : clientes) {
            int mesAniversario = cliente.getDataNascimento().getMonthValue();

            if (mesAniversario == mesAtual) {
                // Calcular idade e formatar as informações
                int idade = calcularIdade(cliente.getDataNascimento());
                String detalhes = formatarDetalhesAniversariante(cliente, idade, nomeMesAtual);
                aniversariantes.add(detalhes);
            }
        }

        return aniversariantes;
    }

    // Método auxiliar para calcular a idade
    private int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    // Método auxiliar para formatar os detalhes do aniversariante
    private String formatarDetalhesAniversariante(Cliente cliente, int idade, String nomeMes) {
        return "Mês: " + nomeMes
                + " | Nome: " + cliente.getNome()
                + " | Idade: " + idade
                + " | Dia do Aniversário: " + cliente.getDataNascimento().getDayOfMonth();
    }

    public List<String> calcularTotalVendasPorCliente() {
        List<Cliente> clientes = arquivoService.getClientes();
        List<Venda> vendas = arquivoService.getVendas();
        List<String> totalVendasPorCliente = new ArrayList<>();

        // Iterar sobre os clientes
        for (Cliente cliente : clientes) {
            BigDecimal totalGasto = BigDecimal.ZERO;

            // Iterar sobre as vendas para calcular o total gasto pelo cliente
            for (Venda venda : vendas) {
                if (venda.getIdCliente() == cliente.getId()) {
                    totalGasto = totalGasto.add(venda.getValorVenda());
                }
            }

            // Adicionar o resultado na lista formatada
            String resultado = "Cliente: " + cliente.getNome()
                    + ", Total gasto: " + totalGasto;
            totalVendasPorCliente.add(resultado);
        }

        return totalVendasPorCliente;
    }

    public List<String> calcularTotalVendasPorFuncionario() {
        List<Funcionario> funcionarios = arquivoService.getFuncionarios();
        List<Venda> vendas = arquivoService.getVendas();
        List<String> totalVendasPorFuncionario = new ArrayList<>();

        // Iterar sobre os funcionários
        for (Funcionario funcionario : funcionarios) {
            BigDecimal totalVendas = BigDecimal.ZERO;

            // Iterar sobre as vendas para calcular o total de vendas do funcionário
            for (Venda venda : vendas) {
                if (venda.getIdFuncionario() == funcionario.getId()) {
                    totalVendas = totalVendas.add(venda.getValorVenda());
                }
            }

            // Adicionar o resultado na lista formatada
            String resultado = "Funcionário: " + funcionario.getNome()
                    + ", Total de vendas: " + totalVendas;
            totalVendasPorFuncionario.add(resultado);
        }

        return totalVendasPorFuncionario;
    }

    public String clienteQueMaisComprou() {
        List<Cliente> clientes = arquivoService.getClientes();
        List<Venda> vendas = arquivoService.getVendas();

        Cliente clienteComMaisCompras = null;
        BigDecimal maiorTotal = BigDecimal.ZERO;

        for (Cliente cliente : clientes) {
            BigDecimal totalGasto = BigDecimal.ZERO;

            // Calcular o total gasto por cliente
            for (Venda venda : vendas) {
                if (venda.getIdCliente() == cliente.getId()) {
                    totalGasto = totalGasto.add(venda.getValorVenda());
                }
            }

            // Verificar se este cliente é o que mais gastou até agora
            if (totalGasto.compareTo(maiorTotal) > 0) {
                maiorTotal = totalGasto;
                clienteComMaisCompras = cliente;
            }
        }

        if (clienteComMaisCompras != null) {
            return clienteComMaisCompras.getNome();
        } else {
            return "Nenhum cliente encontrado.";
        }
    }

    public List<Funcionario> listarTresFuncionariosComMaisTempo() {
        List<Funcionario> funcionarios = arquivoService.getFuncionarios();

        // Ordena os funcionários por data de contratação (mais antigos primeiro)
        funcionarios.sort(Comparator.comparing(Funcionario::getDataContratacao));

        // Retorna os três primeiros funcionários
        return funcionarios.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Funcionario> listarFuncionariosMaisVendedores() {
        List<Venda> vendas = arquivoService.getVendas();
        List<Funcionario> funcionarios = arquivoService.getFuncionarios();

        // Reseta o total de vendas de cada funcionário para zero
        for (Funcionario funcionario : funcionarios) {
            funcionario.setTotalVendas(BigDecimal.ZERO);
        }

        // Calcula o total de vendas para cada funcionário
        for (Venda venda : vendas) {
            for (Funcionario funcionario : funcionarios) {
                if (funcionario.getId() == venda.getIdFuncionario()) {
                    funcionario.adicionarVendas(venda.getValorVenda());
                    break; // Já encontrou o funcionário, pode sair do loop interno
                }
            }
        }

        // Ordena os funcionários pelo total de vendas em ordem decrescente
        Collections.sort(funcionarios, Comparator.comparing(Funcionario::getTotalVendas).reversed());

        return funcionarios;
    }
}
