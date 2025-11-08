package com.tecsup.examen_03_web.service.impl;

import com.tecsup.examen_03_web.model.entity.Usuario;
import com.tecsup.examen_03_web.model.enums.Rol;
import com.tecsup.examen_03_web.repository.UsuarioRepository;
import com.tecsup.examen_03_web.service.interfaces.IAuthService;
import com.tecsup.examen_03_web.service.interfaces.IBitacoraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de autenticación
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final IBitacoraService bitacoraService;

    @Override
    @Transactional
    public Usuario registrarUsuario(String nombreUsuario, String contrasena, String nombreCompleto,
                                    String email, Rol rol) {
        log.info("Registrando nuevo usuario: {}", nombreUsuario);

        // Validar que el nombre de usuario no exista
        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setContrasena(passwordEncoder.encode(contrasena)); // Encriptar contraseña
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setActivo(true);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Registrar en bitácora
        bitacoraService.registrar(usuarioGuardado, "ADMINISTRACION", "CREAR",
                "Usuario registrado: " + nombreUsuario + " con rol " + rol);

        log.info("Usuario registrado exitosamente: {}", nombreUsuario);
        return usuarioGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombreUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String nombreUsuario = authentication.getName();
        return obtenerPorNombreUsuario(nombreUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    @Override
    @Transactional
    public void cambiarContrasena(Long idUsuario, String contrasenaActual, String nuevaContrasena) {
        log.info("Cambiando contraseña del usuario ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Validar nueva contraseña
        if (nuevaContrasena == null || nuevaContrasena.length() < 6) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 6 caracteres");
        }

        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);

        // Registrar en bitácora
        bitacoraService.registrar(usuario, "ADMINISTRACION", "EDITAR",
                "Cambió su contraseña");

        log.info("Contraseña cambiada exitosamente para usuario ID: {}", idUsuario);
    }

    @Override
    @Transactional
    public Usuario cambiarEstadoUsuario(Long idUsuario, boolean activo) {
        log.info("Cambiando estado del usuario ID: {} a {}", idUsuario, activo ? "ACTIVO" : "INACTIVO");

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(activo);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        // Registrar en bitácora
        Usuario usuarioActual = obtenerUsuarioActual();
        if (usuarioActual != null) {
            bitacoraService.registrar(usuarioActual, "ADMINISTRACION", "EDITAR",
                    (activo ? "Activó" : "Desactivó") + " al usuario: " + usuario.getNombreUsuario());
        }

        log.info("Estado del usuario actualizado exitosamente");
        return usuarioActualizado;
    }
}