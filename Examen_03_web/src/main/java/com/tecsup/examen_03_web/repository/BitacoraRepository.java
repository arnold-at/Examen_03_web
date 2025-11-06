package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Bitacora
 * Registra todas las acciones del sistema para auditoría
 */
@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    // Buscar registros por usuario
    List<Bitacora> findByUsuario_IdUsuario(Long idUsuario);

    // Buscar registros por módulo
    List<Bitacora> findByModulo(String modulo);

    // Buscar registros por tipo de acción
    List<Bitacora> findByTipoAccion(String tipoAccion);

    // Buscar registros por rango de fechas
    List<Bitacora> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar últimas acciones (dashboard admin)
    List<Bitacora> findTop20ByOrderByFechaHoraDesc();

    // Buscar acciones del día actual
    @Query("SELECT b FROM Bitacora b WHERE DATE(b.fechaHora) = CURRENT_DATE ORDER BY b.fechaHora DESC")
    List<Bitacora> findAccionesDelDia();

    // Buscar acciones de un usuario en un módulo específico
    List<Bitacora> findByUsuario_IdUsuarioAndModulo(Long idUsuario, String modulo);
}