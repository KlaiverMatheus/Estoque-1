package com.loja; 
// Define o pacote onde a classe está organizada (estrutura lógica do projeto)

import java.sql.Connection;
import java.sql.DriverManager; 
// Importa a classe DriverManager, responsável por gerenciar e criar conexões com o banco

public class Conexao { 
// Declaração da classe pública chamada Conexao

    public static Connection conectar() { 
    // Método público, estático e que retorna um objeto do tipo Connection
    // Ele será responsável por criar e retornar a conexão com o banco

        try { 
        // Bloco try para tentar executar a conexão e tratar possíveis erros

            String url = "jdbc:mysql://localhost:3306/Estoque1"; 
            // URL de conexão com o banco
            // jdbc:mysql:// -> indica que é conexão JDBC com MySQL
            // localhost -> servidor (máquina local)
            // 3306 -> porta padrão do MySQL
            // Nome do Banco -> nome do banco de dados

            String user = "root"; 
            // Nome do usuário do banco de dados

            String pass = "root"; 
            // Senha do banco de dados
            
            return DriverManager.getConnection(url, user, pass); 
            // Cria e retorna a conexão usando a URL, usuário e senha
            
        } catch (Exception e) { 
        // Caso ocorra algum erro na tentativa de conexão

            System.out.println("Deu ruim na conexao: " + e.getMessage()); 
            // Exibe no console a mensagem de erro

            return null; 
            // Retorna null caso a conexão falhe
            
        }
    }
}
