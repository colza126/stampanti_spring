//ma fig
function caricaHome() {
    $("#elenco-container").empty();

    $.ajax({
        url: '../getCoda',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            console.log(response);
            for (let index = 0; index < response.length; index++) {
                console.log("daje");
                console.log(index);
                var elemento_coda = '<div id="' + response[index].id + '"><img class="image" src="../' + response[index].fronte + '" width = "300" height = "400" ><img src="../' + response[index].retro + '" width = "300" height = "400"><br><p>Colorata? ' + response[index].colorata + '</p></div>';
                $("#elenco-container").append(elemento_coda);
            }
        },
        error: function (xhr, status, error) {
            alert('Errore durante il caricamento degli elementi');
            console.log(xhr.responseText);
        }
    });
}


$(document).ready(function () {

    $("#btn-passa-inserisci").hide();
    var numero_elementi = 0;
    caricaHome();
    //sessione esiste
    $.ajax({
        url: '../sess_exist',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            if (response){
                console.log(response);
                
            }else{
                window.location.href = "../index.html";
            }
            
        },
        error: function (xhr, status, error) {
            alert('Errore durante controllo sessione');
            console.log(xhr.responseText);
        }
    });

    //privilegi
    $.ajax({
        url: '../checkPrivilegi',
        method: 'GET',
        dataType: 'json',
        success: function (response) {
            if (response) {
                $("#btn-passa-inserisci").show();

                $.ajax({
                    url: '../getStats',
                    type: 'GET',
                    dataType: 'json',
                    success: function (response) {
                        console.log(response);
                        for (let index = 0; index < response.length; index++) {
                            
                            var elemento_coda = '<div id="' + response[index].id + '"><img class="image" src="../' + response[index].fronte + '" width = "300" height = "400" ><img src="../' + response[index].retro + '" width = "300" height = "400"><br><p>Colorata? ' + response[index].colorata + '</p></div>';
                            $("#elenco-container").append(elemento_coda);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('Errore durante il caricamento degli elementi');
                        console.log(xhr.responseText);
                    }
                });


            } else {
                $("#btn-passa-inserisci").hide();
            }
        },
    });

    $("#btn-passa-inserisci").on('click', function () {

        $.ajax({
            url: "inserisci",
            type: "GET",
            dataType: "json",
            success: function(response) {
                
            }
        });
    });
    


    $('#btn-coda').on('click', function () {
        var fronte = $('#fronte').val();
        var retro = $('#retro').val();
        var colorato = $('#col_id').val();
        
        if(colorato == "on"){
            var colore = true;
        }else{
            var colore = false;

        }
        // Check if fronte is null
        if (!fronte) {
            console.log("Errore: Il campo 'fronte' è vuoto.");
            return; // End function execution
        }
        $.ajax({
            url: '../inserisci_coda',
            type: 'GET',
            dataType: 'json',
            data: {
                fronte: fronte,
                retro: retro,
                colorato : colore,
            },
            success: function (response) {
                caricaHome();
                // Handle success response here
            },
            error: function (xhr, status, error) {
                console.log('Errore durante caricamento stampanti');
                console.log(xhr.responseText);
            }
        });

    
        
    });
    $("#btn-stampa").on('click', function () {
        $.ajax({
            url: '../stampa',
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                
            },
            error: function (xhr, status, error) {
                alert('Errore durante caricamento stampanti');
                console.log(xhr.responseText);
            }
        });
    });

    $.ajax({
        url: '../getRuolo',
        type: 'GET',
        success: function(response) {
            if (response == 'contabile') {
                // Create the button for contabile role
                var btnPaginaContabile = $('<button id="btn-pagina-contabile">Pagina Contabile</button>');
                btnPaginaContabile.on('click', function () {
                    window.location.href = "paginaContabile.html";
                });
                $("#button-container").append(btnPaginaContabile);
            }
        },
        error: function() {
            alert('Failed to get user role');
        }
    });


});