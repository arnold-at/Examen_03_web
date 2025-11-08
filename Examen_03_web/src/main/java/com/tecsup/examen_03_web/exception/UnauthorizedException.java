package com.tecsup.examen_03_web.exception;

/**
 * Excepción para acceso no autorizado
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("No tiene permisos para realizar esta acción");
    }
}