$(function () {
    /*
    * Ajouter
    *
    * */
    //Actions Effectuées
    var actions = '';
    $('#addNature').tap(function () {
        if ($('#action').val() != '') {
            var newLi = $('<li></li>').html('<span class="actionAdded">' + $('#action').val().trim() + '</span><i class="iconfont icon-shanchu2"></i>');
            $('#actionList').append(newLi);
            actions += $('#action').val().trim() + ';';
            $('#action').val('');
        }
    })

    //A Refacturer
    var articlesAdded = '';
    $('#addRef').tap(function () {
        if ($('#actionRef').val() != '') {
            var newLi = $('<li></li>').html('<span class="refAdded">' + $('#actionRef').val().trim() + '</span><i class="iconfont icon-shanchu2"></i>');
            $('#RefList').append(newLi);
            articlesAdded += $('#actionRef').val().trim() + ';';
            $('#actionRef').val('');
        }
    })

    /*
    * Supprimer
    *
    * */
    //Actions
    $('#actionList').on('tap', 'li i', function () {
        var deleteAction = $(this).parent().find('span').text();
        var actionList = actions.split(';');
        $.each(actionList, function (index, item) {
            if (item === deleteAction) {
                actionList.splice(index, 1);
                return false;
            }
        })
        //renouveler actions après supprimer un élément
        actions = '';
        $.each(actionList, function (index, item) {
            if (item != '') {
                actions += item + ';';
            }
        })
        $(this).parent().remove();
    })

    //Refacturer
    $('#RefList').on('tap', 'li i', function () {
        var deleteArticle = $(this).parent().find('span').text();
        var articleList = articlesAdded.split(';');
        $.each(articleList, function (index, item) {
            if (item === deleteArticle) {
                articleList.splice(index, 1);
                return false;
            }
        })
        articlesAdded = '';
        $.each(articleList, function (index, item) {
            if (item != '') {
                articlesAdded += item + ';';
            }
        })
        $(this).parent().remove();
    });

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
            url: 'http://10.37.15.110:8080/saveServlet',
            type: 'post',
            data: $('#fiche').serialize(),
            traditional: true,
            success: function (data) {

                if (data == 1) {
                    alert("Merci de séléctionner une sociéte");
                    $('.detail div').css('border-color','cornflowerblue');
                    $('.societe').parent().css('border-color','red');
                } else if (data == 2) {
                    alert("Merci de séléctionner un centre");
                    $('.detail div').css('border-color','cornflowerblue');
                    $('.centre').parent().css('border-color','red');
                } else if (data == 3) {
                    alert("Merci de séléctionner un technicien");
                    $('.detail div').css('border-color','cornflowerblue');
                    $('.technicien').parent().css('border-color','red');
                } else if (data == 4) {
                    alert("Merci de séléctionner la date");
                    $('.detail div').css('border-color','cornflowerblue');
                    $('.date').parent().css('border-color','red');
                } else if (data == 5) {
                    alert("Merci de séléctionner l'heure arrivée");
                    $('.detail div').css('border-color','cornflowerblue');
                    $('#timeStart').parent().css('border-color','red');
                } else if (data == 6) {
                    alert("Merci de séléctionner l'heure départ");
                    $('.detail div').css('border-color','cornflowerblue');
                    $('#timeEnd').parent().css('border-color','red');
                } else {
                    alert('Merci pour remplir cette fiche!');
                    $('#client').val('');
                    $('#tech').val('');
                    $('#actions').val('');
                    $('#articles').val('');
                    let remplir = confirm("Vous voulez remplir une autre fiche? \n" +
                        "(Si vous voulez remplir une autre fiche après, actualiser cette page.)");
                    if (remplir==true){
                        document.querySelector('.title').scrollIntoView({
                            behavior: 'smooth'
                        });
                        window.location.reload();
                    }
                }
                document.querySelector('.title').scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    }

    $('.submit').tap(function () {
        var sign_Client = tablet1.getBase64().replaceAll('data:image/png;base64,', '');
        var sign_Tech = tablet2.getBase64().replaceAll('data:image/png;base64,', '');

        //Actions Effectuées
        $('#actions').val(actions);
        //A refacturer
        $('#articlesAdded').val(articlesAdded);
        //Signature centre
        $('#client').val(sign_Client);
        //Signature technicien
        $('#tech').val(sign_Tech);

        if($('.codePostal').val()==''){
            alert("Merci de saisir le code postal");
            $('.codePostal').parent().css('border-color','red');
            document.querySelector('.information').scrollIntoView({
                behavior: 'smooth'
            });
        }else{
            fSave();
        }
    })
})