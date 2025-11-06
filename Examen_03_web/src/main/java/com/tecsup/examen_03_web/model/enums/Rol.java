package com.tecsup.examen_03_web.model.enums;

/**
 * Enum que define los roles de usuario en el sistema
 */
public enum Rol {
    ADMIN,      // Administrador - acceso completo
    MOZO,       // Mozo - gestión de pedidos y mesas
    CAJERO,     // Cajero - facturación y cobros
    COCINERO    // Cocinero - visualización de pedidos en cocina
}