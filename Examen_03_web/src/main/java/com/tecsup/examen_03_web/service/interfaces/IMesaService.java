package com.tecsup.examen_03_web.service.interfaces;

import com.tecsup.examen_03_web.model.entity.Mesa;
import com.tecsup.examen_03_web.model.enums.EstadoMesa;
import java.time.LocalDateTime;
import java.util.List;

public interface IMesaService {

    List<Mesa> listarTodas();
    List<Mesa> listarDisponibles();
    Mesa obtenerPorId(Long idMesa);
    Mesa cambiarEstado(Long idMesa, EstadoMesa nuevoEstado);
    Mesa ocuparMesa(Long idMesa);

    Mesa liberarMesa(Long idMesa);

    Mesa reservarMesa(Long idMesa, String nombreCliente, String telefonoCliente, LocalDateTime fechaHoraReserva);

    long contarTotal();
    long contarDisponibles();
    long contarOcupadas();
    long contarReservadas();
}