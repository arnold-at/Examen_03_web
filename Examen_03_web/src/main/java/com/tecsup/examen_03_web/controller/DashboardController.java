package com.tecsup.examen_03_web.controller;

import com.tecsup.examen_03_web.model.entity.Usuario;
import com.tecsup.examen_03_web.model.enums.EstadoPedido;
import com.tecsup.examen_03_web.service.interfaces.IAuthService;
import com.tecsup.examen_03_web.service.interfaces.IMesaService;
import com.tecsup.examen_03_web.service.interfaces.IPedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador del Dashboard principal
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final IAuthService authService;
    private final IPedidoService pedidoService;
    private final IMesaService mesaService;

    /**
     * Página principal del dashboard
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Usuario usuarioActual = authService.obtenerUsuarioActual();

        // Estadísticas para el dashboard
        long pedidosPendientes = pedidoService.listarPorEstado(EstadoPedido.PENDIENTE).size();
        long pedidosEnPreparacion = pedidoService.listarPorEstado(EstadoPedido.EN_PREPARACION).size();
        long mesasDisponibles = mesaService.listarDisponibles().size();

        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("pedidosPendientes", pedidosPendientes);
        model.addAttribute("pedidosEnPreparacion", pedidosEnPreparacion);
        model.addAttribute("mesasDisponibles", mesasDisponibles);

        // Últimos pedidos
        model.addAttribute("ultimosPedidos", pedidoService.listarDelDia());

        return "dashboard";
    }
}