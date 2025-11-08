package com.tecsup.examen_03_web.controller;

import com.tecsup.examen_03_web.service.interfaces.IAuthService;
import com.tecsup.examen_03_web.service.interfaces.IMesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de Mesas
 * Solo visualización (sin lógica de negocio)
 */
@Controller
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final IMesaService mesaService;
    private final IAuthService authService;

    /**
     * Listar todas las mesas
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "mesas/lista";
    }

    /**
     * Listar solo mesas disponibles
     */
    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        model.addAttribute("mesas", mesaService.listarDisponibles());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "mesas/lista";
    }
}