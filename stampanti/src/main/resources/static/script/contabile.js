function accredita(code, fondi) {
    $.ajax({
        url: '../accredita',
        type: 'GET',
        data: { codice: code, fondi: fondi },
        success: function(response) {
            alert("Accreditamento effettuato");
        },
        error: function() {
            alert("Errore durante l'accreditamento");
        }
    });
}

function visualizzaUser(response) {
    var card = "<p>" + response.nome + "</p>";
    card += "<p>" + response.cognome + "</p>";
    card += "<p>" + response.codice + "</p>";
    card += "<p>" + response.ruolo + "</p>";
    card += "<p>" + response.fondi + "</p>";
    card += "<input type='number' id='valore'>";
    card += "<button class='accredita'>Accredita fondi</button>";
    $('#usr-dsplay').html(card);

    $('#usr-dsplay').on('click', '.accredita', function() {
        let fondi = $('#valore').val();
        accredita(response.codice, fondi);
    });
}

function cercaUtente() {
    let cod = $('#inputCodice').val();
    $.ajax({
        url: '../cercaUtente',
        type: 'GET',
        data: { codice: cod },
        success: function(response) {
            visualizzaUser(response);
        },
        error: function() {
            alert("Errore durante la ricerca dell'utente");
        }
    });
}

$(document).ready(function() {
    $.ajax({
        url: '../getRuolo',
        type: 'GET',
        success: function(response) {
            if (response !== 'contabile') {
                // Logout e reindirizzamento alla pagina di accesso
                $.ajax({
                    url: '/logout',
                    type: 'POST',
                    success: function() {
                        window.location.href = 'index.html';
                    },
                    error: function() {
                        alert('Errore durante il logout');
                    }
                });
            }
        },
        error: function() {
            alert('Errore durante il recupero del ruolo utente');
        }
    });

    $('#cerca').click(function() {
        cercaUtente();
    });
});
