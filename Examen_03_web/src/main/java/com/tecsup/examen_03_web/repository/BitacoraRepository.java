package com.tecsup.examen_03_web.repository;

import com.tecsup.examen_03_web.model.entity.Bitacora;
import com.tecsup.examen_03_web.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Bitacora
 */
@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    // ========== MÉTODOS USADOS EN BitacoraServiceImpl ==========

    /**
     * Obtener las últimas 20 acciones registradas
     */
    List<Bitacora> findTop20ByOrderByFechaHoraDesc();

    /**
     * Buscar bitácoras por ID de usuario
     */
    List<Bitacora> findByUsuario_IdUsuario(Long idUsuario);

    /**
     * Buscar bitácoras por módulo
     */
    List<Bitacora> findByModulo(String modulo);

    // ========== MÉTODOS ADICIONALES ÚTILES ==========

    /**
     * Obtener acciones del día actual
     */
    @Query("SELECT b FROM Bitacora b WHERE CAST(b.fechaHora AS LocalDate) = CURRENT_DATE ORDER BY b.fechaHora DESC")
    List<Bitacora> findAccionesDelDia();

    /**
     * Buscar por usuario (entidad completa)
     */
    List<Bitacora> findByUsuarioOrderByFechaHoraDesc(Usuario usuario);

    /**
     * Buscar por módulo ordenado
     */
    List<Bitacora> findByModuloOrderByFechaHoraDesc(String modulo);

    /**
     * Buscar por tipo de acción
     */
    List<Bitacora> findByTipoAccionOrderByFechaHoraDesc(String tipoAccion);

    /**
     * Buscar entre fechas
     */
    List<Bitacora> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar por usuario y rango de fechas
     */
    @Query("SELECT b FROM Bitacora b WHERE b.usuario = :usuario " +
            "AND b.fechaHora BETWEEN :inicio AND :fin " +
            "ORDER BY b.fechaHora DESC")
    List<Bitacora> findByUsuarioAndFechaHoraBetween(
            @Param("usuario") Usuario usuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    /**
     * Últimas 10 acciones
     */
    List<Bitacora> findTop10ByOrderByFechaHoraDesc();

    /**
     * Buscar por módulo y fecha específica
     */
    @Query("SELECT b FROM Bitacora b WHERE b.modulo = :modulo " +
            "AND CAST(b.fechaHora AS LocalDate) = :fecha " +
            "ORDER BY b.fechaHora DESC")
    List<Bitacora> findByModuloAndFecha(
            @Param("modulo") String modulo,
            @Param("fecha") LocalDate fecha
    );

    /**
     * Contar acciones del día
     */
    @Query("SELECT COUNT(b) FROM Bitacora b WHERE CAST(b.fechaHora AS LocalDate) = CURRENT_DATE")
    long countAccionesDelDia();

    /**
     * Contar acciones por usuario hoy
     */
    @Query("SELECT COUNT(b) FROM Bitacora b WHERE b.usuario = :usuario " +
            "AND CAST(b.fechaHora AS LocalDate) = CURRENT_DATE")
    long countAccionesDelDiaPorUsuario(@Param("usuario") Usuario usuario);

    /**
     * Contar acciones por módulo
     */
    long countByModulo(String modulo);

    /**
     * Contar acciones por usuario
     */
    long countByUsuario_IdUsuario(Long idUsuario);
}