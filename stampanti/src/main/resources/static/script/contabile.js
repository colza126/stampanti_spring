function accredita(code, fondi){
    
    $.ajax({
        url: '../accredita',
        type: 'GET',
        data: {codice : code, fondi : fondi},
        success: function(response) {
            alert("accreditamento effettuato");
        },
        error: function() {
            alert("errore");
        }
        
    });

}

function visualizzaUser(respose) {
    var card = "<p>"+respose.nome+"</p>";
    card += "<p>"+respose.cognome+"</p>";
    card += "<p>"+respose.codice+"</p>";
    card += "<p>"+respose.ruolo+"</p>";
    card += "<p>"+respose.fondi+"</p>";
    card += "<input type = 'number' id = 'valore'>"
    card += "<button id = 'accredita'>accredita fondi</button>";
    $('#usr-dsplay').append(card);


    
    $('#accredita').click(function() {
        let cod = $('#inputCodice').val();
        let fondi = $('#valore').val();
        accredita(cod, fondi);
    });
}


function cercaUtente() {
    let cod = $('#inputCodice').val();
    $.ajax({
        url: '../cercaUtente',
        type: 'GET',
        data: {codice : cod},
        success: function(response) {
            visualizzaUser(response);

        },
        error: function() {
            
        }
        
    });
}


$(document).ready(function() {

    $.ajax({
        url: '../getRuolo',
        type: 'GET',
        success: function(response) {
            if (response !== 'contabile') {
                // Clear session and redirect to index.html
                $.ajax({
                    url: '/logout',
                    type: 'POST',
                    success: function() {
                        window.location.href = 'index.html';
                    },
                    error: function() {
                        alert('Failed to clear session');
                    }
                });
            }
        },
        error: function() {
            alert('Failed to get user role');
        }
    });

    $('#cerca').click(function() {
        cercaUtente();
    });

    

});