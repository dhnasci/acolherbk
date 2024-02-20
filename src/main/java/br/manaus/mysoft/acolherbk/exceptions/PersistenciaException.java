package br.manaus.mysoft.acolherbk.exceptions;

public class PersistenciaException extends Exception{
    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenciaException(Throwable cause) {
        super(cause);
    }
}
