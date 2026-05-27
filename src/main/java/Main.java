import dao.ProdutoDAO;
import dao.ProdutoDAOImpl;
import db.DatabaseInitializer;
import service.EstoqueService;
import ui.Menu;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        DatabaseInitializer.inicializar();
        ProdutoDAO produtoDAO = new ProdutoDAOImpl();
        EstoqueService estoqueService = new EstoqueService(produtoDAO);
        Menu menu = new Menu(estoqueService);
        menu.rodar();
    }
}
