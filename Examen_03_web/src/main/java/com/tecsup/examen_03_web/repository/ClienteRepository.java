package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por DNI
    Optional<Cliente> findByDni(String dni);

    // Buscar cliente por teléfono
    Optional<Cliente> findByTelefono(String telefono);

    // Buscar clientes activos
    List<Cliente> findByActivoTrue();

    // Buscar clientes por apellido (para búsqueda)
    List<Cliente> findByApellidosContainingIgnoreCase(String apellidos);

    // Buscar clientes por nombre o apellido
    List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
}