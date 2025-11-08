package com.tecsup.examen_03_web.service.interfaces;

import com.tecsup.examen_03_web.model.entity.Mesa;
import com.tecsup.examen_03_web.model.enums.EstadoMesa;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de mesas
 */
public interface IMesaService {

    /**
     * Listar todas las mesas
     */
    List<Mesa> listarTodas();

    /**
     * Listar mesas disponibles
     */
    List<Mesa> listarDisponibles();

    /**
     * Obtener mesa por ID
     */
    Mesa obtenerPorId(Long idMesa);

    /**
     * Cambiar estado de una mesa
     */
    Mesa cambiarEstado(Long idMesa, EstadoMesa nuevoEstado);

    /**
     * Ocupar una mesa (cambiar a OCUPADA)
     */
    Mesa ocuparMesa(Long idMesa);

    /**
     * Liberar una mesa (cambiar a DISPONIBLE)
     */
    Mesa liberarMesa(Long idMesa);
}