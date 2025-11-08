package com.tecsup.examen_03_web.service.interfaces;

import com.tecsup.examen_03_web.model.entity.Usuario;
import com.tecsup.examen_03_web.model.enums.Rol;

/**
 * Interfaz de servicio para autenticación y gestión de usuarios
 */
public interface IAuthService {

    /**
     * Registrar un nuevo usuario
     */
    Usuario registrarUsuario(String nombreUsuario, String contrasena, String nombreCompleto,
                             String email, Rol rol);

    /**
     * Obtener usuario por nombre de usuario
     */
    Usuario obtenerPorNombreUsuario(String nombreUsuario);

    /**
     * Obtener usuario actual autenticado
     */
    Usuario obtenerUsuarioActual();

    /**
     * Verificar si un nombre de usuario ya existe
     */
    boolean existeNombreUsuario(String nombreUsuario);

    /**
     * Cambiar contraseña de usuario
     */
    void cambiarContrasena(Long idUsuario, String contrasenaActual, String nuevaContrasena);

    /**
     * Activar/Desactivar usuario
     */
    Usuario cambiarEstadoUsuario(Long idUsuario, boolean activo);
}