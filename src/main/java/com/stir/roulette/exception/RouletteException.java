package com.stir.roulette.exception;

public class RouletteException extends RuntimeException {
    public RouletteException() { }

    public RouletteException(String message) {
        super(message);
    }

    public RouletteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouletteException(Throwable cause) {
        super(cause);
    }
}
