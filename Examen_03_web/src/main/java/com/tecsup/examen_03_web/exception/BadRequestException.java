package com.tecsup.examen_03_web.exception;

/**
 * Excepción para solicitudes inválidas
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}