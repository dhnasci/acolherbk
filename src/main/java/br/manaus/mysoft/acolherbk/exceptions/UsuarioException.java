package br.manaus.mysoft.acolherbk.exceptions;

public class UsuarioException extends RuntimeException {
    public UsuarioException() {
        super();
    }

    public UsuarioException(String message) {
        super(message);
    }

    public UsuarioException(Throwable cause) {
        super(cause);
    }
}
