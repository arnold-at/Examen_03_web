package com.tecsup.examen_03_web.model.enums;

/**
 * Enum que define los estados de una mesa
 */
public enum EstadoMesa {
    DISPONIBLE,     // Mesa libre, lista para ser asignada
    OCUPADA,        // Mesa ocupada por clientes
    RESERVADA,      // Mesa reservada para un horario específico
    MANTENIMIENTO   // Mesa fuera de servicio
}