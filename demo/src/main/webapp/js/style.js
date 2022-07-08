$(function () {
    $('.codePostal').on('change', function () {
        $('.societe').find('option').remove();
        var societeList = [];
        var reg = /^(?=.*\d)\d{4,5}$/g;
        if (reg.test($('.codePostal').val())) {
            $.each(records, function (index, item) {
                if (item.Code_Postal === parseInt($('.codePostal').val())) {
                    societeList.push(item.Societe);
                }
            });
        }

        $('.societe').append('<option hidden>--Merci de séléctionner une société--</option>')
        societeList = $.unique(societeList.sort());
        $.each(societeList, function (index, item) {
            $('.societe').append('<option>' + item + '</option>');
        });


    });


    $('.societe').on('change', function () {
        $('.centre').find('option').remove();
        var centreList = [];
        var reg = /^(?=.*\d)\d{4,5}$/g;

        if (reg.test($('.codePostal').val())) {
            $.each(records, function (index, item) {
                if (item.Code_Postal === parseInt($('.codePostal').val()) && item.Societe===$('.societe').val()) {
                    centreList.push(item.Code_Cosium);
                }
            });
        }

        $('.centre').append('<option hidden>--Merci de séléctionner un centre--</option>');
        centreList = $.unique(centreList.sort());
        $.each(centreList, function (index, item) {
            $('.centre').append('<option>' + item + '</option>');
        });
    });


    $('.add').on('mouseover touchstart',function () {
        $(this).css('opacity', '0.5');
    })
    $('.add').on('mouseout touchend',function () {
        $(this).css('opacity', '1');
    })

    //Ajouter
    //Actions Effectuées
    $('#addNature').on("tap", function () {
        $(this).css('opacity', '0.5');
        if ($('#action').val() != '') {
            var newLi = $('<li></li>').html('<div class="actionAdded"><h3>' + $('#action').val().trim() + '</h3><div><a href="javascript:;">supprimer</a></div></div>');
            $('#actionList').append(newLi);
        }
    })
    $('#addNature').on('click', function () {
        var newLi = $('<li></li>').html('<div class="actionAdded"><h3>' + $('#action').val().trim() + '</h3><div><a href="javascript:;">supprimer</a></div></div>');
        $('#actionList').append(newLi);
    })

    //A Refacturer
    $('#addRef').on("tap", function () {
        if ($('#actionRef').val() != '') {
            var newLi = $('<li></li>').html('<div class="actionAdded"><h3>' + $('#actionRef').val().trim() + '</h3><div><a href="javascript:;">supprimer</a></div></div>');
            $('#RefList').append(newLi);
        }
    })
    $('#addRef').on('click', function () {
        var newLi = $('<li></li>').html('<div class="actionAdded"><h3>' + $('#actionRef').val().trim() + '</h3><div><a href="javascript:;">supprimer</a></div></div>');
        $('#RefList').append(newLi);
    })

    //Supprimer
    $('#actionList').on('click','li div div a', function () {
        $(this).parent().parent().remove();
    })

    $('#RefList').on('click','li div div a', function () {
        $(this).parent().parent().remove();
    })


    //checkbox
    $('#domain').on('click','li',function () {
        $(this).siblings().find('input'). removeAttr('checked');
    })

    //slider
    $('#slider').on('change',function () {
        $('#note').html($("#slider").val());
        if ($("#slider").val()>5){
            $('.result').find('img').attr('src','img/happy.png');
        }else{
            $('.result').find('img').attr('src','img/unhappy.png');
        }
    })

    //signature
    $('.signature-pad').on('touchmove', function (e) { e.preventDefault(); }, false);
    var tablet1;
    $(function () {
        tablet1 = new Tablet("#signature_client", {
            defaultColor: "#000",
            response: true,
            onBeforeClear: function () {},
            onClear: function () {},
            autoResize: false,
            onInit: function () {
                var that = this;
                $('.clear-canvas').on('click tap', function () {
                    that.clear();
                })
            }
        });
        console.log(tablet1);
    });

    var tablet2;
    $(function () {
        tablet2 = new Tablet("#signature_technicien", {
            defaultColor: "#000",
            response: true,
            onBeforeClear: function () {},
            onClear: function () {},
            autoResize: false,
            onInit: function () {
                var that = this;
                $('.clear-canvas').on('click tap', function () {
                    that.clear();
                })
            }
        });
        console.log(tablet2);
    });
})