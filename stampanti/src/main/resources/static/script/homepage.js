function caricaHome() {
    

}
$(document).ready(function () {
    var numero_elementi = 0;
    caricaHome();
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

    $.ajax({
        url: '../getCoda',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            for (let index = 0; index < response.length; index++) {
                console.log("daje");
                console.log(index);
                numero_elementi+=1;
                var elemento_coda = '<div id = "'+response.id+'"><img src="'+ response.fronte+'"><img src="'+ response.retro+'"><br><p>Colorata? '+ response.colorato+'</p></div>';
                $("#elenco-container").append(elemento_coda);     
            }
        },
        error: function (xhr, status, error) {
            alert('Errore durante caricamento stampanti');
            console.log(xhr.responseText);
        }
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
                // Handle success response here
            },
            error: function (xhr, status, error) {
                alert('Errore durante caricamento stampanti');
                console.log(xhr.responseText);
            }
        });
    });



});