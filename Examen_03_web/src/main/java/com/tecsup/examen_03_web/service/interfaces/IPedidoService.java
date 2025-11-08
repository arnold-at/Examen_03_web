package com.tecsup.examen_03_web.service.interfaces;

import com.tecsup.examen_03_web.model.entity.Pedido;
import com.tecsup.examen_03_web.model.enums.EstadoPedido;
import com.tecsup.examen_03_web.model.enums.MetodoPago;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de pedidos
 * ⭐ MÓDULO PRINCIPAL CON LÓGICA COMPLETA ⭐
 */
public interface IPedidoService {

    /**
     * Crear un nuevo pedido
     */
    Pedido crearPedido(Long idMesa, Long idCliente, Long idUsuario, String observaciones);

    /**
     * Agregar un plato al pedido
     */
    Pedido agregarPlato(Long idPedido, Long idPlato, Integer cantidad, String observaciones);

    /**
     * Cambiar el estado de un pedido
     */
    Pedido cambiarEstado(Long idPedido, EstadoPedido nuevoEstado);

    /**
     * Cerrar un pedido y generar factura automáticamente
     */
    Pedido cerrarPedido(Long idPedido, MetodoPago metodoPago);

    /**
     * Cancelar un pedido
     */
    Pedido cancelarPedido(Long idPedido);

    /**
     * Obtener pedido por ID
     */
    Pedido obtenerPorId(Long idPedido);

    /**
     * Listar todos los pedidos
     */
    List<Pedido> listarTodos();

    /**
     * Listar pedidos por estado
     */
    List<Pedido> listarPorEstado(EstadoPedido estado);

    /**
     * Listar pedidos de una mesa
     */
    List<Pedido> listarPorMesa(Long idMesa);

    /**
     * Listar pedidos activos de una mesa (no cerrados ni cancelados)
     */
    List<Pedido> listarActivosPorMesa(Long idMesa);

    /**
     * Listar pedidos para cocina (pendientes y en preparación)
     */
    List<Pedido> listarParaCocina();

    /**
     * Listar pedidos del día actual
     */
    List<Pedido> listarDelDia();

    /**
     * Eliminar un pedido (solo si está en estado PENDIENTE)
     */
    void eliminar(Long idPedido);
}