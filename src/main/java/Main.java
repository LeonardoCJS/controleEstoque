import db.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.inicializar();
        System.out.println("Banco inicializado com sucesso!");
    }
}
