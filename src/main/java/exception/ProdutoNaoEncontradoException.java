package exception;

public class ProdutoNaoEncontradoException extends EstoqueException {
    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
