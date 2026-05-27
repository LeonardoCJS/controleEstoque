package ui;

import exception.EstoqueException;
import exception.ProdutoNaoEncontradoException;
import model.Produto;
import model.enums.Categoria;
import service.EstoqueService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final EstoqueService estoqueService;

    public Menu(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    public void rodar(){
        boolean sair  = false;

        do {
            mostrarMenu();
            String opcao = sc.nextLine().trim();
            switch (opcao) {
                case "1" -> inserirProduto();
                case "2" -> deletarProduto();
                case "3" -> atualizarProduto();
                case "4" -> buscarPorId();
                case "5" -> buscarPorNome();
                case "6" -> getAllProdutos();
                case "7" -> entradaEstoque();
                case "8" -> saidaEstoque();
                case "9" -> relatorioEstoque();
                case "0" -> sair = true;
                default -> System.out.println("Opcao invalida!");
            }
        }while(!sair);
    }

    private void mostrarMenu(){
        System.out.println("====== Sistema de Estoque ======");
        System.out.println("1. Cadastrar Produto");
        System.out.println("2. Remover Produto");
        System.out.println("3. Atualizar Produto");
        System.out.println("4. Buscar Produto por Id");
        System.out.println("5. Buscar Produto por Nome");
        System.out.println("6. Listar Todos os Produtos");
        System.out.println("------Estoque------");
        System.out.println("7. Entrada de Estoque");
        System.out.println("8. Saida de Estoque");
        System.out.println("------Relatorio------");
        System.out.println("9. Relatorio de Estoque");
        System.out.println("-------------------------");
        System.out.println("0. Sair");
        System.out.println("Selecione uma opcao: ");
    }

    private void inserirProduto(){
        try {
            System.out.println("Digite o nome do produto: ");
            String nome = sc.nextLine();
            System.out.println("Digite a descrição do produto: ");
            String descricao = sc.nextLine();
            System.out.println("Digite o preço do produto: ");
            String precoInput = sc.nextLine();
            BigDecimal preco;
            try {
                preco = new BigDecimal(precoInput);
            } catch (NumberFormatException e) {
                throw new EstoqueException(e.getMessage());
            }
            System.out.println("Digite a quantidade do produto: ");
            int quantidade;
            try {
                quantidade = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                throw new EstoqueException("Quantidade inválida.");
            }
            System.out.println("Digite a categoria do produto: ");
            Categoria categoria = lerCategoria();
            estoqueService.inserirProduto(new Produto(nome, descricao, preco,  quantidade, categoria));
            System.out.println("Produto inserido com sucesso!");
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void deletarProduto(){
        try{
            List<Produto> produtos = estoqueService.getAllProdutos();
            if (produtos.isEmpty()){
                System.out.println("Nenhum produto encontrado!");
                return;
            }
            System.out.println("====== Lista de Produtos ======");
            exibirEstoque(produtos);
            System.out.println("Digite o numero do produto que dejesa remover: ");
            int escolha = Integer.parseInt(sc.nextLine()) - 1;
            estoqueService.deletarProduto(produtos.get(escolha).getId());
            System.out.println("Produto removido com sucesso!");
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(NumberFormatException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(IndexOutOfBoundsException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void atualizarProduto(){
        try{
            List<Produto> produtos = estoqueService.getAllProdutos();
            if (produtos.isEmpty()){
                System.out.println("Nenhum produto encontrado!");
                return;
            }
            System.out.println("====== Lista de Produtos ======");
            exibirEstoque(produtos);
            System.out.println("Digite o numero do produto que dejesa atualizar: ");
            int escolha = Integer.parseInt(sc.nextLine()) - 1;

            System.out.println("Digite o nome do produto: ");
            String nome = sc.nextLine();
            System.out.println("Digite a descrição do produto: ");
            String descricao = sc.nextLine();
            System.out.println("Digite o preço do produto: ");
            String precoInput = sc.nextLine();
            BigDecimal preco;
            try {
                preco = new BigDecimal(precoInput);
            } catch (NumberFormatException e) {
                throw new EstoqueException(e.getMessage());
            }
            System.out.println("Digite a quantidade do produto: ");
            int quantidade;
            try {
                quantidade = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                throw new EstoqueException("Quantidade inválida.");
            }
            System.out.println("Digite a categoria do produto: ");
            Categoria categoria = lerCategoria();
            Long id = produtos.get(escolha).getId();

            estoqueService.atualizarProduto(new Produto(id, nome, descricao, preco,  quantidade, categoria));
            System.out.println("Produto atualizado com sucesso!");
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(NumberFormatException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(IndexOutOfBoundsException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarPorId(){
        try{
            System.out.println("Digite o id do produto: ");
            long id;
            try {
                id = Long.parseLong(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: ID inválido.");
                return;
            }
            Produto produto = estoqueService.buscarPorId(id);
            System.out.println("Resultado da busca: " + produto);
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarPorNome(){
        try{
            System.out.println("Digite o nome do produto: ");
            String nome = sc.nextLine();
            List<Produto> produtos = estoqueService.buscarPorNome(nome);
            if(produtos.isEmpty()){
                System.out.println("Produto nao encontrado!");
                return;
            }
            System.out.println("Resultado da busca: ");
            exibirEstoque(produtos);
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void getAllProdutos(){
        List<Produto> produtos = estoqueService.getAllProdutos();
        if(produtos.isEmpty()){
            System.out.println("Nenhum produto encontrado!");
            return;
        }
        System.out.println("====== Lista de Produtos ======");
        exibirEstoque(produtos);
    }

    private void entradaEstoque(){
        try{
            List<Produto> produtos = estoqueService.getAllProdutos();
            if (produtos.isEmpty()){
                System.out.println("Nenhum produto encontrado!");
                return;
            }
            System.out.println("====== Lista de Produtos ======");
            exibirEstoque(produtos);
            System.out.println("Digite o numero do produto que dejesa adicionar no estoque: ");
            int escolha = Integer.parseInt(sc.nextLine()) - 1;

            System.out.println("Digite a quantidade que dejesa adicionar no estoque: ");
            int quantidade = Integer.parseInt(sc.nextLine());

            estoqueService.entradaEstoque(produtos.get(escolha).getId(), quantidade);
            System.out.println("Quantidade atualizada com sucesso!");
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(NumberFormatException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(IndexOutOfBoundsException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void saidaEstoque(){
        try{
            List<Produto> produtos = estoqueService.getAllProdutos();
            if (produtos.isEmpty()){
                System.out.println("Nenhum produto encontrado!");
                return;
            }
            System.out.println("====== Lista de Produtos ======");
            exibirEstoque(produtos);
            System.out.println("Digite o numero do produto que dejesa dar saida no estoque: ");
            int escolha = Integer.parseInt(sc.nextLine()) - 1;

            System.out.println("Digite a quantidade que dejesa dar saida no estoque: ");
            int quantidade = Integer.parseInt(sc.nextLine());

            estoqueService.saidaEstoque(produtos.get(escolha).getId(), quantidade);
            System.out.println("Quantidade atualizada com sucesso!");
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(NumberFormatException e){
            System.out.println("Erro: " + e.getMessage());
        }catch(IndexOutOfBoundsException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void relatorioEstoque(){
        try{
            List<Produto> produtos = estoqueService.getAllProdutos();
            if (produtos.isEmpty()){
                System.out.println("Nenhum produto encontrado!");
                return;
            }
            estoqueService.relatorioEstoque();
        }catch(EstoqueException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void exibirEstoque(List<Produto> produtos){
        for(int i = 0; i < produtos.size(); i++){
            System.out.println((i + 1) + ". " + produtos.get(i));
            System.out.println("----------------------------");
        }
    }

    private Categoria lerCategoria(){
        System.out.println("Digite a categoria do produto (ELETRONICO, ALIMENTO, VESTUARIO, PAPELARIA, OUTRO): ");
        String categoriaInput = sc.nextLine();
        try{
            return Categoria.valueOf(categoriaInput.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new EstoqueException("Categoria invalida: " + categoriaInput);
        }
    }
}
