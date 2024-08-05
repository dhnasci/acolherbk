package br.manaus.mysoft.acolherbk.exceptions;

public class SessaoException extends Exception {
    public SessaoException() {
        super();
    }

    public SessaoException(String message) {
        super(message);
    }

    public SessaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
