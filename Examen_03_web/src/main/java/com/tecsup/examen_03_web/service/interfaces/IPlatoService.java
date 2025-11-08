package com.tecsup.examen_03_web.service.interfaces;

import com.tecsup.examen_03_web.model.entity.Plato;
import com.tecsup.examen_03_web.model.enums.TipoPlato;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de platos
 */
public interface IPlatoService {

    /**
     * Listar todos los platos
     */
    List<Plato> listarTodos();

    /**
     * Listar platos disponibles (activos y disponibles)
     */
    List<Plato> listarDisponibles();

    /**
     * Listar platos por tipo
     */
    List<Plato> listarPorTipo(TipoPlato tipo);

    /**
     * Listar platos disponibles por tipo
     */
    List<Plato> listarDisponiblesPorTipo(TipoPlato tipo);

    /**
     * Obtener plato por ID
     */
    Plato obtenerPorId(Long idPlato);

    /**
     * Buscar platos por nombre
     */
    List<Plato> buscarPorNombre(String nombre);
}