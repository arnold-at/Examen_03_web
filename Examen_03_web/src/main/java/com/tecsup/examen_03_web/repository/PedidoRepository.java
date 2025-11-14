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

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByEstadoOrderByFechaHoraDesc(EstadoPedido estado);

    List<Pedido> findByMesa_IdMesa(Long idMesa);

    @Query("SELECT p FROM Pedido p WHERE p.mesa.idMesa = :idMesa AND p.estado NOT IN ('CERRADO', 'CANCELADO') ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosActivosPorMesa(@Param("idMesa") Long idMesa);

    @Query("SELECT p FROM Pedido p WHERE p.estado NOT IN ('CERRADO', 'CANCELADO') ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosActivos();

    List<Pedido> findByCliente_IdCliente(Long idCliente);

    List<Pedido> findByUsuarioRegistro_IdUsuario(Long idUsuario);

    @Query("SELECT p FROM Pedido p WHERE p.estado IN ('PENDIENTE', 'EN_PREPARACION') ORDER BY p.fechaHora ASC")
    List<Pedido> findPedidosParaCocina();

    List<Pedido> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosDelDia();

    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE AND p.estado = 'CERRADO'")
    List<Pedido> findPedidosCerradosDelDia();

    long countByEstado(EstadoPedido estado);

    List<Pedido> findTop10ByOrderByFechaHoraDesc();

    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE AND p.estado = :estado ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosDelDiaPorEstado(@Param("estado") EstadoPedido estado);

    @Query("SELECT p FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = :fecha ORDER BY p.fechaHora DESC")
    List<Pedido> findPedidosPorFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE")
    long countPedidosDelDia();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE CAST(p.fechaHora AS LocalDate) = CURRENT_DATE AND p.estado = 'CERRADO'")
    long countPedidosCerradosDelDia();

    @Query("SELECT p FROM Pedido p WHERE p.mesa.idMesa = :idMesa AND p.estado = :estado ORDER BY p.fechaHora DESC")
    List<Pedido> findByMesaAndEstado(@Param("idMesa") Long idMesa, @Param("estado") EstadoPedido estado);
}