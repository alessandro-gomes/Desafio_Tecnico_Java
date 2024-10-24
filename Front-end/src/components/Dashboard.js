import React, { useState, useEffect } from 'react';
import $ from 'jquery';

const Dashboard = () => {
    const [quantidadeFuncionarios, setQuantidadeFuncionarios] = useState('');
    const [quantidadeClientes, setQuantidadeClientes] = useState('');
    const [quantidadeVendas, setQuantidadeVendas] = useState('');
    const [totalVendas, setTotalVendas] = useState('');
    const [clienteMaiorComprador, setClienteMaiorComprador] = useState('');
    const [vendasMensais, setVendasMensais] = useState([]);
    const [funcionariosDestaque, setFuncionariosDestaque] = useState([]);
    const [aniversariantes, setAniversariantes] = useState([]);
    const [vendasPorCliente, setVendasPorCliente] = useState([]);
    const [vendasPorFuncionario, setVendasPorFuncionario] = useState([]);
    const [funcionarios, setFuncionarios] = useState([]);
    const [funcionariosMaisVendedores, setFuncionariosMaisVendedores] = useState([]);



    useEffect(() => {
        // Requisição para obter a quantidade de funcionários
        $.get('http://localhost:8080/gestao/quantidade-funcionarios', (data) => {
            setQuantidadeFuncionarios(data);
        });

        // Requisição para obter a quantidade de clientes
        $.get('http://localhost:8080/gestao/quantidade-clientes', (data) => {
            setQuantidadeClientes(data);
        });

        // Requisição para obter a quantidade de vendas
        $.get('http://localhost:8080/gestao/quantidade-vendas', (data) => {
            setQuantidadeVendas(data);
        });

        // Requisição para obter o valor total de vendas
        $.get('http://localhost:8080/gestao/total-vendas', (data) => {
            // Formata o valor recebido como moeda brasileira (BRL) e atualiza o estado
            const totalVendasFormatado = formatarValor(data);
            setTotalVendas(totalVendasFormatado);
        });

        // Requisição para obter as vendas mensais
        $.get('http://localhost:8080/gestao/total-vendas-mensal', (data) => {
            setVendasMensais(data);
        });

        // Requisição para obter o cliente maior comprador da empresa
        $.get('http://localhost:8080/gestao/cliente-maior-comprador', (data) => {
            setClienteMaiorComprador(data);
        });

        // Requisição para obter o funcionario destaque de cada mês 
        $.get('http://localhost:8080/gestao/funcionarios-destaque', (data) => {
            setFuncionariosDestaque(data);
        });

        // Requisição para obter o cliente aniversariante do mês
        $.get('http://localhost:8080/gestao/aniversariantes-do-mes', (data) => {
            setAniversariantes(data);
        });

        // Requisição para obter o total de vendas por cliente
        $.get('http://localhost:8080/gestao/vendas-por-cliente', (data) => {
            const clientes = data.map((item) => {
                // Usar regex para extrair nome e total gasto da string
                const regex = /Cliente: (.*), Total gasto: (.*)/;
                const matches = regex.exec(item);

                if (matches) {
                    const nome = matches[1]; // Nome do cliente
                    const totalGasto = parseFloat(matches[2]); // Total gasto como número

                    // Usar a função formatarValor para formatar o total gasto
                    const totalGastoFormatado = formatarValor(totalGasto);

                    return { nome, totalGasto: totalGastoFormatado }; // Retorna um objeto com nome e total formatado
                }
                return null; // Retorna null se a correspondência falhar
            });

            setVendasPorCliente(clientes);
        });

        // Requisição para obter o total de vendas por funcionário
        $.get('http://localhost:8080/gestao/vendas-por-funcionarios', (data) => {
            const funcionarios = data.map((item) => {
                // Usar regex para extrair nome e total de vendas da string
                const regex = /Funcionário: (.*), Total de vendas: (.*)/;
                const matches = regex.exec(item);

                if (matches) {
                    const nome = matches[1]; // Nome do funcionário
                    const totalVendas = parseFloat(matches[2]); // Total de vendas como número

                    // Usar a função formatarValor para formatar o total de vendas
                    const totalVendasFormatado = formatarValor(totalVendas);

                    return { nome, totalVendas: totalVendasFormatado }; // Retorna um objeto com nome e total formatado
                }
                return null; // Retorna null se a correspondência falhar
            });

            setVendasPorFuncionario(funcionarios);
        });

        // Requisição para obter os 3 funcionário com mais tempo de trabalho
        $.get('http://localhost:8080/gestao/funcionarios-mais-tempo', (data) => {
            setFuncionarios(data);
        });

        // Requisição para obter os funcionários que mais venderam
        $.get('http://localhost:8080/gestao/funcionarios-mais-vendedores', (data) => {
            setFuncionariosMaisVendedores(data);
        });
    }, []);



    // Função para formatar o valor da moeda em R$
    const formatarValor = (valor) => {
        return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    };

    // Função para formatar o mês (ex: '2019-02' para 'Fevereiro 2019')
    const formatarMes = (mesAno) => {
        const [ano, mes] = mesAno.split('-');
        const meses = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
        return `${meses[parseInt(mes) - 1]} ${ano}`;
    };

    // Função para formatar a data (ex: DD/MM/AAAA)
    const formatarData = (data) => {
        const [ano, mes, dia] = data.split('-');
        return `${dia}/${mes}/${ano}`;
    };



    return (
        <div>
            <h2>Quantidade de Funcionários:</h2>
            <p style={{ marginBottom: '10px', lineHeight: '1.5' }}>{quantidadeFuncionarios}</p>

            <h2>Quantidade de Clientes:</h2>
            <p style={{ marginBottom: '10px', lineHeight: '1.5' }}>{quantidadeClientes}</p>

            <h2>Quantidade de Vendas:</h2>
            <p style={{ marginBottom: '10px', lineHeight: '1.5' }}>{quantidadeVendas}</p>

            <h2>Valor Total de Vendas:</h2>
            <p style={{ marginBottom: '10px', lineHeight: '1.5' }}>{totalVendas}</p>

            <h2>Total de Vendas por Mês:</h2>
            <ul style={{ listStyle: 'none', padding: 0 }}>
                {vendasMensais.map((venda, index) => (
                    <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                        {formatarMes(venda.mes)}: {formatarValor(venda.total)}
                    </li>
                ))}
            </ul>

            <h2>Funcionário Destaque de Cada Mês:</h2>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {funcionariosDestaque.map((funcionario, index) => (
                    <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                        ID: {funcionario.id} | {funcionario.nome} | Mês: {formatarMes(funcionario.mes)}
                    </li>
                ))}
            </ul>

            <h2>Clientes que Fazem Aniversário no Mês:</h2>
            {aniversariantes.length === 0 ? (
                <p style={{ marginBottom: '10px', lineHeight: '1.5' }}>Nenhum cliente faz aniversário este mês.</p>
            ) : (
                <ul style={{ listStyleType: 'none', padding: 0 }}>
                    {aniversariantes.map((aniversariante, index) => (
                        <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                            {aniversariante}
                        </li>
                    ))}
                </ul>
            )}

            <h2>Total de Vendas por Cliente:</h2>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {vendasPorCliente.map((cliente, index) => (
                    <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                        Cliente: {cliente.nome} | Total Gasto: {cliente.totalGasto}
                    </li>
                ))}
            </ul>

            <h2>Total de Vendas por Funcionário:</h2>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {vendasPorFuncionario.map((funcionario, index) => (
                    <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                        Funcionário: {funcionario.nome} | Total de Vendas: {funcionario.totalVendas}
                    </li>
                ))}
            </ul>

            <h2>Cliente com Maior Compra:</h2>
            <p style={{ marginBottom: '10px', lineHeight: '1.5' }}>{clienteMaiorComprador}</p>

            <h2>Funcionários com Mais Tempo de Trabalho:</h2>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {funcionarios.map((funcionario, index) => (
                    <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                        Nome: {funcionario.nome} | Data de Contratação: {formatarData(funcionario.dataContratacao)}
                    </li>
                ))}
            </ul>

            <h2>Funcionários com Mais Vendas:</h2>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {funcionariosMaisVendedores.map((funcionario, index) => (
                    <li key={index} style={{ marginBottom: '10px', lineHeight: '1.5' }}>
                        Nome: {funcionario.nome} | Total de Vendas: {formatarValor(funcionario.totalVendas)}
                    </li>
                ))}
            </ul>
        </div >
    );
};

export default Dashboard;
