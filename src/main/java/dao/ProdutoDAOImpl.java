package dao;

import db.ConnectionFactory;
import exception.DatabaseException;
import model.Produto;
import model.enums.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOImpl implements ProdutoDAO {

    @Override
    public void salvar(Produto produto){
        String sql = "INSERT INTO produtos (nome, descricao, preco, quantidade, categoria) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setString(5, produto.getCategoria().name());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new DatabaseException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id){
        Optional<Produto> produto = Optional.empty();
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    Produto p = mapearProduto(rs);
                    produto = Optional.of(p);
                }
            }
        }catch (SQLException e){
            throw new DatabaseException("Erro ao buscar produto: " + e.getMessage());
        }
        return produto;
    }

    @Override
    public List<Produto> listarTodos(){
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try(Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Produto p = mapearProduto(rs);
                produtos.add(p);
            }

        }catch (SQLException e){
            throw new DatabaseException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    @Override
    public List<Produto> buscarPorNome(String nome){
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, "%"+nome+"%");
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    Produto p = mapearProduto(rs);
                    produtos.add(p);
                }
            }

        }catch (SQLException e){
            throw new DatabaseException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    @Override
    public void atualizar(Produto produto){
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, preco = ?, quantidade = ?, categoria = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setString(5, produto.getCategoria().name());
            pstmt.setLong(6, produto.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new DatabaseException("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @Override
    public void deletar(Long id){
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new DatabaseException("Erro ao deletar produto: " + e.getMessage());
        }
    }

    @Override
    public List<Produto> estoqueBaixo(int minimo) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE quantidade < ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, minimo);
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    Produto p = mapearProduto(rs);
                    produtos.add(p);
                }
            }

        }catch (SQLException e){
            throw new DatabaseException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException{
        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getBigDecimal("preco"),
                rs.getInt("quantidade"),
                Categoria.valueOf(rs.getString("categoria"))
        );
    }
}
