package db;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializar(){
        String sql = """
                CREATE TABLE IF NOT EXISTS produtos(
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nome varchar(100) NOT NULL,
                descricao varchar(255),
                preco DECIMAL(10,2) NOT NULL,
                quantidade INT NOT NULL DEFAULT 0,
                categoria varchar(50) NOT NULL
                )
                """;

        try(Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        }catch (SQLException e){
            throw new DatabaseException("Erro ao inicializar banco: " + e.getMessage());
        }
    }
}
