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


    $('.etalonnage input').tap(function () {
        if ($('.etalonnage input').is(':checked')) {
            $('.nbCabine').css('display', 'block');
        } else {
            $('.nbCabine').css('display', 'none');
            $('.cabine').val("");
        }
    })

    $('.add').on('mouseover touchstart', function () {
        $(this).css('opacity', '0.5');
    })
    $('.add').on('mouseout touchend', function () {
        $(this).css('opacity', '1');
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
})
