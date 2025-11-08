package com.tecsup.examen_03_web.service.impl;

import com.tecsup.examen_03_web.model.entity.Bitacora;
import com.tecsup.examen_03_web.model.entity.Usuario;
import com.tecsup.examen_03_web.repository.BitacoraRepository;
import com.tecsup.examen_03_web.service.interfaces.IBitacoraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de bitácora (auditoría)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BitacoraServiceImpl implements IBitacoraService {

    private final BitacoraRepository bitacoraRepository;

    @Override
    @Transactional
    public void registrar(Usuario usuario, String modulo, String tipoAccion, String accion) {
        Bitacora bitacora = new Bitacora();
        bitacora.setUsuario(usuario);
        bitacora.setModulo(modulo);
        bitacora.setTipoAccion(tipoAccion);
        bitacora.setAccion(accion);
        // IP Address podría obtenerse del request en el controller
        bitacora.setIpAddress("127.0.0.1");

        bitacoraRepository.save(bitacora);
        log.debug("Bitácora registrada: {} - {}", modulo, accion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bitacora> listarTodas() {
        return bitacoraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bitacora> listarUltimas() {
        return bitacoraRepository.findTop20ByOrderByFechaHoraDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bitacora> listarPorUsuario(Long idUsuario) {
        return bitacoraRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bitacora> listarPorModulo(String modulo) {
        return bitacoraRepository.findByModulo(modulo);
    }
}