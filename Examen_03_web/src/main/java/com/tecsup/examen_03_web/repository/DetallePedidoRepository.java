package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad DetallePedido
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Buscar detalles de un pedido específico
    List<DetallePedido> findByPedido_IdPedido(Long idPedido);

    // Buscar todos los pedidos que contienen un plato específico
    List<DetallePedido> findByPlato_IdPlato(Long idPlato);

    // Platos más vendidos (para reportes)
    @Query("SELECT dp.plato.nombre, SUM(dp.cantidad) as total " +
            "FROM DetallePedido dp " +
            "GROUP BY dp.plato.idPlato, dp.plato.nombre " +
            "ORDER BY total DESC")
    List<Object[]> findPlatosMasVendidos();
}