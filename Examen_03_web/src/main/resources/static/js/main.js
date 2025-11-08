/**
 * ============================================
 * SISTEMA RESTAURANTE SABOR GOURMET
 * main.js - Funciones JavaScript personalizadas
 * ============================================
 */

// ========== INICIALIZACIÓN ==========
document.addEventListener('DOMContentLoaded', function() {
    console.log('🍽️ Sistema Restaurante Sabor Gourmet cargado');

    // Inicializar componentes
    initTooltips();
    initPopovers();
    initSidebar();
    initSearchFilters();
    initFormValidation();
    loadNotifications();

    // Auto-hide alerts después de 5 segundos
    autoHideAlerts();
});

// ========== TOOLTIPS Y POPOVERS ==========
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

function initPopovers() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

// ========== SIDEBAR ==========
function initSidebar() {
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');
    const sidebarToggle = document.getElementById('sidebarToggle');
    const closeSidebar = document.getElementById('closeSidebar');

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            toggleSidebar();
        });
    }

    if (closeSidebar) {
        closeSidebar.addEventListener('click', function() {
            toggleSidebar();
        });
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', function() {
            toggleSidebar();
        });
    }
}

function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');

    if (sidebar) {
        sidebar.classList.toggle('active');
    }
    if (sidebarOverlay) {
        sidebarOverlay.classList.toggle('active');
    }
}

// ========== BÚSQUEDA Y FILTROS ==========
function initSearchFilters() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(function(e) {
            filterItems(e.target.value);
        }, 300));
    }
}

function filterItems(searchTerm) {
    const cards = document.querySelectorAll('.plato-card, .mesa-card, .pedido-card');
    const lowerSearch = searchTerm.toLowerCase();

    cards.forEach(card => {
        const text = card.textContent.toLowerCase();
        const shouldShow = text.includes(lowerSearch);
        const parent = card.closest('.col-md-4, .col-md-6, .col-lg-3, .col-lg-4');

        if (parent) {
            parent.style.display = shouldShow ? 'block' : 'none';
        }
    });
}

// Debounce para optimizar búsquedas
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// ========== VALIDACIÓN DE FORMULARIOS ==========
function initFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');

    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}

// ========== NOTIFICACIONES ==========
function loadNotifications() {
    // Simulación de carga de notificaciones
    // En producción, esto haría una llamada AJAX al backend
    const notificationBadge = document.getElementById('notificationBadge');
    const notificationList = document.getElementById('notificationList');

    // Por ahora, mantener oculto
    if (notificationBadge) {
        notificationBadge.style.display = 'none';
    }
}

function showNotification(title, message, type = 'info') {
    Swal.fire({
        icon: type,
        title: title,
        text: message,
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true
    });
}

// ========== ALERTS AUTO-HIDE ==========
function autoHideAlerts() {
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
}

// ========== LOADING SPINNER ==========
function showLoading(message = 'Cargando...') {
    Swal.fire({
        title: message,
        html: '<div class="spinner-border text-primary" role="status"></div>',
        showConfirmButton: false,
        allowOutsideClick: false,
        allowEscapeKey: false
    });
}

function hideLoading() {
    Swal.close();
}

// ========== CONFIRMACIONES ==========
function confirmarAccion(titulo, mensaje, callback) {
    Swal.fire({
        title: titulo,
        text: mensaje,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ff6b35',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, continuar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed && callback) {
            callback();
        }
    });
}

// ========== FORMATEO ==========
function formatCurrency(amount) {
    return new Intl.NumberFormat('es-PE', {
        style: 'currency',
        currency: 'PEN'
    }).format(amount);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-PE', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(date);
}

function formatDateShort(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-PE', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).format(date);
}

// ========== AJAX HELPERS ==========
async function fetchData(url, options = {}) {
    try {
        showLoading();
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        hideLoading();

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        hideLoading();
        console.error('Error en fetchData:', error);
        showNotification('Error', 'Ocurrió un error al procesar la solicitud', 'error');
        throw error;
    }
}

async function postData(url, data) {
    return fetchData(url, {
        method: 'POST',
        body: JSON.stringify(data)
    });
}

async function putData(url, data) {
    return fetchData(url, {
        method: 'PUT',
        body: JSON.stringify(data)
    });
}

async function deleteData(url) {
    return fetchData(url, {
        method: 'DELETE'
    });
}

// ========== VISTA DE TABLAS/GRID ==========
function cambiarVista(vista) {
    const gridView = document.getElementById('gridView');
    const tableView = document.getElementById('tableView');
    const buttons = document.querySelectorAll('.btn-group button');

    if (!gridView || !tableView) return;

    if (vista === 'grid') {
        gridView.style.display = 'flex';
        tableView.style.display = 'none';
        buttons[0]?.classList.add('active');
        buttons[1]?.classList.remove('active');
    } else {
        gridView.style.display = 'none';
        tableView.style.display = 'block';
        buttons[0]?.classList.remove('active');
        buttons[1]?.classList.add('active');

        // Inicializar DataTable si no está inicializado
        const table = tableView.querySelector('table');
        if (table && !$.fn.DataTable.isDataTable(table)) {
            $(table).DataTable({
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json'
                },
                pageLength: 10,
                responsive: true
            });
        }
    }

    // Guardar preferencia en localStorage
    localStorage.setItem('vistaPreferida', vista);
}

// ========== FILTROS ==========
function limpiarFiltros() {
    const form = document.getElementById('filterForm');
    if (form) {
        form.reset();
    }

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.value = '';
    }

    // Mostrar todos los elementos
    filterItems('');

    showNotification('Filtros limpiados', 'Se han restablecido todos los filtros', 'success');
}

// ========== COPIAR AL PORTAPAPELES ==========
function copiarAlPortapapeles(texto) {
    navigator.clipboard.writeText(texto).then(() => {
        showNotification('Copiado', 'Texto copiado al portapapeles', 'success');
    }).catch(err => {
        console.error('Error al copiar:', err);
        showNotification('Error', 'No se pudo copiar al portapapeles', 'error');
    });
}

// ========== IMPRIMIR ==========
function imprimirPagina() {
    window.print();
}

function imprimirElemento(elementId) {
    const elemento = document.getElementById(elementId);
    if (!elemento) return;

    const ventana = window.open('', '_blank');
    ventana.document.write(`
        <html>
        <head>
            <title>Impresión</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="/css/style.css" rel="stylesheet">
            <style>
                @media print {
                    body { margin: 0; padding: 20px; }
                }
            </style>
        </head>
        <body>
            ${elemento.innerHTML}
            <script>
                window.onload = function() {
                    window.print();
                    window.onafterprint = function() {
                        window.close();
                    }
                }
            </script>
        </body>
        </html>
    `);
    ventana.document.close();
}

// ========== EXPORTAR A PDF ==========
function exportarPDF(elementId, filename = 'documento.pdf') {
    const elemento = document.getElementById(elementId);
    if (!elemento) {
        showNotification('Error', 'Elemento no encontrado', 'error');
        return;
    }

    // Usando html2pdf si está disponible
    if (typeof html2pdf !== 'undefined') {
        const opt = {
            margin: 1,
            filename: filename,
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
        };

        html2pdf().set(opt).from(elemento).save();
    } else {
        showNotification('Error', 'Librería html2pdf no disponible', 'error');
    }
}

// ========== VALIDACIONES PERSONALIZADAS ==========
function validarEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validarTelefono(telefono) {
    const re = /^(\+51)?9\d{8}$/;
    return re.test(telefono);
}

function validarDNI(dni) {
    const re = /^\d{8}$/;
    return re.test(dni);
}

function validarRUC(ruc) {
    const re = /^\d{11}$/;
    return re.test(ruc);
}

// ========== TEMPORIZADOR ==========
function iniciarTemporizador(elementId, segundos) {
    const elemento = document.getElementById(elementId);
    if (!elemento) return;

    let tiempoRestante = segundos;

    const intervalo = setInterval(() => {
        const minutos = Math.floor(tiempoRestante / 60);
        const segs = tiempoRestante % 60;

        elemento.textContent = `${minutos.toString().padStart(2, '0')}:${segs.toString().padStart(2, '0')}`;

        if (tiempoRestante <= 0) {
            clearInterval(intervalo);
            elemento.textContent = '00:00';
        }

        tiempoRestante--;
    }, 1000);

    return intervalo;
}

// ========== SCROLL SUAVE ==========
function scrollSuave(elementId) {
    const elemento = document.getElementById(elementId);
    if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}

// ========== MODO OSCURO ==========
function toggleModoOscuro() {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    localStorage.setItem('modoOscuro', isDark);

    showNotification(
        'Modo ' + (isDark ? 'Oscuro' : 'Claro'),
        'El tema ha sido cambiado',
        'success'
    );
}

// Cargar preferencia de modo oscuro
if (localStorage.getItem('modoOscuro') === 'true') {
    document.body.classList.add('dark-mode');
}

// ========== HELPERS DE TIEMPO ==========
function tiempoTranscurrido(fecha) {
    const ahora = new Date();
    const entonces = new Date(fecha);
    const diff = ahora - entonces;

    const segundos = Math.floor(diff / 1000);
    const minutos = Math.floor(segundos / 60);
    const horas = Math.floor(minutos / 60);
    const dias = Math.floor(horas / 24);

    if (dias > 0) return `hace ${dias} día${dias > 1 ? 's' : ''}`;
    if (horas > 0) return `hace ${horas} hora${horas > 1 ? 's' : ''}`;
    if (minutos > 0) return `hace ${minutos} minuto${minutos > 1 ? 's' : ''}`;
    return 'hace un momento';
}

// ========== DETECTAR DISPOSITIVO ==========
function esMobil() {
    return window.innerWidth <= 768;
}

function esTablet() {
    return window.innerWidth > 768 && window.innerWidth <= 1024;
}

function esEscritorio() {
    return window.innerWidth > 1024;
}

// ========== ESTADÍSTICAS EN TIEMPO REAL ==========
function actualizarEstadisticas(endpoint) {
    setInterval(async () => {
        try {
            const data = await fetchData(endpoint);
            // Actualizar elementos de estadísticas
            Object.keys(data).forEach(key => {
                const elemento = document.getElementById(`stat-${key}`);
                if (elemento) {
                    elemento.textContent = data[key];
                }
            });
        } catch (error) {
            console.error('Error actualizando estadísticas:', error);
        }
    }, 30000); // Cada 30 segundos
}

// ========== EXPORTAR FUNCIONES GLOBALES ==========
window.SaborGourmet = {
    // Notificaciones
    showNotification,
    showLoading,
    hideLoading,
    confirmarAccion,

    // Formato
    formatCurrency,
    formatDate,
    formatDateShort,

    // AJAX
    fetchData,
    postData,
    putData,
    deleteData,

    // Vistas
    cambiarVista,
    limpiarFiltros,

    // Utilidades
    copiarAlPortapapeles,
    imprimirPagina,
    imprimirElemento,
    exportarPDF,

    // Validaciones
    validarEmail,
    validarTelefono,
    validarDNI,
    validarRUC,

    // Tiempo
    tiempoTranscurrido,
    iniciarTemporizador,

    // UI
    scrollSuave,
    toggleModoOscuro,
    toggleSidebar,

    // Información del sistema
    version: '1.0.0',
    nombre: 'Sabor Gourmet'
};

console.log('✅ SaborGourmet System v' + window.SaborGourmet.version + ' ready');