package com.juliana.ForoHub.exception;

public class TopicoException extends RuntimeException {
    
    public TopicoException(String message) {
        super(message);
    }
    
    public TopicoException(String message, Throwable cause) {
        super(message, cause);
    }
} 