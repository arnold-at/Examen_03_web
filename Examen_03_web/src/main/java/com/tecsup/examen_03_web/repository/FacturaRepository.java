package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Factura;
import com.tecsup.examen_03_web.model.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    // Buscar facturas del día actual
    @Query("SELECT f FROM Factura f WHERE DATE(f.fechaEmision) = CURRENT_DATE ORDER BY f.fechaEmision DESC")
    List<Factura> findFacturasDelDia();

    // Calcular total de ventas del día
    @Query("SELECT SUM(f.total) FROM Factura f WHERE DATE(f.fechaEmision) = CURRENT_DATE AND f.pagado = true")
    Double calcularVentasDelDia();
}