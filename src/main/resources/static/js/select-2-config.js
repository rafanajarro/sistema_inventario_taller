$(document).ready(function(){
    document.querySelectorAll('.select-2').forEach((select) => {
        $(select).select2({
            placeholder: "Seleccionar...",
            language: {

                noResults: function () {
                    return "No hay resultado";
                },
                searching: function () {

                    return "Buscando..";
                }
            },
            theme: 'bootstrap-5'
        });
    });
});