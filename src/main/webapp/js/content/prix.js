$(function () {
    //Calculer le prix étalonnage
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
})