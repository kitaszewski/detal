$(document).ready( function () {
    var table = $('#customersTable').DataTable({
        "language": {
        "lengthMenu": "Wyświetl _MENU_ wyników na stronę",
        "zeroRecords": "Brak wyników",
        "info": "Strona _PAGE_ z _PAGES_",
        "infoEmpty": "Brak danych",
        "infoFiltered": "(wybrane z _MAX_ wszystkich wyników)",
        "paginate": {
            "first": "Pierwsza",
            "last": "Ostatnia",
            "next": "Następna",
            "previous": "Poprzednia"
            },
             "search": "Szukaj:"
        },
        "sAjaxSource": "/detal/custlist",
        "sAjaxDataProp": "",
        "columnDefs": [ {
            "targets": -1,
            "data": null
            } ],
        "aoColumns": [
            { "mData": "surname" },
            { "mData": "name" },
            { "mData": "address" },
            { "render": function ( mData ) {
                          return '<a class="btn btn-sm btn-primary" href="/detal/customer/'+ mData.id +'">'+
                          "<i class='material-icons md-18'>more_vert</i>"
                          +'</a>';
                }
            }
        ]
	 })
});