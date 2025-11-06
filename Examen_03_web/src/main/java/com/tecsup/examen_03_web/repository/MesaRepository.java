package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Mesa;
import com.tecsup.examen_03_web.model.enums.EstadoMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Mesa
 */
@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // Buscar mesa por número
    Optional<Mesa> findByNumero(Integer numero);

    // Buscar mesas por estado
    List<Mesa> findByEstado(EstadoMesa estado);

    // Buscar mesas disponibles
    List<Mesa> findByEstado(EstadoMesa estado);

    // Buscar mesas disponibles con capacidad mínima
    List<Mesa> findByEstadoAndCapacidadGreaterThanEqual(EstadoMesa estado, Integer capacidad);

    // Buscar mesas por ubicación
    List<Mesa> findByUbicacionContainingIgnoreCase(String ubicacion);

    // Contar mesas por estado
    long countByEstado(EstadoMesa estado);
}
