package com.tecsup.examen_03_web.service.impl;

import com.tecsup.examen_03_web.model.entity.*;
import com.tecsup.examen_03_web.model.enums.EstadoMesa;
import com.tecsup.examen_03_web.model.enums.EstadoPedido;
import com.tecsup.examen_03_web.model.enums.MetodoPago;
import com.tecsup.examen_03_web.repository.*;
import com.tecsup.examen_03_web.service.interfaces.IBitacoraService;
import com.tecsup.examen_03_web.service.interfaces.IPedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final MesaRepository mesaRepository;
    private final PlatoRepository platoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final InsumoRepository insumoRepository;
    private final PlatoInsumoRepository platoInsumoRepository;
    private final FacturaRepository facturaRepository;
    private final IBitacoraService bitacoraService;

    @Override
    @Transactional
    public Pedido crearPedido(Long idMesa, Long idCliente, Long idUsuario, String observaciones) {
        log.info("Creando pedido para mesa: {}", idMesa);

        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        if (!mesa.getEstado().equals(EstadoMesa.DISPONIBLE)) {
            throw new RuntimeException("La mesa no está disponible");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cliente cliente = null;
        if (idCliente != null) {
            cliente = clienteRepository.findById(idCliente).orElse(null);
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setCliente(cliente);
        pedido.setUsuarioRegistro(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setObservaciones(observaciones);
        pedido.setTotal(BigDecimal.ZERO);

        mesa.setEstado(EstadoMesa.OCUPADA);
        mesaRepository.save(mesa);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        bitacoraService.registrar(usuario, "PEDIDOS", "CREAR",
                "Creó pedido #" + pedidoGuardado.getIdPedido() + " para mesa " + mesa.getNumero());

        log.info("Pedido creado exitosamente: ID {}", pedidoGuardado.getIdPedido());
        return pedidoGuardado;
    }

    @Override
    @Transactional
    public Pedido agregarPlato(Long idPedido, Long idPlato, Integer cantidad, String observaciones) {
        log.info("Agregando plato {} al pedido {}", idPlato, idPedido);

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado().equals(EstadoPedido.CERRADO) ||
                pedido.getEstado().equals(EstadoPedido.CANCELADO)) {
            throw new RuntimeException("No se puede agregar platos a un pedido cerrado o cancelado");
        }

        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));

        if (!plato.getDisponible() || !plato.getActivo()) {
            throw new RuntimeException("El plato no está disponible");
        }

        verificarStockInsumos(plato, cantidad);

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setPlato(plato);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(plato.getPrecio());
        detalle.setObservaciones(observaciones);
        detalle.calcularSubtotal();

        detallePedidoRepository.save(detalle);

        descontarInsumos(plato, cantidad);

        pedido.calcularTotal();
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        bitacoraService.registrar(pedido.getUsuarioRegistro(), "PEDIDOS", "EDITAR",
                "Agregó " + cantidad + "x " + plato.getNombre() + " al pedido #" + idPedido);

        log.info("Plato agregado exitosamente al pedido");
        return pedidoActualizado;
    }

    @Override
    @Transactional
    public Pedido cambiarEstado(Long idPedido, EstadoPedido nuevoEstado) {
        log.info("Cambiando estado del pedido {} a {}", idPedido, nuevoEstado);

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        validarTransicionEstado(pedido.getEstado(), nuevoEstado);

        pedido.setEstado(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        bitacoraService.registrar(pedido.getUsuarioRegistro(), "PEDIDOS", "EDITAR",
                "Cambió estado del pedido #" + idPedido + " a " + nuevoEstado);

        log.info("Estado del pedido actualizado exitosamente");
        return pedidoActualizado;
    }

    @Override
    @Transactional
    public Pedido cerrarPedido(Long idPedido, MetodoPago metodoPago) {
        log.info("Cerrando pedido {} con método de pago: {}", idPedido, metodoPago);

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado().equals(EstadoPedido.CERRADO)) {
            throw new RuntimeException("El pedido ya está cerrado");
        }

        if (pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("No se puede cerrar un pedido sin platos");
        }

        pedido.setEstado(EstadoPedido.CERRADO);
        pedidoRepository.save(pedido);

        Mesa mesa = pedido.getMesa();
        mesa.setEstado(EstadoMesa.DISPONIBLE);
        mesaRepository.save(mesa);

        generarFactura(pedido, metodoPago);

        bitacoraService.registrar(pedido.getUsuarioRegistro(), "PEDIDOS", "CERRAR",
                "Cerró pedido #" + idPedido + " - Total: S/. " + pedido.getTotal());

        log.info("Pedido cerrado exitosamente y factura generada");
        return pedido;
    }

    @Override
    @Transactional
    public Pedido cancelarPedido(Long idPedido) {
        log.info("Cancelando pedido {}", idPedido);

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado().equals(EstadoPedido.CERRADO)) {
            throw new RuntimeException("No se puede cancelar un pedido cerrado");
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            devolverInsumos(detalle.getPlato(), detalle.getCantidad());
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);

        Mesa mesa = pedido.getMesa();
        mesa.setEstado(EstadoMesa.DISPONIBLE);
        mesaRepository.save(mesa);

        bitacoraService.registrar(pedido.getUsuarioRegistro(), "PEDIDOS", "CANCELAR",
                "Canceló pedido #" + idPedido);

        log.info("Pedido cancelado exitosamente");
        return pedido;
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido obtenerPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pedido> listarPedidosActivos() {
        return pedidoRepository.findPedidosActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByFechaHoraDesc(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorMesa(Long idMesa) {
        return pedidoRepository.findByMesa_IdMesa(idMesa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarActivosPorMesa(Long idMesa) {
        return pedidoRepository.findPedidosActivosPorMesa(idMesa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarParaCocina() {
        return pedidoRepository.findPedidosParaCocina();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarDelDia() {
        return pedidoRepository.findPedidosDelDia();
    }

    @Override
    @Transactional
    public void eliminar(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new RuntimeException("Solo se pueden eliminar pedidos en estado PENDIENTE");
        }

        Mesa mesa = pedido.getMesa();
        mesa.setEstado(EstadoMesa.DISPONIBLE);
        mesaRepository.save(mesa);

        pedidoRepository.delete(pedido);

        bitacoraService.registrar(pedido.getUsuarioRegistro(), "PEDIDOS", "ELIMINAR",
                "Eliminó pedido #" + idPedido);
    }

    private void verificarStockInsumos(Plato plato, Integer cantidad) {
        List<PlatoInsumo> insumos = platoInsumoRepository.findByPlato_IdPlato(plato.getIdPlato());

        for (PlatoInsumo platoInsumo : insumos) {
            Insumo insumo = platoInsumo.getInsumo();
            BigDecimal cantidadNecesaria = platoInsumo.getCantidadUsada()
                    .multiply(BigDecimal.valueOf(cantidad));

            if (insumo.getStock().compareTo(cantidadNecesaria) < 0) {
                throw new RuntimeException("Stock insuficiente de " + insumo.getNombre() +
                        ". Disponible: " + insumo.getStock() + " " + insumo.getUnidadMedida());
            }
        }
    }

    private void descontarInsumos(Plato plato, Integer cantidad) {
        List<PlatoInsumo> insumos = platoInsumoRepository.findByPlato_IdPlato(plato.getIdPlato());

        for (PlatoInsumo platoInsumo : insumos) {
            Insumo insumo = platoInsumo.getInsumo();
            BigDecimal cantidadADescontar = platoInsumo.getCantidadUsada()
                    .multiply(BigDecimal.valueOf(cantidad));

            insumo.setStock(insumo.getStock().subtract(cantidadADescontar));
            insumoRepository.save(insumo);

            log.info("Descontado {} {} de {}", cantidadADescontar,
                    insumo.getUnidadMedida(), insumo.getNombre());
        }
    }

    private void devolverInsumos(Plato plato, Integer cantidad) {
        List<PlatoInsumo> insumos = platoInsumoRepository.findByPlato_IdPlato(plato.getIdPlato());

        for (PlatoInsumo platoInsumo : insumos) {
            Insumo insumo = platoInsumo.getInsumo();
            BigDecimal cantidadADevolver = platoInsumo.getCantidadUsada()
                    .multiply(BigDecimal.valueOf(cantidad));

            insumo.setStock(insumo.getStock().add(cantidadADevolver));
            insumoRepository.save(insumo);
        }
    }

    private void generarFactura(Pedido pedido, MetodoPago metodoPago) {
        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setMetodoPago(metodoPago);
        factura.setSubtotal(pedido.getTotal());

        BigDecimal igv = pedido.getTotal().multiply(BigDecimal.valueOf(0.18));
        factura.setIgv(igv);
        factura.setTotal(pedido.getTotal().add(igv));
        factura.setPagado(true);

        for (DetallePedido detalle : pedido.getDetalles()) {
            DetalleFactura detalleFactura = new DetalleFactura();
            detalleFactura.setFactura(factura);
            detalleFactura.setConcepto(detalle.getPlato().getNombre());
            detalleFactura.setCantidad(detalle.getCantidad());
            detalleFactura.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleFactura.setMonto(detalle.getSubtotal());
            factura.getDetalles().add(detalleFactura);
        }

        facturaRepository.save(factura);
        log.info("Factura generada para pedido #{}", pedido.getIdPedido());
    }

    private void validarTransicionEstado(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
        if (estadoActual.equals(EstadoPedido.CERRADO) || estadoActual.equals(EstadoPedido.CANCELADO)) {
            throw new RuntimeException("No se puede cambiar el estado de un pedido cerrado o cancelado");
        }

        if (estadoActual.equals(EstadoPedido.SERVIDO) && !nuevoEstado.equals(EstadoPedido.CERRADO)) {
            throw new RuntimeException("Un pedido servido solo puede pasar a CERRADO");
        }
    }
}