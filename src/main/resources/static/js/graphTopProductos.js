// GRAFICO DE TOP PRODUCTOS
$.ajax({
    url: "/topProductos",
    type: "GET",
    success: function (data) {
        const labels = [];
        const totalUtilizado = [];
        const colores = [
            'rgba(255, 99, 132, 0.7)',
            'rgba(54, 162, 235, 0.7)',
            'rgba(255, 206, 86, 0.7)',
            'rgba(75, 192, 192, 0.7)',
            'rgba(153, 102, 255, 0.7)',
            'rgba(255, 159, 64, 0.7)',
            'rgba(199, 199, 199, 0.7)',
            'rgba(83, 102, 255, 0.7)',
            'rgba(40, 159, 64, 0.7)',
            'rgba(210, 99, 132, 0.7)'
        ];

        data.forEach((item, index) => {
            // Limitar nombre del producto si es muy largo
            const nombreCorto = item.Producto.length > 20 ?
                item.Producto.substring(0, 20) + '...' : item.Producto;

            labels.push(nombreCorto);
            totalUtilizado.push(item.TotalUtilizado || 0);
        });

        const ctx = document.getElementById('graficoTopProductos').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Total Utilizado',
                    data: totalUtilizado,
                    backgroundColor: colores,
                    borderColor: colores.map(color => color.replace('0.7', '1')),
                    borderWidth: 1
                }]
            },
            options: {
                indexAxis: 'y', // Hace el gráfico horizontal
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Top 10 Productos Más Utilizados'
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const item = data[context.dataIndex];
                                return [
                                    `Producto: ${item.Producto}`,
                                    `Categoría: ${item.Categoria}`,
                                    `Total utilizado: ${context.parsed.x} unidades`,
                                    `Total vendido: $${(item.TotalVendido || 0).toFixed(2)}`,
                                    `Veces utilizado: ${item.VecesUtilizado || 0}`
                                ];
                            }
                        }
                    },
                    legend: {
                        display: false
                    }
                },
                scales: {
                    x: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Cantidad de Unidades Utilizadas'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Productos'
                        }
                    }
                }
            }
        });
    },
    error: function (error) {
        console.error("Error al obtener top productos:", error);
    }
});