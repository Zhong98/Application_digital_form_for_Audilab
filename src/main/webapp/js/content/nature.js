$(function () {
    //Obtenir la liste d'article
    $.ajax({
        url: 'http://10.37.15.110:8080/searchArticle',
        dataType: 'json',
        success: function (data) {
            $.each(data, function (index, item) {
                $('#articles').append('<option>' + item.name + '</option>');
            })
        }
    })
})