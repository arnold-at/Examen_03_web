package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Factura;
import com.tecsup.examen_03_web.model.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Factura
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Buscar factura por pedido
    Optional<Factura> findByPedido_IdPedido(Long idPedido);

    // Buscar facturas pagadas
    List<Factura> findByPagadoTrue();

    // Buscar facturas pendientes de pago
    List<Factura> findByPagadoFalse();

    // Buscar facturas por método de pago
    List<Factura> findByMetodoPago(MetodoPago metodoPago);

    // Buscar facturas por rango de fechas
    List<Factura> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // ✅ CORREGIDO - Buscar facturas del día actual
    @Query("SELECT f FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = CURRENT_DATE ORDER BY f.fechaEmision DESC")
    List<Factura> findFacturasDelDia();

    // ✅ CORREGIDO - Calcular total de ventas del día (con COALESCE para evitar NULL)
    @Query("SELECT COALESCE(SUM(f.total), 0) FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = CURRENT_DATE AND f.pagado = true")
    Double calcularVentasDelDia();

    // ========== MÉTODOS ADICIONALES ÚTILES ==========

    // Buscar facturas de una fecha específica
    @Query("SELECT f FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = :fecha ORDER BY f.fechaEmision DESC")
    List<Factura> findFacturasPorFecha(@Param("fecha") LocalDate fecha);

    // Contar facturas del día
    @Query("SELECT COUNT(f) FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = CURRENT_DATE")
    long countFacturasDelDia();

    // Contar facturas pagadas del día
    @Query("SELECT COUNT(f) FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = CURRENT_DATE AND f.pagado = true")
    long countFacturasPagadasDelDia();

    // Buscar facturas del día por método de pago
    @Query("SELECT f FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = CURRENT_DATE AND f.metodoPago = :metodoPago ORDER BY f.fechaEmision DESC")
    List<Factura> findFacturasDelDiaPorMetodo(@Param("metodoPago") MetodoPago metodoPago);

    // Calcular ventas por método de pago del día
    @Query("SELECT COALESCE(SUM(f.total), 0) FROM Factura f WHERE CAST(f.fechaEmision AS LocalDate) = CURRENT_DATE AND f.metodoPago = :metodoPago AND f.pagado = true")
    Double calcularVentasDelDiaPorMetodo(@Param("metodoPago") MetodoPago metodoPago);

    // Calcular ventas de un rango de fechas
    @Query("SELECT COALESCE(SUM(f.total), 0) FROM Factura f WHERE f.fechaEmision BETWEEN :inicio AND :fin AND f.pagado = true")
    Double calcularVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    List<Factura> findTop10ByOrderByFechaEmisionDesc();

    boolean existsByPedido_IdPedido(Long idPedido);
}