package com.tecsup.examen_03_web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    @JsonIgnore
    private Plato plato;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_insumo", nullable = false)
    private Insumo insumo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadUsada;
}