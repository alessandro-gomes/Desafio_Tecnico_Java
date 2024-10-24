package com.alessandrogomes.gestao.service;

import com.alessandrogomes.gestao.model.Cliente;
import com.alessandrogomes.gestao.model.Funcionario;
import com.alessandrogomes.gestao.model.Venda;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class ArquivoService {

    private static final Logger logger = Logger.getLogger(ArquivoService.class.getName());
    private final String caminhoArquivo = "src/main/resources/files/dados.txt";

    private List<Funcionario> funcionarios = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private List<Venda> vendas = new ArrayList<>();

    public ArquivoService() {
        lerArquivo();
    }

    public void lerArquivo() {

        /* Usando try-with-resources, não precisamos fechar o BufferedReader ao final do bloco try. Ele 
         * se responsabiliza por isso e garante que seja fechado corretamente, mesmo em caso de exceções.
         */
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\\|");

                switch (dados[0]) {
                    case "0": // Funcionario
                        Funcionario funcionario = new Funcionario(
                                Integer.parseInt(dados[1]),
                                dados[2],
                                LocalDate.parse(dados[3], formatter)
                        );
                        funcionarios.add(funcionario);
                        break;
                    case "1": // Cliente
                        Cliente cliente = new Cliente(
                                Integer.parseInt(dados[1]),
                                dados[2],
                                LocalDate.parse(dados[3], formatter)
                        );
                        clientes.add(cliente);
                        break;
                    case "2": // Venda
                        Venda venda = new Venda(
                                Integer.parseInt(dados[1]),
                                Integer.parseInt(dados[2]),
                                Integer.parseInt(dados[3]),
                                new BigDecimal(dados[4].replace(",", ".")),
                                LocalDate.parse(dados[5], formatter)
                        );
                        vendas.add(venda);
                        break;
                    default:
                        System.out.println("Linha inválida no arquivo.");
                        break;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler o arquivo no caminho: {0}. Detalhes: {1}", new Object[]{caminhoArquivo, e.getMessage()});
        }
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Venda> getVendas() {
        return vendas;
    }
}
