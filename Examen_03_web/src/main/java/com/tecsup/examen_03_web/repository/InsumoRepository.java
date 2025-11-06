package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Insumo
 */
@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    // Buscar insumos activos
    List<Insumo> findByActivoTrue();

    // Buscar insumos por nombre
    List<Insumo> findByNombreContainingIgnoreCase(String nombre);

    // Buscar insumos con stock bajo el mínimo (alertas)
    @Query("SELECT i FROM Insumo i WHERE i.stock < i.stockMinimo AND i.activo = true")
    List<Insumo> findInsumosConStockBajo();

    // Buscar insumos con stock disponible
    @Query("SELECT i FROM Insumo i WHERE i.stock > 0 AND i.activo = true")
    List<Insumo> findInsumosConStock();
}