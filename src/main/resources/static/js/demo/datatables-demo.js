$(document).ready(function () {
  $('#dataTable').DataTable({
    "paging": true,
    "lengthMenu": [10, 25, 50, 100],
    "searching": true,
    "ordering": true,
    "info": true,
    "autoWidth": false,
    "responsive": true,
    "language": {
      "url": "/js/datatables/Spanish.json"  // Ruta local
    },
    "order": [[0, "asc"]],
    "pagingType": "full_numbers",
  });
});
