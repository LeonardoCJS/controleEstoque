package dao;


import model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoDAO {

    void salvar(Produto produto);
    Optional<Produto> buscarPorId(Long id);
    List<Produto> listarTodos();
    List<Produto> buscarPorNome(String nome);
    void atualizar(Produto produto);
    void deletar(Long id);
    List<Produto> estoqueBaixo(int minimo);
}
