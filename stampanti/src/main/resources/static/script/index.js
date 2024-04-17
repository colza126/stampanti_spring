$(document).ready(function () {
    // Centra il contenuto verticalmente
    $(".container").css("margin-top", ($(window).height() - $(".container").height()) / 2);

    // Applica i colori tendenti al nero
    $("body").css({
        "background-color": "#000",
        "color": "#fff"
    });
    $(".form-label").css("color", "#fff");
    $(".btn-primary").css({
        "background-color": "#333",
        "border-color": "#333"
    });

    // Mostra o nasconde il modulo di login al clic
    $('#showLoginForm').click(function () {
        $('#loginForm').toggle();
        $('#registrationForm').hide();
        $('#recoveryPw').hide();
    });

    // Funzione al submit del form di login
    $('#loginForm').submit(function (e) {
        e.preventDefault();
        var codice = $('#loginCodice').val();
        var password = $('#loginPassword').val();

        $.ajax({
            type: 'GET',
            url: 'login',
            data: {
                codice: codice,
                pass: password
            },
            dataType: 'json',
            complete: function (xhr, textStatus) {
                console.log('Request completed with status: ' + textStatus);
            },
            success: function (response) {
                if (response == true) {
                    $.ajax({
                        url: 'home',
                        type: 'GET',
                        dataType: 'json',
                        success: function (response) {}
                        
                    });

                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('Status:', status);
                console.error('XHR:', xhr);
            }
        });

        return false;
    });
});
