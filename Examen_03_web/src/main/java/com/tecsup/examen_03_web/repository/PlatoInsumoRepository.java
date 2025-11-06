package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.PlatoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad PlatoInsumo
 */
@Repository
public interface PlatoInsumoRepository extends JpaRepository<PlatoInsumo, Long> {

    // Buscar todos los insumos de un plato
    List<PlatoInsumo> findByPlato_IdPlato(Long idPlato);

    // Buscar todos los platos que usan un insumo
    List<PlatoInsumo> findByInsumo_IdInsumo(Long idInsumo);
}