// FUNCIÓN MEJORADA PARA ALERTAS DE PAGOS PENDIENTES
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

                let colorAlerta = 'warning';
                let icono = '⚠️';

                if (alertaMasCritica && alertaMasCritica.NivelAlerta === 'CRÍTICO') {
                    colorAlerta = 'danger';
                    icono = '🚨';
                } else {
                    colorAlerta = 'warning';
                    icono = '⚠️';
                }

                alertaElement.className = `alert alert-${colorAlerta} mb-4 alerta-persistente`;
                alertaElement.style.display = 'block';

                alertaElement.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="flex-grow-1">
                            <!-- Header de la alerta -->
                            <div class="d-flex align-items-center mb-1">
                                <div class="alert-icon me-3">
                                    <i class="fas fa-exclamation-triangle fa-lg text-${colorAlerta}"></i>
                                </div>
                                <div>
                                    <h5 class="alert-title mb-1">Estado de Cuentas Pendientes</h5>
                                    <p class="alert-subtitle mb-0 text-muted">Resumen financiero actual</p>
                                </div>
                            </div>
                            
                            <!-- Métricas principales -->
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
                            
                            <!-- Alerta crítica -->
                            ${alertaMasCritica ? `
                            <div class="critical-alert p-3 rounded border-start border-${alertaMasCritica.NivelAlerta === 'CRÍTICO' ? 'danger' : 'warning'} border-4">
                                <div class="d-flex align-items-center mb-2">
                                    <i class="fas fa-flag me-2 text-${alertaMasCritica.NivelAlerta === 'CRÍTICO' ? 'danger' : 'warning'}"></i>
                                    <strong class="me-2">Caso Requiere Atención:</strong>
                                    <span class="badge bg-${alertaMasCritica.NivelAlerta === 'CRÍTICO' ? 'danger' : 'warning'} text-white">
                                        ${alertaMasCritica.NivelAlerta}
                                    </span>
                                </div>
                                <div class="mb-2">
                                    <strong>Cliente:</strong> ${alertaMasCritica.Cliente}
                                </div>
                                <div class="recommendation p-2 bg-${alertaMasCritica.NivelAlerta === 'CRÍTICO' ? 'danger' : 'warning'}-subtle rounded">
                                    <small class="text-muted">
                                        <strong>Acción Recomendada:</strong> ${alertaMasCritica.AccionRecomendada}
                                    </small>
                                </div>
                            </div>
                            ` : ''}
                        </div>
                        <button type="button" class="btn-close ms-3 mt-1" onclick="ocultarAlerta()"></button>
                    </div>
                `;

            } else {
                alertaElement.style.display = 'none';
            }
        },
        error: function (error) {
            console.error("Error:", error);
            document.getElementById('alertasPagosPendientes').style.display = 'none';
        }
    });
}

// FUNCIÓN PARA OCULTAR LA ALERTA
function ocultarAlerta() {
    document.getElementById('alertasPagosPendientes').style.display = 'none';
}

// CARGAR AL INICIAR
$(document).ready(function () {
    cargarAlertasPagosPendientes();
});