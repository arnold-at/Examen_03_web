package com.tecsup.examen_03_web.service.impl;

import com.tecsup.examen_03_web.model.entity.Mesa;
import com.tecsup.examen_03_web.model.enums.EstadoMesa;
import com.tecsup.examen_03_web.repository.MesaRepository;
import com.tecsup.examen_03_web.service.interfaces.IMesaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de mesas
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MesaServiceImpl implements IMesaService {

    private final MesaRepository mesaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mesa> listarDisponibles() {
        return mesaRepository.findByEstado(EstadoMesa.DISPONIBLE);
    }

    @Override
    @Transactional(readOnly = true)
    public Mesa obtenerPorId(Long idMesa) {
        return mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
    }

    @Override
    @Transactional
    public Mesa cambiarEstado(Long idMesa, EstadoMesa nuevoEstado) {
        Mesa mesa = obtenerPorId(idMesa);
        mesa.setEstado(nuevoEstado);
        return mesaRepository.save(mesa);
    }

    @Override
    @Transactional
    public Mesa ocuparMesa(Long idMesa) {
        return cambiarEstado(idMesa, EstadoMesa.OCUPADA);
    }

    @Override
    @Transactional
    public Mesa liberarMesa(Long idMesa) {
        return cambiarEstado(idMesa, EstadoMesa.DISPONIBLE);
    }
}