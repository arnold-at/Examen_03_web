package com.tecsup.examen_03_web.controller;

import com.tecsup.examen_03_web.model.enums.TipoPlato;
import com.tecsup.examen_03_web.service.interfaces.IAuthService;
import com.tecsup.examen_03_web.service.interfaces.IPlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de Platos / Menú
 * Solo visualización (sin lógica de negocio)
 */
@Controller
@RequestMapping("/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final IPlatoService platoService;
    private final IAuthService authService;

    /**
     * Listar todos los platos (menú completo)
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("platos", platoService.listarDisponibles());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        model.addAttribute("tipos", TipoPlato.values());
        return "platos/lista";
    }

    /**
     * Listar platos por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public String listarPorTipo(@PathVariable TipoPlato tipo, Model model) {
        model.addAttribute("platos", platoService.listarDisponiblesPorTipo(tipo));
        model.addAttribute("tipoActual", tipo);
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        model.addAttribute("tipos", TipoPlato.values());
        return "platos/lista";
    }

    /**
     * Ver detalle de un plato
     */
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("plato", platoService.obtenerPorId(id));
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "platos/detalle";
    }
}