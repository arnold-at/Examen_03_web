package com.tecsup.examen_03_web.controller;

import com.tecsup.examen_03_web.model.entity.Pedido;
import com.tecsup.examen_03_web.model.entity.Usuario;
import com.tecsup.examen_03_web.model.enums.EstadoPedido;
import com.tecsup.examen_03_web.model.enums.MetodoPago;
import com.tecsup.examen_03_web.service.interfaces.IAuthService;
import com.tecsup.examen_03_web.service.interfaces.IMesaService;
import com.tecsup.examen_03_web.service.interfaces.IPedidoService;
import com.tecsup.examen_03_web.service.interfaces.IPlatoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Slf4j
public class PedidoController {

    private final IPedidoService pedidoService;
    private final IMesaService mesaService;
    private final IPlatoService platoService;
    private final IAuthService authService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidosActivos());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "pedidos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("mesas", mesaService.listarDisponibles());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "pedidos/nuevo";
    }

    @PostMapping("/crear")
    public String crear(
            @RequestParam Long idMesa,
            @RequestParam(required = false) String observaciones,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = authService.obtenerUsuarioActual();
            Pedido pedido = pedidoService.crearPedido(idMesa, null, usuario.getIdUsuario(), observaciones);

            redirectAttributes.addFlashAttribute("success", "Pedido creado exitosamente");
            return "redirect:/pedidos/" + pedido.getIdPedido() + "/agregar-platos";

        } catch (Exception e) {
            log.error("Error al crear pedido", e);
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/pedidos/nuevo";
        }
    }

    @GetMapping("/{id}/agregar-platos")
    public String agregarPlatos(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        pedido.getDetalles().size();

        List<Map<String, Object>> platosDTO = platoService.listarDisponibles().stream()
                .map(p -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("idPlato", p.getIdPlato());
                    dto.put("nombre", p.getNombre());
                    dto.put("precio", p.getPrecio());
                    dto.put("categoria", p.getTipo() != null ? p.getTipo().toString() : "");
                    dto.put("descripcion", p.getDescripcion() != null ? p.getDescripcion() : "");
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("pedido", pedido);
        model.addAttribute("platos", platosDTO);
        model.addAttribute("usuario", authService.obtenerUsuarioActual());

        return "pedidos/agregar-platos";
    }

    @PostMapping("/{id}/agregar-plato")
    public String agregarPlato(
            @PathVariable Long id,
            @RequestParam Long idPlato,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String observaciones,
            RedirectAttributes redirectAttributes) {

        try {
            pedidoService.agregarPlato(id, idPlato, cantidad, observaciones);
            redirectAttributes.addFlashAttribute("success", "Plato agregado exitosamente");
        } catch (Exception e) {
            log.error("Error al agregar plato", e);
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/pedidos/" + id + "/agregar-platos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        pedido.getDetalles().size();

        model.addAttribute("pedido", pedido);
        model.addAttribute("usuario", authService.obtenerUsuarioActual());

        return "pedidos/detalle";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado,
            RedirectAttributes redirectAttributes) {

        try {
            pedidoService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado a " + estado);
        } catch (Exception e) {
            log.error("Error al cambiar estado", e);
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/pedidos/" + id;
    }

    @GetMapping("/{id}/cerrar")
    public String formularioCerrar(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        pedido.getDetalles().size();

        model.addAttribute("pedido", pedido);
        model.addAttribute("metodosPago", MetodoPago.values());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());

        return "pedidos/cerrar";
    }

    @PostMapping("/{id}/cerrar")
    public String cerrar(
            @PathVariable Long id,
            @RequestParam String metodoPago,
            RedirectAttributes redirectAttributes) {

        try {
            MetodoPago metodo = MetodoPago.valueOf(metodoPago);
            pedidoService.cerrarPedido(id, metodo);

            redirectAttributes.addFlashAttribute("success",
                    "Pedido cerrado y facturado exitosamente");
            return "redirect:/pedidos";

        } catch (Exception e) {
            log.error("Error al cerrar pedido", e);
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/pedidos/" + id + "/cerrar";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.cancelarPedido(id);
            redirectAttributes.addFlashAttribute("success", "Pedido cancelado exitosamente");
        } catch (Exception e) {
            log.error("Error al cancelar pedido", e);
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/pedidos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Pedido eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar pedido", e);
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/pedidos";
    }

    @GetMapping("/cocina")
    public String cocina(Model model) {
        model.addAttribute("pedidos", pedidoService.listarParaCocina());
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "pedidos/cocina";
    }

    @GetMapping("/estado/{estado}")
    public String listarPorEstado(@PathVariable EstadoPedido estado, Model model) {
        model.addAttribute("pedidos", pedidoService.listarPorEstado(estado));
        model.addAttribute("estadoActual", estado);
        model.addAttribute("usuario", authService.obtenerUsuarioActual());
        return "pedidos/lista";
    }
}