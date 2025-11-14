package com.tecsup.examen_03_web.controller;

import com.tecsup.examen_03_web.service.interfaces.IAuthService;
import com.tecsup.examen_03_web.service.interfaces.IMesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final IMesaService mesaService;
    private final IAuthService authService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        model.addAttribute("mesasDisponibles", mesaService.contarDisponibles());
        model.addAttribute("mesasOcupadas", mesaService.contarOcupadas());
        model.addAttribute("mesasReservadas", mesaService.contarReservadas());
        model.addAttribute("totalMesas", mesaService.contarTotal());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "mesas/lista";
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        model.addAttribute("mesas", mesaService.listarDisponibles());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "mesas/lista";
    }

    @PostMapping("/{id}/reservar")
    @ResponseBody
    public ResponseEntity<?> reservarMesa(@PathVariable("id") Long mesaId,
                                          @RequestBody Map<String, String> reservaData) {
        try {
            String nombre = reservaData.get("nombre");
            String telefono = reservaData.get("telefono");
            String fechaStr = reservaData.get("fecha");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaHoraReserva = LocalDateTime.parse(fechaStr, formatter);

            mesaService.reservarMesa(mesaId, nombre, telefono, fechaHoraReserva);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al procesar la reserva: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/liberar")
    @ResponseBody
    public ResponseEntity<?> liberarMesa(@PathVariable("id") Long mesaId) {
        try {
            mesaService.liberarMesa(mesaId);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al liberar la mesa: " + e.getMessage());
        }
    }
}