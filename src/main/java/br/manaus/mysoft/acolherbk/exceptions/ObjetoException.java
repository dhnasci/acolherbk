package br.manaus.mysoft.acolherbk.exceptions;

public class ObjetoException extends Exception{
    private static final long serialVersionUID = 1L;

    public ObjetoException(String message) {
        super(message);
    }

    public ObjetoException(Throwable cause) {
        super(cause);
    }
}
