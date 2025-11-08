package com.tecsup.examen_03_web.exception;

/**
 * Excepción personalizada para manejar casos donde no hay stock suficiente
 * de insumos para preparar un plato.
 *
 * Esta excepción se lanza cuando:
 * - Se intenta agregar un plato a un pedido y no hay suficientes insumos
 * - Se intenta cambiar el estado de un pedido pero faltan insumos
 * - Se detecta que el stock disponible es menor al requerido
 *
 * @author Sistema Restaurante Sabor Gourmet
 * @version 1.0
 */
public class InsufficientStockException extends RuntimeException {

    private String nombrePlato;
    private String nombreInsumo;
    private Double stockDisponible;
    private Double stockRequerido;

    /**
     * Constructor básico con mensaje simple
     *
     * @param mensaje Descripción del error de stock
     */
    public InsufficientStockException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     *
     * @param mensaje Descripción del error
     * @param causa Causa original del error
     */
    public InsufficientStockException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor detallado con información del plato e insumo
     *
     * @param nombrePlato Nombre del plato que se está preparando
     * @param nombreInsumo Nombre del insumo que falta
     * @param stockDisponible Cantidad disponible en stock
     * @param stockRequerido Cantidad requerida
     */
    public InsufficientStockException(String nombrePlato, String nombreInsumo,
                                      Double stockDisponible, Double stockRequerido) {
        super(String.format(
                "Stock insuficiente para el plato '%s': El insumo '%s' tiene %.2f unidades disponibles pero se requieren %.2f",
                nombrePlato, nombreInsumo, stockDisponible, stockRequerido
        ));
        this.nombrePlato = nombrePlato;
        this.nombreInsumo = nombreInsumo;
        this.stockDisponible = stockDisponible;
        this.stockRequerido = stockRequerido;
    }

    /**
     * Constructor para múltiples insumos faltantes
     *
     * @param nombrePlato Nombre del plato
     * @param insumosFaltantes Lista de nombres de insumos faltantes
     */
    public InsufficientStockException(String nombrePlato, String... insumosFaltantes) {
        super(String.format(
                "Stock insuficiente para el plato '%s'. Insumos faltantes: %s",
                nombrePlato, String.join(", ", insumosFaltantes)
        ));
        this.nombrePlato = nombrePlato;
    }

    // Getters
    public String getNombrePlato() {
        return nombrePlato;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public Double getStockDisponible() {
        return stockDisponible;
    }

    public Double getStockRequerido() {
        return stockRequerido;
    }

    /**
     * Calcula la diferencia de stock faltante
     *
     * @return Cantidad de stock que falta
     */
    public Double getStockFaltante() {
        if (stockRequerido != null && stockDisponible != null) {
            return stockRequerido - stockDisponible;
        }
        return null;
    }

    /**
     * Retorna un mensaje detallado del error
     *
     * @return Mensaje formateado con toda la información del error
     */
    public String getMensajeDetallado() {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ERROR DE STOCK INSUFICIENTE\n");
        mensaje.append("═".repeat(50)).append("\n");

        if (nombrePlato != null) {
            mensaje.append("Plato: ").append(nombrePlato).append("\n");
        }

        if (nombreInsumo != null) {
            mensaje.append("Insumo: ").append(nombreInsumo).append("\n");
        }

        if (stockDisponible != null && stockRequerido != null) {
            mensaje.append(String.format("Stock disponible: %.2f unidades\n", stockDisponible));
            mensaje.append(String.format("Stock requerido: %.2f unidades\n", stockRequerido));
            mensaje.append(String.format("Stock faltante: %.2f unidades\n", getStockFaltante()));
        }

        mensaje.append("═".repeat(50));

        return mensaje.toString();
    }

    /**
     * Retorna sugerencias para resolver el problema
     *
     * @return Lista de sugerencias para el usuario
     */
    public String getSugerencias() {
        return "SUGERENCIAS:\n" +
                "• Verifique el stock actual en el inventario\n" +
                "• Realice una compra de insumos si es necesario\n" +
                "• Ajuste la receta del plato si es posible\n" +
                "• Considere ofrecer un plato alternativo al cliente\n" +
                "• Actualice el menú si el insumo está agotado";
    }

    @Override
    public String toString() {
        return "InsufficientStockException{" +
                "mensaje='" + getMessage() + '\'' +
                ", nombrePlato='" + nombrePlato + '\'' +
                ", nombreInsumo='" + nombreInsumo + '\'' +
                ", stockDisponible=" + stockDisponible +
                ", stockRequerido=" + stockRequerido +
                ", stockFaltante=" + getStockFaltante() +
                '}';
    }
}