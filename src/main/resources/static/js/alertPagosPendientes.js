// 🧾 FUNCIÓN MEJORADA PARA ALERTAS DE PAGOS PENDIENTES E INVENTARIO BAJO
function cargarAlertasPagosPendientes() {
    $.ajax({
        url: "/alertasPagosPendientes",
        type: "GET",
        success: function (alertas) {
            const alertaElement = document.getElementById('alertasPagosPendientes');

            if (alertas && alertas.length > 0) {
                let totalSaldo = 0;
                let alertaMasCritica = null;
                let clientesUnicos = new Set();

                alertas.forEach(alerta => {
                    totalSaldo += alerta.SALDO_PENDIENTE || 0;
                    clientesUnicos.add(alerta.ID_CLIENTE);

                    if (!alertaMasCritica || (alerta.Prioridad > alertaMasCritica.Prioridad)) {
                        alertaMasCritica = alerta;
                    }
                });

                let colorAlerta = alertaMasCritica?.NivelAlerta === 'CRÍTICO' ? 'danger' : 'warning';

                alertaElement.className = `alert alert-${colorAlerta} mb-4 alerta-persistente`;
                alertaElement.style.display = 'block';

                alertaElement.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="flex-grow-1">
                            <div class="d-flex align-items-center mb-1">
                                <div class="alert-icon me-3">
                                    <i class="fas fa-exclamation-triangle fa-lg text-${colorAlerta}"></i>
                                </div>
                                <div>
                                    <h5 class="alert-title mb-1">Estado de Cuentas Pendientes</h5>
                                    <p class="alert-subtitle mb-0 text-muted">Resumen financiero actual</p>
                                </div>
                            </div>

                            <div class="row g-3 mb-1">
                                <div class="col-md-4">
                                    <div class="metric-card p-3 rounded">
                                        <div class="metric-label text-muted small">Total Pendiente</div>
                                        <div class="metric-value h5 mb-1 text-${colorAlerta}">$${totalSaldo.toFixed(2)}</div>
                                        <div class="metric-trend">
                                            <span class="badge bg-${colorAlerta}-subtle text-${colorAlerta}">Por cobrar</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="metric-card p-3 rounded">
                                        <div class="metric-label text-muted small">Cuentas Pendientes</div>
                                        <div class="metric-value h5 mb-1 text-gray-700">${alertas.length}</div>
                                        <div class="metric-trend">
                                            <span class="badge bg-gray-200 text-gray-700">Activas</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="metric-card p-3 rounded">
                                        <div class="metric-label text-muted small">Clientes con Deuda</div>
                                        <div class="metric-value h5 mb-1 text-gray-700">${clientesUnicos.size}</div>
                                        <div class="metric-trend">
                                            <span class="badge bg-gray-200 text-gray-700">En seguimiento</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            ${alertaMasCritica ? `
                            <div class="critical-alert p-3 rounded border-start border-${colorAlerta} border-4">
                                <div class="d-flex align-items-center mb-2">
                                    <i class="fas fa-flag me-2 text-${colorAlerta}"></i>
                                    <strong class="me-2">Caso Requiere Atención:</strong>
                                    <span class="badge bg-${colorAlerta} text-white">
                                        ${alertaMasCritica.NivelAlerta}
                                    </span>
                                </div>
                                <div class="mb-1">
                                    <strong>Cliente:</strong> ${alertaMasCritica.Cliente}
                                </div>
                                <div class="mb-1">
                                    <strong>Servicio:</strong> ${alertaMasCritica.NOMBRE_SERVICIO}
                                </div>
                                <div class="mb-2">
                                    <strong>Saldo Pendiente:</strong> $${(alertaMasCritica.SALDO_PENDIENTE || 0).toFixed(2)}
                                </div>
                                <div class="recommendation p-2 bg-${colorAlerta}-subtle rounded">
                                    <small class="text-muted">
                                        <strong>Acción Recomendada:</strong> ${alertaMasCritica.AccionRecomendada}
                                    </small>
                                </div>
                            </div>` : ''}
                        </div>
                        <button type="button" class="btn-close ms-3 mt-1" onclick="ocultarAlerta()"></button>
                    </div>
                `;
            } else {
                alertaElement.style.display = 'none';
            }

            // 📦 DESPUÉS DE MOSTRAR PAGOS PENDIENTES, CARGAMOS ALERTAS DE INVENTARIO BAJO
            cargarAlertasInventarioBajo();
        },
        error: function (error) {
            console.error("Error al obtener pagos pendientes:", error);
            document.getElementById('alertasPagosPendientes').style.display = 'none';
            cargarAlertasInventarioBajo(); // también cargamos inventario bajo si hay error
        }
    });
}

// 📦 FUNCIÓN PARA ALERTAS DE INVENTARIO BAJO
function cargarAlertasInventarioBajo() {
    $.ajax({
        url: "inventario/alertasInventarioBajo",
        type: "GET",
        success: function (alertas) {
            const alertaElement = document.getElementById('alertasInventarioBajo');

            if (alertas && alertas.length > 0) {
                alertaElement.className = 'alert alert-warning mb-4 alerta-persistente';
                alertaElement.style.display = 'block';

                let productosHTML = alertas.map(a => `
                    <tr>
                        <td>${a.PRODUCTO}</td>
                        <td>${a.CATEGORIA}</td>
                        <td>${a.STOCK_ACTUAL}</td>
                        <td>${a.STOCK_MINIMO}</td>
                    </tr>
                `).join('');

                alertaElement.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="flex-grow-1">
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-box-open fa-lg text-warning me-2"></i>
                                <h5 class="alert-title mb-0">Alerta de Inventario Bajo</h5>
                            </div>
                            <p class="text-muted small mb-2">Productos con niveles de stock cercanos o por debajo del mínimo.</p>
                            <div class="table-responsive">
                                <table class="table table-sm table-bordered mb-0">
                                    <thead class="table-warning">
                                        <tr>
                                            <th>Producto</th>
                                            <th>Categoría</th>
                                            <th>Stock Actual</th>
                                            <th>Stock Mínimo</th>
                                        </tr>
                                    </thead>
                                    <tbody>${productosHTML}</tbody>
                                </table>
                            </div>
                        </div>
                        <button type="button" class="btn-close ms-3 mt-1" onclick="ocultarAlertaInventario()"></button>
                    </div>
                `;
            } else {
                alertaElement.style.display = 'none';
            }
        },
        error: function (error) {
            console.error("Error al obtener inventario bajo:", error);
            document.getElementById('alertasInventarioBajo').style.display = 'none';
        }
    });
}

// 🧹 FUNCIONES PARA OCULTAR
function ocultarAlerta() {
    document.getElementById('alertasPagosPendientes').style.display = 'none';
}

function ocultarAlertaInventario() {
    document.getElementById('alertasInventarioBajo').style.display = 'none';
}

// 🚀 CARGAR AL INICIAR
$(document).ready(function () {
    cargarAlertasPagosPendientes();
});
