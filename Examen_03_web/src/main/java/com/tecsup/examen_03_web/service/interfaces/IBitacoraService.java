package com.tecsup.examen_03_web.service.interfaces;

import com.tecsup.examen_03_web.model.entity.Bitacora;
import com.tecsup.examen_03_web.model.entity.Usuario;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de bitácora (auditoría)
 */
public interface IBitacoraService {

    /**
     * Registrar una acción en la bitácora
     */
    void registrar(Usuario usuario, String modulo, String tipoAccion, String accion);

    /**
     * Listar todas las acciones
     */
    List<Bitacora> listarTodas();

    /**
     * Listar últimas acciones (para dashboard)
     */
    List<Bitacora> listarUltimas();

    /**
     * Listar acciones de un usuario
     */
    List<Bitacora> listarPorUsuario(Long idUsuario);

    /**
     * Listar acciones de un módulo
     */
    List<Bitacora> listarPorModulo(String modulo);
}