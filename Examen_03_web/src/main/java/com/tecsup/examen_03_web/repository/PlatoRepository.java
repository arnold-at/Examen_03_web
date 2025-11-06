package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Plato;
import com.tecsup.examen_03_web.model.enums.TipoPlato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Plato
 */
@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {

    // Buscar platos activos
    List<Plato> findByActivoTrue();

    // Buscar platos disponibles
    List<Plato> findByDisponibleTrueAndActivoTrue();

    // Buscar platos por tipo
    List<Plato> findByTipo(TipoPlato tipo);

    // Buscar platos disponibles por tipo
    List<Plato> findByTipoAndDisponibleTrueAndActivoTrue(TipoPlato tipo);

    // Buscar platos por nombre
    List<Plato> findByNombreContainingIgnoreCase(String nombre);

    // Buscar platos por nombre y disponibles
    List<Plato> findByNombreContainingIgnoreCaseAndDisponibleTrueAndActivoTrue(String nombre);
}