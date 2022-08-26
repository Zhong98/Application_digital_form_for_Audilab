$(function () {
    //Obtenir les données de centre
    var records = [];
    $.ajax({
        url: 'http://10.37.15.110:8080/searchCentre',
        dataType: 'json',
        success: function (data) {
            records = data;

            //Corriger le bug de l'excel ex.8400 -> 08400 et transformer le type à string
            $.each(records, function (index, item) {
                if (item.codePostal / 10000 < 1) {
                    item.codePostal = '0' + item.codePostal;
                } else {
                    item.codePostal = item.codePostal + '';
                }
            })

            //Trouver les sociétés
            $('.codePostal').on('change', function () {
                $('.societe').find('option').remove();
                var societeList = [];

                //Permettre utilisateur entre 3;37;370 etc
                var reg = /^(?=.*\d)\d{1,5}$/g;
                if ($('.codePostal').val().length < 5) {
                    if (reg.test($('.codePostal').val())) {
                        var len = $('.codePostal').val().length;
                        $.each(records, function (index, item) {
                            var newCode = item.codePostal.substring(0, len);
                            if (newCode === $('.codePostal').val()) {
                                societeList.push(item.societe);
                            }
                        })
                    }
                } else if ($('.codePostal').val().length === 5) {
                    if (reg.test($('.codePostal').val())) {
                        $.each(records, function (index, item) {
                            if (item.codePostal === $('.codePostal').val()) {
                                societeList.push(item.societe);
                            }
                        });
                    }
                } else {
                    alert("Le formule du code postal n'est pas correct. Merci de corriger.");
                }

                //remplacer les options dupliquées
                $('.societe').append('<option hidden>--Merci de selectionner une societe--</option>')
                var societeClean = [];
                $.each(societeList, function (index, item) {
                    if (societeClean.length === 0) {
                        societeClean.push(item);
                    } else {
                        if (societeClean.indexOf(item) == -1) {
                            societeClean.push(item);
                        }
                    }
                })
                $.each(societeClean, function (index, item) {
                    $('.societe').append('<option>' + item + '</option>');
                });
            });

            //Trouver les centres
            $('.societe').on('change', function () {
                $('.centre').find('option').remove();
                var centreList = [];
                var reg = /^(?=.*\d)\d{1,5}$/g;
                if ($('.codePostal').val().length < 5) {
                    if (reg.test($('.codePostal').val())) {
                        var len = $('.codePostal').val().length;
                        $.each(records, function (index, item) {

                            //obtenir le string en meme length
                            var newCode = item.codePostal.substring(0, len);
                            if (newCode === $('.codePostal').val() && item.societe === $('.societe').val()) {
                                centreList.push(item.codeCosium);
                            }
                        })
                    }
                } else if ($('.codePostal').val().length === 5) {
                    if (reg.test($('.codePostal').val())) {
                        $.each(records, function (index, item) {
                            if (item.codePostal === $('.codePostal').val() && item.societe === $('.societe').val()) {
                                centreList.push(item.codeCosium);
                            }
                        });
                    }
                }

                var centreClean = [];
                $.each(centreList, function (index, item) {
                    if (centreClean.length === 0) {
                        centreClean.push(item);
                    } else {
                        if (centreClean.indexOf(item) == -1) {
                            centreClean.push(item);
                        }
                    }
                })

                $('.centre').append('<option hidden>--Merci de selectionner un centre--</option>');
                $.each(centreClean, function (index, item) {
                    $('.centre').append('<option>' + item + '</option>');
                });
            });
        }
    });

    //Chercher les techniciens
    $.ajax({
        url:'http://10.37.15.110:8080/searchTech',
        dataType: 'json',
        success:function (data) {
            $('.technicien').append('<option hidden>--Merci de selectionner un technicien--</option>');
            $.each(data,function (index, item) {
                $('.technicien').append('<option>' + item + '</option>');
            })
        }
    })
})