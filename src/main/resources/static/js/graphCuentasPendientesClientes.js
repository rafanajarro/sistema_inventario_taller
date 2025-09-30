// GRÁFICO DE CUENTAS PENDIENTES CLIENTES 
$.ajax({
    url: "/cuentasPendientesClientes",
    type: "GET",
    success: function (data) {
        const clientesConSaldo = data.filter(item => (item.SaldoTotalPendiente || 0) > 0);
        const topClientes = clientesConSaldo.slice(0, 6);
        const otrosClientes = clientesConSaldo.slice(6);

        const labels = topClientes.map(item => {
            return item.Cliente.length > 12 ?
                item.Cliente.substring(0, 12) + '...' : item.Cliente;
        });

        if (otrosClientes.length > 0) {
            labels.push('Otros');
        }

        const saldos = topClientes.map(item => item.SaldoTotalPendiente || 0);

        if (otrosClientes.length > 0) {
            const sumaOtros = otrosClientes.reduce((sum, item) => sum + (item.SaldoTotalPendiente || 0), 0);
            saldos.push(sumaOtros);
        }

        const ctx = document.getElementById('graficoDistribucionSaldos').getContext('2d');
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: saldos,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.7)',
                        'rgba(54, 162, 235, 0.7)',
                        'rgba(255, 206, 86, 0.7)',
                        'rgba(75, 192, 192, 0.7)',
                        'rgba(153, 102, 255, 0.7)',
                        'rgba(255, 159, 64, 0.7)',
                        'rgba(199, 199, 199, 0.7)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Distribución de Saldos Pendientes'
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = Math.round((context.parsed / total) * 100);
                                return `${context.label}: $${context.parsed.toFixed(2)} (${percentage}%)`;
                            }
                        }
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    },
    error: function (error) {
        console.error("Error:", error);
    }
});