$.ajax({
    url: "/movimientosMensuales",
    type: "GET",
    success: function (data) {
        const labels = [];
        const entradas = [];
        const salidas = [];

        data.forEach(item => {
            labels.push(item.MesTexto || item.Etiqueta || item.Mes);
            entradas.push(item.Entradas || 0);
            salidas.push(item.Salidas || 0);
        });

        const ctx = document.getElementById('graficoMovimientos').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Pagos en Efectivo',
                        data: entradas,
                        borderColor: 'rgba(75, 192, 192, 1)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4
                    },
                    {
                        label: 'Pagos con Tarjeta',
                        data: salidas,
                        borderColor: 'rgba(255, 99, 132, 1)',
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Pagos Mensuales por Método de Pago'
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Cantidad de Pagos'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Meses'
                        }
                    }
                },
                interaction: {
                    mode: 'nearest',
                    axis: 'x',
                    intersect: false
                }
            }
        });
    },
    error: function (error) {
        console.error("Error al obtener movimientos mensuales:", error);
    }
});