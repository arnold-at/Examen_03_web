package com.tecsup.examen_03_web.model.enums;

/**
 * Enum que define los estados de un pedido
 */
public enum EstadoPedido {
    PENDIENTE,          // Pedido registrado, aún no enviado a cocina
    EN_PREPARACION,     // Pedido en cocina
    SERVIDO,            // Pedido entregado al cliente
    CERRADO,            // Pedido pagado y facturado
    CANCELADO           // Pedido cancelado
}