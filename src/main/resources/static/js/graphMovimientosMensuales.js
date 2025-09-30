// GRÁFICO DE MOVIMIENTOS MENSUALES
        $.ajax({
            url: "/movimientosMensuales",
            type: "GET",
            success: function (data) {
                const labels = [];
                const entradas = [];
                const salidas = [];

                data.forEach(item => {
                    labels.push(item.Mes || item.MesTexto || item.Etiqueta);
                    entradas.push(item.Entradas || item.UnidadesEntrantes || 0);
                    salidas.push(item.Salidas || item.UnidadesSalientes || 0);
                });

                const ctx = document.getElementById('graficoMovimientos').getContext('2d');
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [
                            {
                                label: 'Entradas de Stock',
                                data: entradas,
                                borderColor: 'rgba(75, 192, 192, 1)',
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                borderWidth: 2,
                                fill: true,
                                tension: 0.4
                            },
                            {
                                label: 'Salidas de Stock',
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
                                text: 'Movimientos de Inventario Mensuales'
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
                                    text: 'Cantidad de Unidades'
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