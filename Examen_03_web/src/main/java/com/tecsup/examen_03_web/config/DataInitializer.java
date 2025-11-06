package com.tecsup.examen_03_web.config;

import com.tecsup.examen_03_web.model.entity.*;
import com.tecsup.examen_03_web.model.enums.*;
import com.tecsup.examen_03_web.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Inicializador de datos de prueba
 * Se ejecuta al iniciar la aplicación y crea usuarios, mesas, platos e insumos de ejemplo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;
    private final PlatoRepository platoRepository;
    private final InsumoRepository insumoRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Solo inicializar si la BD está vacía
        if (usuarioRepository.count() == 0) {
            log.info("🌱 Iniciando carga de datos de prueba...");
            crearUsuarios();
            crearMesas();
            crearInsumos();
            crearPlatos();
            crearClientes();
            log.info("✅ Datos de prueba cargados exitosamente!");
        } else {
            log.info("ℹ️ La base de datos ya contiene datos. Omitiendo inicialización.");
        }
    }

    private void crearUsuarios() {
        // Admin
        Usuario admin = new Usuario();
        admin.setNombreUsuario("admin");
        admin.setContrasena(passwordEncoder.encode("admin123"));
        admin.setNombreCompleto("Administrador del Sistema");
        admin.setEmail("admin@saborgourmet.com");
        admin.setRol(Rol.ADMIN);
        admin.setActivo(true);
        usuarioRepository.save(admin);

        // Mozo
        Usuario mozo = new Usuario();
        mozo.setNombreUsuario("mozo");
        mozo.setContrasena(passwordEncoder.encode("mozo123"));
        mozo.setNombreCompleto("Juan Pérez");
        mozo.setEmail("mozo@saborgourmet.com");
        mozo.setRol(Rol.MOZO);
        mozo.setActivo(true);
        usuarioRepository.save(mozo);

        // Cajero
        Usuario cajero = new Usuario();
        cajero.setNombreUsuario("cajero");
        cajero.setContrasena(passwordEncoder.encode("cajero123"));
        cajero.setNombreCompleto("María García");
        cajero.setEmail("cajero@saborgourmet.com");
        cajero.setRol(Rol.CAJERO);
        cajero.setActivo(true);
        usuarioRepository.save(cajero);

        // Cocinero
        Usuario cocinero = new Usuario();
        cocinero.setNombreUsuario("cocinero");
        cocinero.setContrasena(passwordEncoder.encode("cocinero123"));
        cocinero.setNombreCompleto("Carlos Rodríguez");
        cocinero.setEmail("cocinero@saborgourmet.com");
        cocinero.setRol(Rol.COCINERO);
        cocinero.setActivo(true);
        usuarioRepository.save(cocinero);

        log.info("👥 4 usuarios creados (admin, mozo, cajero, cocinero)");
    }

    private void crearMesas() {
        for (int i = 1; i <= 10; i++) {
            Mesa mesa = new Mesa();
            mesa.setNumero(i);
            mesa.setCapacidad(i <= 6 ? 4 : 6); // Mesas 1-6 para 4 personas, 7-10 para 6
            mesa.setEstado(EstadoMesa.DISPONIBLE);
            mesa.setUbicacion(i <= 5 ? "Salón Principal" : "Terraza");
            mesaRepository.save(mesa);
        }
        log.info("🪑 10 mesas creadas");
    }

    private void crearInsumos() {
        // Insumos básicos
        crearInsumo("Arroz", "kg", 50.0, 10.0, 3.50);
        crearInsumo("Pollo", "kg", 30.0, 5.0, 12.00);
        crearInsumo("Carne de res", "kg", 25.0, 5.0, 18.00);
        crearInsumo("Pescado", "kg", 20.0, 3.0, 15.00);
        crearInsumo("Tomate", "kg", 15.0, 3.0, 2.50);
        crearInsumo("Cebolla", "kg", 20.0, 5.0, 2.00);
        crearInsumo("Lechuga", "kg", 10.0, 2.0, 3.00);
        crearInsumo("Papa", "kg", 40.0, 10.0, 1.80);
        crearInsumo("Aceite", "litros", 20.0, 5.0, 8.00);
        crearInsumo("Sal", "kg", 10.0, 2.0, 1.50);

        log.info("📦 10 insumos creados");
    }

    private void crearInsumo(String nombre, String unidad, double stock, double stockMin, double precio) {
        Insumo insumo = new Insumo();
        insumo.setNombre(nombre);
        insumo.setUnidadMedida(unidad);
        insumo.setStock(BigDecimal.valueOf(stock));
        insumo.setStockMinimo(BigDecimal.valueOf(stockMin));
        insumo.setPrecioCompra(BigDecimal.valueOf(precio));
        insumo.setActivo(true);
        insumoRepository.save(insumo);
    }

    private void crearPlatos() {
        // ENTRADAS
        crearPlato("Ensalada César", TipoPlato.ENTRADA, 15.00, "Lechuga fresca, crutones y aderezo César");
        crearPlato("Tequeños", TipoPlato.ENTRADA, 12.00, "Deliciosos tequeños de queso");
        crearPlato("Causa Limeña", TipoPlato.ENTRADA, 18.00, "Papa amarilla con pollo y palta");

        // FONDOS
        crearPlato("Lomo Saltado", TipoPlato.FONDO, 35.00, "Carne de res salteada con papas y arroz");
        crearPlato("Ají de Gallina", TipoPlato.FONDO, 28.00, "Pollo en crema de ají amarillo");
        crearPlato("Arroz con Pollo", TipoPlato.FONDO, 25.00, "Arroz con pollo y verduras");
        crearPlato("Ceviche de Pescado", TipoPlato.FONDO, 32.00, "Pescado fresco en limón con cebolla");
        crearPlato("Tallarines Verdes con Bistec", TipoPlato.FONDO, 30.00, "Pasta en salsa de albahaca");

        // POSTRES
        crearPlato("Suspiro Limeño", TipoPlato.POSTRE, 12.00, "Dulce tradicional de manjar");
        crearPlato("Mazamorra Morada", TipoPlato.POSTRE, 8.00, "Postre morado con frutas");
        crearPlato("Picarones", TipoPlato.POSTRE, 10.00, "Masa frita con miel de chancaca");

        // BEBIDAS
        crearPlato("Chicha Morada", TipoPlato.BEBIDA, 5.00, "Bebida tradicional peruana");
        crearPlato("Inca Kola", TipoPlato.BEBIDA, 4.00, "Gaseosa nacional");
        crearPlato("Jugo de Maracuyá", TipoPlato.BEBIDA, 6.00, "Jugo natural de maracuyá");
        crearPlato("Pisco Sour", TipoPlato.BEBIDA, 18.00, "Cóctel de pisco");

        log.info("🍽️ 15 platos creados");
    }

    private void crearPlato(String nombre, TipoPlato tipo, double precio, String descripcion) {
        Plato plato = new Plato();
        plato.setNombre(nombre);
        plato.setTipo(tipo);
        plato.setPrecio(BigDecimal.valueOf(precio));
        plato.setDescripcion(descripcion);
        plato.setDisponible(true);
        plato.setActivo(true);
        platoRepository.save(plato);
    }

    private void crearClientes() {
        // Cliente 1
        Cliente cliente1 = new Cliente();
        cliente1.setDni("12345678");
        cliente1.setNombres("Luis");
        cliente1.setApellidos("Martínez");
        cliente1.setTelefono("987654321");
        cliente1.setCorreo("luis.martinez@email.com");
        cliente1.setActivo(true);
        clienteRepository.save(cliente1);

        // Cliente 2
        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321");
        cliente2.setNombres("Ana");
        cliente2.setApellidos("Torres");
        cliente2.setTelefono("912345678");
        cliente2.setCorreo("ana.torres@email.com");
        cliente2.setActivo(true);
        clienteRepository.save(cliente2);

        log.info("👤 2 clientes creados");
    }
}