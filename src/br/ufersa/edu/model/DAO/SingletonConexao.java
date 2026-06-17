package br.ufersa.edu.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConexao {

    private static Connection instancia;

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/poo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "poo";
    private static final String PASS = "melhormateria";

    private SingletonConexao() { }

    public static Connection getInstancia() {
        try {
            if (instancia == null || instancia.isClosed()) {
                synchronized (SingletonConexao.class) {
                    if (instancia == null || instancia.isClosed()) {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        instancia = DriverManager.getConnection(URL, USER, PASS);
                        System.out.println("[Singleton] Conexao aberta com o MySQL.");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Falha ao conectar ao banco de dados: " + e.getMessage(), e);
        }
        return instancia;
    }

    public static void fechar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                System.out.println("[Singleton] Conexao encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexao: " + e.getMessage());
        }
    }
}
