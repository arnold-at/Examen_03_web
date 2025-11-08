package com.tecsup.examen_03_web.exception;

import com.tecsup.examen_03_web.model.enums.EstadoMesa;

/**
 * Excepción personalizada para manejar casos donde una mesa no está disponible
 * para ser asignada a un nuevo pedido.
 *
 * Esta excepción se lanza cuando:
 * - Se intenta asignar una mesa que ya está ocupada
 * - Se intenta usar una mesa que está reservada
 * - Se intenta realizar una operación en una mesa con estado no válido
 *
 * @author Sistema Restaurante Sabor Gourmet
 * @version 1.0
 */
public class MesaNoDisponibleException extends RuntimeException {

    private Long mesaId;
    private Integer numeroMesa;
    private EstadoMesa estadoActual;
    private String motivoNoDisponible;

    /**
     * Constructor básico con mensaje simple
     *
     * @param mensaje Descripción del error
     */
    public MesaNoDisponibleException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     *
     * @param mensaje Descripción del error
     * @param causa Causa original del error
     */
    public MesaNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor con información de la mesa y su estado
     *
     * @param mesaId ID de la mesa
     * @param numeroMesa Número de la mesa
     * @param estadoActual Estado actual de la mesa
     */
    public MesaNoDisponibleException(Long mesaId, Integer numeroMesa, EstadoMesa estadoActual) {
        super(String.format(
                "La mesa #%d (ID: %d) no está disponible. Estado actual: %s",
                numeroMesa, mesaId, estadoActual
        ));
        this.mesaId = mesaId;
        this.numeroMesa = numeroMesa;
        this.estadoActual = estadoActual;
    }

    /**
     * Constructor detallado con motivo específico
     *
     * @param mesaId ID de la mesa
     * @param numeroMesa Número de la mesa
     * @param estadoActual Estado actual de la mesa
     * @param motivoNoDisponible Motivo específico por el que no está disponible
     */
    public MesaNoDisponibleException(Long mesaId, Integer numeroMesa,
                                     EstadoMesa estadoActual, String motivoNoDisponible) {
        super(String.format(
                "La mesa #%d no está disponible. Estado: %s. Motivo: %s",
                numeroMesa, estadoActual, motivoNoDisponible
        ));
        this.mesaId = mesaId;
        this.numeroMesa = numeroMesa;
        this.estadoActual = estadoActual;
        this.motivoNoDisponible = motivoNoDisponible;
    }

    /**
     * Constructor para mesa ocupada con información del pedido activo
     *
     * @param numeroMesa Número de la mesa
     * @param pedidoId ID del pedido que está usando la mesa
     */
    public MesaNoDisponibleException(Integer numeroMesa, Long pedidoId) {
        super(String.format(
                "La mesa #%d está ocupada por el pedido #%d",
                numeroMesa, pedidoId
        ));
        this.numeroMesa = numeroMesa;
        this.estadoActual = EstadoMesa.OCUPADA;
        this.motivoNoDisponible = "Pedido activo #" + pedidoId;
    }

    // Getters
    public Long getMesaId() {
        return mesaId;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public EstadoMesa getEstadoActual() {
        return estadoActual;
    }

    public String getMotivoNoDisponible() {
        return motivoNoDisponible;
    }

    /**
     * Verifica si la mesa está ocupada
     *
     * @return true si el estado es OCUPADA
     */
    public boolean estaMesaOcupada() {
        return EstadoMesa.OCUPADA.equals(estadoActual);
    }

    /**
     * Verifica si la mesa está reservada
     *
     * @return true si el estado es RESERVADA
     */
    public boolean estaMesaReservada() {
        return EstadoMesa.RESERVADA.equals(estadoActual);
    }

    /**
     * Retorna un mensaje detallado del error
     *
     * @return Mensaje formateado con toda la información del error
     */
    public String getMensajeDetallado() {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ERROR: MESA NO DISPONIBLE\n");
        mensaje.append("═".repeat(50)).append("\n");

        if (numeroMesa != null) {
            mensaje.append("Mesa número: ").append(numeroMesa).append("\n");
        }

        if (mesaId != null) {
            mensaje.append("ID de mesa: ").append(mesaId).append("\n");
        }

        if (estadoActual != null) {
            mensaje.append("Estado actual: ").append(estadoActual).append("\n");
        }

        if (motivoNoDisponible != null && !motivoNoDisponible.isEmpty()) {
            mensaje.append("Motivo: ").append(motivoNoDisponible).append("\n");
        }

        mensaje.append("═".repeat(50));

        return mensaje.toString();
    }

    /**
     * Retorna sugerencias para resolver el problema
     *
     * @return Lista de sugerencias según el estado de la mesa
     */
    public String getSugerencias() {
        StringBuilder sugerencias = new StringBuilder();
        sugerencias.append("SUGERENCIAS:\n");

        if (estaMesaOcupada()) {
            sugerencias.append("• La mesa está ocupada con un pedido activo\n");
            sugerencias.append("• Espere a que el pedido actual se cierre\n");
            sugerencias.append("• Puede verificar el estado del pedido en el sistema\n");
            sugerencias.append("• Considere asignar otra mesa disponible\n");
        } else if (estaMesaReservada()) {
            sugerencias.append("• La mesa tiene una reserva activa\n");
            sugerencias.append("• Verifique los detalles de la reserva\n");
            sugerencias.append("• Confirme con el cliente si corresponde cancelar la reserva\n");
            sugerencias.append("• Busque otra mesa disponible\n");
        } else {
            sugerencias.append("• Verifique el estado actual de la mesa\n");
            sugerencias.append("• Actualice el estado si es necesario\n");
            sugerencias.append("• Consulte con el personal de sala\n");
            sugerencias.append("• Seleccione otra mesa disponible\n");
        }

        return sugerencias.toString();
    }

    /**
     * Retorna el código de error HTTP recomendado
     *
     * @return Código HTTP (normalmente 409 Conflict)
     */
    public int getCodigoHttpRecomendado() {
        return 409; // Conflict
    }

    /**
     * Genera un mensaje corto para mostrar al usuario
     *
     * @return Mensaje amigable para el usuario final
     */
    public String getMensajeUsuario() {
        if (numeroMesa != null && estadoActual != null) {
            switch (estadoActual) {
                case OCUPADA:
                    return String.format(
                            "Lo sentimos, la mesa #%d está ocupada en este momento. " +
                                    "Por favor, seleccione otra mesa disponible.",
                            numeroMesa
                    );
                case RESERVADA:
                    return String.format(
                            "La mesa #%d está reservada. " +
                                    "Verifique los detalles de la reserva o elija otra mesa.",
                            numeroMesa
                    );
                default:
                    return String.format(
                            "La mesa #%d no está disponible en este momento. " +
                                    "Por favor, intente con otra mesa.",
                            numeroMesa
                    );
            }
        }
        return "La mesa seleccionada no está disponible. Por favor, elija otra.";
    }

    @Override
    public String toString() {
        return "MesaNoDisponibleException{" +
                "mensaje='" + getMessage() + '\'' +
                ", mesaId=" + mesaId +
                ", numeroMesa=" + numeroMesa +
                ", estadoActual=" + estadoActual +
                ", motivoNoDisponible='" + motivoNoDisponible + '\'' +
                '}';
    }
}