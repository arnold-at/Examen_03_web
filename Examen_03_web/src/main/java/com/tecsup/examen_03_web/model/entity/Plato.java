package com.tecsup.examen_03_web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecsup.examen_03_web.model.enums.TipoPlato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "platos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlato;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPlato tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 255)
    private String imagenUrl;

    @Column(nullable = false)
    private Boolean disponible = true;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "plato", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<PlatoInsumo> insumos = new ArrayList<>();
}