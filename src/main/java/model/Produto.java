package model;

import model.enums.Categoria;
import java.math.BigDecimal;

public class Produto {
    private final Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade = 0;
    private Categoria categoria;

    public Produto(Long id, String nome, String descricao, BigDecimal preco, int quantidade, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public Produto(String nome, String descricao, BigDecimal preco, int quantidade, Categoria categoria) {
        this.id = null;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Produto setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getDescricao() {
        return descricao;
    }

    public Produto setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Produto setPreco(BigDecimal preco) {
        this.preco = preco;
        return this;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Produto setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Produto setCategoria(Categoria categoria) {
        this.categoria = categoria;
        return this;
    }

    @Override
    public String toString() {
        return "Nome: " + nome +
                "\nDescrição: " + descricao +
                "\nPreço: " + preco +
                "\nQuantidade: " + quantidade +
                "\nCategoria: " + categoria;
    }
}
