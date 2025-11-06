package com.tecsup.examen_03_web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa un insumo del inventario
 */
@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsumo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20)
    private String unidadMedida; // kg, litros, unidades, etc.

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal stock = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(nullable = false)
    private Boolean activo = true;
}