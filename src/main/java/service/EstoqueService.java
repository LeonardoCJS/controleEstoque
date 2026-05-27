package service;

import dao.ProdutoDAO;
import exception.EstoqueException;
import exception.ProdutoNaoEncontradoException;
import model.Produto;

import java.math.BigDecimal;
import java.util.List;

public class EstoqueService {
    private final ProdutoDAO produtoDAO;

    public EstoqueService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void inserirProduto(Produto produto){
        if (produto == null){
            throw new EstoqueException("Produto nao pode ser nulo.");
        }
        boolean nomeExiste = produtoDAO.buscarPorNome(produto.getNome())
                .stream()
                .anyMatch(p -> p.getNome().equalsIgnoreCase(produto.getNome()));
        if (nomeExiste){
            throw new EstoqueException("Já existe um produto com esse nome.");
        }
        produtoDAO.salvar(produto);
    }

    public void deletarProduto(Long id){
        if (id == null){
            throw new EstoqueException("Id nao pode ser nulo.");
        }

        produtoDAO.buscarPorId(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto nao encontrado. Id: " + id));
        produtoDAO.deletar(id);
    }

    public void atualizarProduto(Produto produto){
        if (produto == null){
            throw new EstoqueException("Produto nao pode ser nulo.");
        }
        produtoDAO.buscarPorId(produto.getId()).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto nao encontrado."));
        produtoDAO.atualizar(produto);
    }

    public Produto buscarPorId(Long id){
        if (id == null){
            throw new EstoqueException("Id nao pode ser nulo.");
        }
        return produtoDAO.buscarPorId(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto nao encontrado. Id: " + id));
    }

    public List<Produto> buscarPorNome(String nome){
        if (nome == null){
            throw new EstoqueException("Nome nao pode ser nulo.");
        }

        List<Produto> produtos = produtoDAO.buscarPorNome(nome);

        return produtos;
    }

    public List<Produto> getAllProdutos(){
        return produtoDAO.listarTodos();
    }

    public void entradaEstoque(Long id, int quantidade){
        if (id == null || quantidade <= 0){
            throw new EstoqueException("Produto ou quantidade nao podem ser nulos.");
        }
        Produto produto = produtoDAO.buscarPorId(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto nao encontrado."));
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        produtoDAO.atualizar(produto);
    }

    public void saidaEstoque(Long id, int quantidade){
        if (id == null || quantidade <= 0){
            throw new EstoqueException("Produto ou quantidade nao podem ser nulos.");
        }
        Produto produto = produtoDAO.buscarPorId(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto nao encontrado."));
        if(produto.getQuantidade() < quantidade){
            throw new EstoqueException("Quantidade insuficiente em estoque.");
        }
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        produtoDAO.atualizar(produto);
    }

    public void relatorioEstoque(){
        System.out.println("===== Relatório de Estoque =====");
        List<Produto> todosProdutos = produtoDAO.listarTodos();
        System.out.println("Total de produtos: " + todosProdutos.size());

        BigDecimal valorTotal = todosProdutos.stream()
                .map(p -> p.getPreco().multiply(new BigDecimal(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Valor total em estoque: R$" + valorTotal);

        List<Produto> estoqueBaixo = produtoDAO.estoqueBaixo(5);
        System.out.println("Produtos com estoque baixo:");
        if(estoqueBaixo.isEmpty()){
            System.out.println("Nenhum produto com estoque baixo!");
        }else{
            estoqueBaixo.forEach(produto -> System.out.println(" -" + produto.getNome() + " (" + produto.getQuantidade() + " un.)"));
        }

    }


}
