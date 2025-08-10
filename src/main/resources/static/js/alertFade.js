setTimeout(() => {
    const alertElements = document.querySelectorAll('.alert');
    alertElements.forEach(alertElement => {
        // Evita eliminar alertas persistentes
        if (!alertElement.classList.contains('alerta-persistente')) {
            alertElement.classList.remove('show');
            alertElement.classList.add('fade');
            setTimeout(() => alertElement.remove(), 500);
        }
    });
}, 5000);
