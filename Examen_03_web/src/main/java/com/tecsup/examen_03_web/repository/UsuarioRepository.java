package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Usuario;
import com.tecsup.examen_03_web.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por nombre de usuario (para login)
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Verificar si existe un usuario con ese nombre
    boolean existsByNombreUsuario(String nombreUsuario);

    // Buscar usuarios por rol
    List<Usuario> findByRol(Rol rol);

    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();

    // Buscar usuarios activos por rol
    List<Usuario> findByRolAndActivoTrue(Rol rol);
}