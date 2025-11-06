package com.tecsup.examen_03_web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa la relación entre Plato e Insumo
 * Define qué insumos se usan en cada plato y en qué cantidad
 */
@Entity
@Table(name = "plato_insumo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlatoInsumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plato", nullable = false)
    private Plato plato;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_insumo", nullable = false)
    private Insumo insumo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadUsada; // Cantidad que se usa del insumo por cada plato
}