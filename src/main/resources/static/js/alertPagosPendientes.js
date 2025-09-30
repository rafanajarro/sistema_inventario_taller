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
                            <h5 class="alert-heading">${icono} Alertas de Pagos Pendientes</h5>
                            
                            <div class="row mt-3">
                                <div class="col-md-4">
                                    <strong>Total Pendiente:</strong><br>
                                    <span class="badge bg-${colorAlerta}">$${totalSaldo.toFixed(2)}</span>
                                </div>
                                <div class="col-md-4">
                                    <strong>Cuentas Pendientes:</strong><br>
                                    <span class="badge bg-secondary">${alertas.length}</span>
                                </div>
                                <div class="col-md-4">
                                    <strong>Clientes con Deuda:</strong><br>
                                    <span class="badge bg-secondary">${clientesUnicos.size}</span>
                                </div>
                            </div>
                            
                            ${alertaMasCritica ? `
                            <div class="mt-3 p-2 bg-light   rounded">
                                <strong>Alerta más crítica:</strong><br>
                                <span class="badge bg-${alertaMasCritica.NivelAlerta === 'CRÍTICO' ? 'danger' : 'warning'}">
                                    ${alertaMasCritica.NivelAlerta}
                                </span>
                                - ${alertaMasCritica.Cliente}<br>
                                <small>Recomendación: ${alertaMasCritica.AccionRecomendada}</small>
                            </div>
                            ` : ''}
                        </div>
                        <button type="button" class="btn-close" onclick="ocultarAlerta()"></button>
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