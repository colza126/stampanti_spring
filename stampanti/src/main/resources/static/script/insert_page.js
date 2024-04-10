$(document).ready(function() {

    $.ajax({
        url: '../checkPrivilegi',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            if (response){
                console.log(response);
            }
        },
        error: function (xhr, status, error) {
            window.location.href = '../index.html';
            alert('Errore durante controllo sessione');
            console.log(xhr.responseText);
        }
    });

    $("#invia").on('click', function () {
        var nome = $("#inputNome").val();
        var cognome = $("#inputCognome").val();
        var codice = $("#inputCodice").val();
        var pw = $("#inputPw").val();
        var ruolo = $("#ruolo").val();

        $.ajax({
            url: '../register',
            type: 'GET',
            data: {
                nome: nome,
                cognome: cognome,
                codice: codice,
                pw: pw,
                ruolo: ruolo
            },
            success: function (response) {
                if (response){
                    alert('Utente inserito con successo');
                    
                } else {
                    alert('Errore durante inserimento utente');
                }
            },
            error: function (xhr, status, error) {
                alert('Errore durante inserimento utente');
                console.log(xhr.responseText);
            }
        });
    });



});