// GRAFICO DE STOCK DE INVENTARIO
$.ajax({
    url: "/stockCategoria",
    type: "GET",
    success: function (data) {
        const labels = [];
        const stockValues = [];

        data.forEach(item => {
            labels.push(item.Categoria);
            stockValues.push(item.StockTotal || 0);
        });

        const ctx = document.getElementById('graficoStockCategoria').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Stock total por categoría',
                    data: stockValues,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.5)',
                        'rgba(54, 162, 235, 0.5)',
                        'rgba(255, 206, 86, 0.5)',
                        'rgba(75, 192, 192, 0.5)',
                        'rgba(153, 102, 255, 0.5)',
                        'rgba(255, 159, 64, 0.5)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Cantidad en Stock'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Categorías'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return `Stock: ${context.parsed.y} unidades`;
                            }
                        }
                    }
                }
            }
        });
    },
    error: function (error) {
        console.error("Error al obtener el stock por categoría:", error);
    }
});