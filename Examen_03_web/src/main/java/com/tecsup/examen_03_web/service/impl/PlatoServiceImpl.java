package com.tecsup.examen_03_web.service.impl;

import com.tecsup.examen_03_web.model.entity.Plato;
import com.tecsup.examen_03_web.model.enums.TipoPlato;
import com.tecsup.examen_03_web.repository.PlatoRepository;
import com.tecsup.examen_03_web.service.interfaces.IPlatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de platos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlatoServiceImpl implements IPlatoService {

    private final PlatoRepository platoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Plato> listarTodos() {
        return platoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> listarDisponibles() {
        return platoRepository.findByDisponibleTrueAndActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> listarPorTipo(TipoPlato tipo) {
        return platoRepository.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> listarDisponiblesPorTipo(TipoPlato tipo) {
        return platoRepository.findByTipoAndDisponibleTrueAndActivoTrue(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public Plato obtenerPorId(Long idPlato) {
        return platoRepository.findById(idPlato)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plato> buscarPorNombre(String nombre) {
        return platoRepository.findByNombreContainingIgnoreCaseAndDisponibleTrueAndActivoTrue(nombre);
    }
}