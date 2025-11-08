package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Pedido;
import com.tecsup.examen_03_web.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Pedido
 * ⭐ MÓDULO PRINCIPAL CON QUERIES PERSONALIZADAS ⭐
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por estado
    List<Pedido> findByEstado(EstadoPedido estado);

    // Buscar pedidos por estado ordenados por fecha
    List<Pedido> findByEstadoOrderByFechaHoraDesc(EstadoPedido estado);

    // Buscar pedidos de una mesa específica
    List<Pedido> findByMesa_IdMesa(Long idMesa);

    // Buscar pedidos activos de una mesa (no cerrados ni cancelados)
    @Query("SELECT p FROM Pedido p WHERE p.mesa.idMesa = :idMesa AND p.estado NOT IN ('CERRADO', 'CANCELADO')")
    List<Pedido> findPedidosActivosPorMesa(@Param("idMesa") Long idMesa);

    // Buscar pedidos de un cliente
    List<Pedido> findByCliente_IdCliente(Long idCliente);

    // Buscar pedidos por usuario que lo registró
    List<Pedido> findByUsuarioRegistro_IdUsuario(Long idUsuario);

    // Buscar pedidos pendientes y en preparación (para cocina)
    @Query("SELECT p FROM Pedido p WHERE p.estado IN ('PENDIENTE', 'EN_PREPARACION') ORDER BY p.fechaHora ASC")
    List<Pedido> findPedidosParaCocina();

    // Buscar pedidos por rango de fechas
    List<Pedido> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // ✅ CORREGIDO - Buscar pedidos del día actual
    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosDelDia();

    // ✅ CORREGIDO - Buscar pedidos cerrados del día (para reportes de ventas)
    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE AND p.estado = 'CERRADO'")
    List<Pedido> findPedidosCerradosDelDia();

    // Contar pedidos por estado
    long countByEstado(EstadoPedido estado);

    // Buscar últimos pedidos (dashboard)
    List<Pedido> findTop10ByOrderByFechaHoraDesc();

    // ========== MÉTODOS ADICIONALES ÚTILES ==========

    // Buscar pedidos del día por estado
    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE AND p.estado = :estado ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosDelDiaPorEstado(@Param("estado") EstadoPedido estado);

    // Buscar pedidos de una fecha específica
    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = :fecha ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosPorFecha(@Param("fecha") LocalDate fecha);

    // Contar pedidos del día
    @Query("SELECT COUNT(p) FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE")
    long countPedidosDelDia();

    // Contar pedidos cerrados del día
    @Query("SELECT COUNT(p) FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE AND p.estado = 'CERRADO'")
    long countPedidosCerradosDelDia();

    // Buscar pedidos por mesa y estado
    @Query("SELECT p FROM Pedido p WHERE p.mesa.idMesa = :idMesa AND p.estado = :estado ORDER BY p.fechaHora DESC")
    List<Pedido> findByMesaAndEstado(@Param("idMesa") Long idMesa, @Param("estado") EstadoPedido estado);
}