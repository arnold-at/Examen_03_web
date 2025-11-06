package com.tecsup.examen_03_web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad que representa la bitácora del sistema
 * Registra todas las acciones importantes para auditoría
 */
@Entity
@Table(name = "bitacora")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBitacora;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 500)
    private String accion;

    @Column(length = 100)
    private String modulo; // PEDIDOS, MESAS, FACTURACION, etc.

    @Column(length = 50)
    private String tipoAccion; // CREAR, EDITAR, ELIMINAR, CONSULTAR

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(length = 50)
    private String ipAddress;
}