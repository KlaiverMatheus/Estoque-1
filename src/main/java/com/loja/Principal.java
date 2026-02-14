package com.loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {

        // Scanner para ler dados do usuário
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE ESTOQUE ===");

        // Loop infinito até o usuário escolher sair
        while (true) {

            // Menu principal
            System.out.println("\nMENU:");
            System.out.println("1 - Cadastrar novo produto");
            System.out.println("2 - Adicionar quantidade (Entrada)");
            System.out.println("3 - Subtrair quantidade (Saída)");
            System.out.println("4 - Listar produtos");
            System.out.println("5 - Sair");
            System.out.println("6 - Excluir produto");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();

            if (opcao == 1) {

                scanner.nextLine(); // limpa o ENTER pendente

                System.out.print("Digite o nome do produto: ");
                String nome = scanner.nextLine();

                System.out.print("Digite a quantidade inicial: ");
                int qtd = scanner.nextInt();
                
                // Comando SQL para inserir um novo registro na tabela "estoque".
                // INSERT INTO estoque (nome, quantidade) → diz em qual tabela e quais colunas vamos inserir dados.
                // VALUES (?, ?) → os "?" são placeholders (parâmetros) que serão substituídos
                // posteriormente pelos valores informados pelo usuário usando PreparedStatement.
                // O primeiro "?" será o nome do produto e o segundo "?" será a quantidade inicial.
                // Usar "?" evita SQL Injection e torna o código mais seguro.
                String sql = "INSERT INTO estoque (nome, quantidade) VALUES (?, ?)";


                try {
                    Connection con = Conexao.conectar();

                    // PreparedStatement evita SQL Injection
                    PreparedStatement stmt = con.prepareStatement(sql);

                    // Define os valores dos "?"
                    stmt.setString(1, nome);
                    stmt.setInt(2, qtd);

                    int linhas = stmt.executeUpdate();

                    if (linhas > 0) {
                        System.out.println(">>> Produto cadastrado com sucesso!");
                    }

                    stmt.close();
                    con.close();

                } catch (Exception e) {
                    System.out.println("Erro ao cadastrar: " + e.getMessage());
                }
            }

            else if (opcao == 2) {

                System.out.print("Digite o ID do produto: ");
                int id = scanner.nextInt();

                System.out.print("Digite a quantidade que entrou: ");
                int qtd = scanner.nextInt();

                String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE produto_id = ?";

                try {
                    Connection con = Conexao.conectar();
                    PreparedStatement stmt = con.prepareStatement(sql);

                    stmt.setInt(1, qtd);
                    stmt.setInt(2, id);

                    int linhas = stmt.executeUpdate();

                    if (linhas > 0) {
                        System.out.println(">>> Estoque atualizado com sucesso!");
                    } else {
                        System.out.println("Produto não encontrado!");
                    }

                    stmt.close();
                    con.close();

                } catch (Exception e) {
                    System.out.println("Erro ao atualizar: " + e.getMessage());
                }
            }

            else if (opcao == 3) {

                System.out.print("Digite o ID do produto: ");
                int id = scanner.nextInt();

                System.out.print("Digite a quantidade que saiu: ");
                int qtd = scanner.nextInt();

                // Impede que o estoque fique negativo
                // Comando SQL para atualizar (UPDATE) a quantidade de um produto no estoque.
                // SET quantidade = quantidade - ? → diminui a quantidade atual do produto
                // subtraindo o valor informado pelo usuário.
                // WHERE produto_id = ? → garante que apenas o produto com o ID informado será alterado.
                // AND quantidade >= ? → condição de segurança que impede que o estoque fique negativo,
                // só permitindo a subtração se houver quantidade suficiente disponível.
                // Os "?" são parâmetros que serão preenchidos via PreparedStatement,
                // garantindo segurança contra SQL Injection.
                String sql = "UPDATE estoque SET quantidade = quantidade - ? "
                    + "WHERE produto_id = ? AND quantidade >= ?";

                try {
                    Connection con = Conexao.conectar();
                    PreparedStatement stmt = con.prepareStatement(sql);

                    stmt.setInt(1, qtd);
                    stmt.setInt(2, id);
                    stmt.setInt(3, qtd);

                    int linhas = stmt.executeUpdate();

                    if (linhas > 0) {
                        System.out.println(">>> Baixa realizada com sucesso!");
                    } else {
                        System.out.println("Estoque insuficiente ou produto não encontrado!");
                    }

                    stmt.close();
                    con.close();

                } catch (Exception e) {
                    System.out.println("Erro ao dar baixa: " + e.getMessage());
                }
            }

            else if (opcao == 4) {

                // Comando SQL para buscar todos os registros da tabela "estoque".
                // SELECT * → seleciona todas as colunas da tabela.
                // FROM estoque → indica de qual tabela os dados serão buscados.
                String sql = "SELECT * FROM estoque";

                try {
                    // Abre conexão com o banco de dados
                    Connection con = Conexao.conectar();

                    // Prepara o comando SQL para execução
                    PreparedStatement stmt = con.prepareStatement(sql);

                    // Executa a consulta (query) e armazena o resultado em um ResultSet
                    // ResultSet funciona como uma "tabela em memória" com os dados retornados
                    ResultSet rs = stmt.executeQuery();

                    System.out.println("\n--- LISTA DE PRODUTOS ---");

                    // Percorre cada linha retornada pela consulta
                    // rs.next() move para o próximo registro e retorna true enquanto houver dados
                    while (rs.next()) {

                        // Recupera os valores de cada coluna pelo nome da coluna no banco
                        // getInt("produto_id") → pega o ID
                        // getString("nome") → pega o nome
                        // getInt("quantidade") → pega a quantidade
                        System.out.println(
                            "ID: " + rs.getInt("produto_id") +
                            " | Nome: " + rs.getString("nome") +
                            " | Quantidade: " + rs.getInt("quantidade")
                        );
                    }

                    // Fecha os recursos para evitar vazamento de memória/conexão
                    rs.close();
                    stmt.close();
                    con.close();

                } catch (Exception e) {
                    // Caso aconteça algum erro durante a consulta
                    System.out.println("Erro ao listar: " + e.getMessage());
                }
            }

            else if (opcao == 5) {
                System.out.println("Encerrando sistema...");
                break;
            }

            else if (opcao == 6) {

            System.out.print("Digite o ID do produto: ");
            int id = scanner.nextInt();

            System.out.print("Tem certeza que quer excluir esse produto? (1 - Sim / 2 - Não): ");
            int escolha = scanner.nextInt();

            if (escolha == 1) {

                String sql = "DELETE FROM estoque WHERE produto_id = ?";

                try {
                    Connection con = Conexao.conectar();
                    PreparedStatement stmt = con.prepareStatement(sql);

                    // Como só existe um ?, o índice é 1
                    stmt.setInt(1, id);

                    int linhas = stmt.executeUpdate();

                    if (linhas > 0) {
                        System.out.println("Produto excluído com sucesso!");
                    } else {
                        System.out.println("Produto não encontrado!");
                    }

                    stmt.close();
                    con.close();

                } catch (Exception e) {
                    System.out.println("Erro ao excluir: " + e.getMessage());
                }

                continue;

            } else if (escolha == 2) {
                System.out.println("Exclusão cancelada.");
                continue;
            } else {
                System.out.println("Escolha inválida!");
                continue;
            }
        }


            // Caso digite algo inválido
            else {
                System.out.println("Opção inválida!");
            }
        }

        // Fecha o scanner
        scanner.close();
    }
}
