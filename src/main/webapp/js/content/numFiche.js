$(function () {
    //Afficher le numero de fiche, demander à backend
    $.ajax({
        url: 'http://10.37.15.110:8080/numFiche',
        data: {},
        success: function (data) {
            $('#numInter').val(data);
        }
    })
})