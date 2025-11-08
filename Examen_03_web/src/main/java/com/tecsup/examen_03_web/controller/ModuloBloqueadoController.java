package com.tecsup.examen_03_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bloqueado")
public class ModuloBloqueadoController {

    /**
     * Muestra la página de módulo bloqueado
     *
     * @param modulo Nombre del módulo bloqueado
     * @param model Modelo para pasar datos a la vista
     * @return Vista de módulo bloqueado
     */
    @GetMapping("/{modulo}")
    public String moduloBloqueado(@PathVariable String modulo, Model model) {
        // Formatear el nombre del módulo
        String moduloFormateado = formatearNombreModulo(modulo);
        model.addAttribute("modulo", moduloFormateado);
        return "bloqueado";
    }

    /**
     * Página de módulo bloqueado genérica
     */
    @GetMapping
    public String moduloBloqueadoGenerico(Model model) {
        model.addAttribute("modulo", "Módulo Bloqueado");
        return "bloqueado";
    }

    /**
     * Formatea el nombre del módulo para mostrarlo correctamente
     * Ejemplo: "reportes" -> "Reportes"
     */
    private String formatearNombreModulo(String modulo) {
        if (modulo == null || modulo.isEmpty()) {
            return "Módulo Bloqueado";
        }

        // Capitalizar primera letra
        String resultado = modulo.substring(0, 1).toUpperCase() + modulo.substring(1);

        // Reemplazar guiones con espacios
        resultado = resultado.replace("-", " ");
        resultado = resultado.replace("_", " ");

        return resultado;
    }
}