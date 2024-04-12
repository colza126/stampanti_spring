function accredita(code, fondi){
    $.ajax({
        url: '/accredita',
        type: 'POST',
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
    card += "<input type = 'number' id = 'accredit'>"
    card += "<button id = 'accredita'>accredita fondi</button>";
}


function cercaUtente() {
    let cod = $('#codice').val();
    $.ajax({
        url: '/cercaUtente',
        type: 'GET',
        data: {codice : cod},
        success: function(response) {
            

        },
        error: function() {
            
        }
        
    });
}


$(document).ready(function() {

    

});