$(function () {
    $('.sideInfos ul li').tap(function () {
        var lis = $('.sideInfos ul').find('li');
        $.each(lis, function (index, item) {
            $(item).removeClass("active");
        });
        $('.sideInfos ul li').css({'width': '1.59rem', 'height': '0.6667rem'})
        $(this).addClass('active');
        $('.active').css({'width': '1.8rem', 'height': '0.8rem', 'display': 'flex', 'align-items': 'center'});
        if ($(this).attr('id') == 'menu1') {
            document.querySelector('.information').scrollIntoView({
                behavior: 'smooth'
            });
        } else if ($(this).attr('id') == 'menu2') {
            document.querySelector('.nature').scrollIntoView({
                behavior: 'smooth'
            });
        } else {
            document.querySelector('.signature').scrollIntoView({
                behavior: 'smooth'
            });
        }
    })

    var navOff = false;
    $('.sideBtn').tap(function () {
        if (navOff === false) {
            navOff = true;
            $('.menu').fadeOut(300);
            $('.logo').fadeOut(300);
            $('.logo img').fadeOut(300);
            $('nav').css('width', '.78rem');
            $('.pad').css('width', '.78rem');

            $('.signature-pad .-tablet').css('width', '100%');
            $('.signature-pad .-tablet .-canvas-wrapper').css('width', '100%');
            $('.signature-pad .-tablet .-canvas-wrapper .tablet-canvas').css('width', '100%');
            $('.signature-pad .-tablet .-canvas-wrapper .backup-canvas').css('width', '100%');
        } else {
            navOff = false;
            $('nav').css('width', '1.8rem');
            $('.pad').css('width', '1.8rem');
            $('.menu').fadeIn(300);
            $('.logo').fadeIn(300);
            $('.logo img').fadeIn(300);
            $('.active').css({'display': 'flex', 'align-items': 'center'});
        }
    })

    //Afficher le numero de fiche, demander à backend
    $.ajax({
        url:'http://10.37.15.110:8080/numFiche',
        data:{},
        success:function (data) {
            data++;
            $('#numInter').val(data);
        }
    })


    $('.etalonnage input').tap(function () {
        if ($('.etalonnage input').is(':checked')) {
            $('.nbCabine').css('display', 'block');
        } else {
            $('.nbCabine').css('display', 'none');
        }
    })


    //Corriger le bug de l'excel ex.8400 -> 08400 et transformer le type à string
    $.each(records, function (index, item) {
        if (item.Code_Postal / 10000 < 1) {
            item.Code_Postal = '0' + item.Code_Postal;
        } else {
            item.Code_Postal = item.Code_Postal + '';
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
                    var newCode = item.Code_Postal.substring(0, len);
                    if (newCode === $('.codePostal').val()) {
                        societeList.push(item.Societe);
                    }
                })
            }
        } else if ($('.codePostal').val().length === 5) {
            if (reg.test($('.codePostal').val())) {
                $.each(records, function (index, item) {
                    if (item.Code_Postal === $('.codePostal').val()) {
                        societeList.push(item.Societe);
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
                    var newCode = item.Code_Postal.substring(0, len);
                    if (newCode === $('.codePostal').val() && item.Societe === $('.societe').val()) {
                        centreList.push(item.Code_Cosium);
                    }
                })
            }
        }else if ($('.codePostal').val().length===5){
            if (reg.test($('.codePostal').val())) {
                $.each(records, function (index, item) {
                    if (item.Code_Postal === $('.codePostal').val() && item.Societe === $('.societe').val()) {
                        centreList.push(item.Code_Cosium);
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

        $('.centre').append('<option hidden>--Merci de séléctionner un centre--</option>');
        $.each(centreClean, function (index, item) {
            $('.centre').append('<option>' + item + '</option>');
        });
    });

    //Obtenir la liste d'article
    $.ajax({
        url:'http://10.37.15.110:8080/searchArticle',
        dataType:'json',
        success:function (data) {
            $.each(data,function (index, item) {
                $('#articles').append('<option>'+item.name+'</option>');
            })
        }
    })

    //Calculer le prix
    $('.eta').text('0 €');
    /*$('.etalonnage input').tap(function () {
        if ($('.etalonnage input').is(':checked')) {
            $('.cabine').on('change', function () {
                var etalonnagePrice = ($('.cabine').val() - 1) * 250 + 500;
                $('.eta').text(etalonnagePrice + ' €');
            })
        }
    });*/

    //function pour transformer le temps en minute
    function calculerMin(time) {
        var timeArr = time.split(':');
        var mins = Number.parseInt(timeArr[0] * 60 + '') + Number.parseInt(timeArr[1] + '');
        return mins;
    }

    var timeStart;
    $('#timeStart').on('change', function () {
        timeStart = $('#timeStart').val();
    })
    var timeEnd;
    var period;

    //Si utilisateur choisit l'heure départ, calculer la différence de temps et le prix
    $('#timeEnd').on('change', function () {
        timeEnd = $('#timeEnd').val();
        if (timeStart != undefined && timeEnd != undefined) {
            var time1 = calculerMin(timeStart);
            var time2 = calculerMin(timeEnd);
            if ($('#rest').val() != '') {
                period = time2 - time1 - Number.parseInt($('#rest').val());
            } else {
                period = time2 - time1;
            }
            var hours = parseInt(period / 60 + '');
            if (hours >= 5) {
                $('.inter').text('500 €')
            } else {
                if (period % 60 == 0) {
                    $('.inter').text(hours * 100 + ' €')
                } else {
                    hours++;
                    $('.inter').text(hours * 100 + ' €')
                }
            }
        }
    })
    //S'il y une pause, recalculer le prix
    $('#rest').on('change', function () {
        if (period != undefined) {
            if ($('#rest').val() != '') {
                period -= $('#rest').val();
                var hours = parseInt(period / 60 + '');
                if (hours >= 5) {
                    $('.inter').text('500 €')
                } else {
                    if (period % 60 == 0) {
                        $('.inter').text(hours * 100 + ' €')
                    } else {
                        hours++;
                        $('.inter').text(hours * 100 + ' €')
                    }
                }
            }
        }
    })


    $('.add').on('mouseover touchstart', function () {
        $(this).css('opacity', '0.5');
    })
    $('.add').on('mouseout touchend', function () {
        $(this).css('opacity', '1');
    })

    //Ajouter
    //Actions Effectuées
    $('#addNature').tap(function () {
        if ($('#action').val() != '') {
            var newLi = $('<li></li>').html('<span class="actionAdded">'+$('#action').val().trim() +'</span><i class="iconfont icon-shanchu2"></i>');
            $('#actionList').append(newLi);
            $('#action').val('');
        }
    })

    //A Refacturer
    $('#addRef').tap(function () {
        if ($('#actionRef').val() != '') {
            var newLi = $('<li></li>').html('<span class="refAdded">'+$('#actionRef').val().trim() +'</span><i class="iconfont icon-shanchu2"></i>');
            $('#RefList').append(newLi);
            $('#actionRef').val('');
        }
    })

    //Supprimer
    $('#actionList').on('tap', 'li i', function () {
        $(this).parent().remove();
    })

    $('#RefList').on('tap', 'li i', function () {
        $(this).parent().remove();
    })


    //domaine
    $('#domain').on('mouseover touchstart', function () {
        $('#domain').css('box-shadow', '0px 0px 10px #ccc');
    })
    $('#domain').on('mouseout touchend', function () {
        $('#domain').css('box-shadow', '0px 0px 0px');
    })

    //slider
    $('#slider').on('change', function () {
        $('#note').html($("#slider").val());
        if ($("#slider").val() > 5) {
            $('.result').find('img').attr('src', 'img/happy.png');
        } else {
            $('.result').find('img').attr('src', 'img/unhappy.png');
        }
    })

    //signature
    var tablet1;
    $(function () {
        tablet1 = new Tablet("#signature_client", {
            defaultColor: "#000",
            response: true,
            autoResize: false,
            onInit: function () {
                var that = this;
                $('.clear-canvas-client').tap(function () {
                    that.clear();
                })
            }
        });
    });

    var tablet2;

    $(function () {
        tablet2 = new Tablet("#signature_technicien", {
            defaultColor: "#000",
            response: true,
            autoResize: false,
            onInit: function () {
                var that = this;
                $('.clear-canvas-tech').tap(function () {
                    that.clear();
                })
            }
        });
    });

    function fSave() {
        $.ajax({
            url:'http://10.37.15.110:8080/saveServlet',
            type:'post',
            data: $('#fiche').serialize(),
            traditional:true,
            success:function (data) {
                console.log(data);
                if (data==1){
                    alert("Merci de séléctionner une sociéte")
                }else{
                    alert('Merci pour remplir cette fiche!');
                    $('#client').remove();
                    $('#tech').remove();
                    window.location.reload();
                }
            }
        });
    }

    $('.submit').tap(function () {
        var sign_Client=tablet1.getBase64().replaceAll('data:image/png;base64,','');
        var sign_Tech=tablet2.getBase64().replaceAll('data:image/png;base64,','');

        $('#fiche').append('<input type="text" id="client" name="signClient">');
        $('#client').val(sign_Client);
        $('#fiche').append('<input type="text" id="tech" name="signTech">');
        $('#tech').val(sign_Tech);
        fSave();
    })
})
