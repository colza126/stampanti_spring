$(document).ready(function() {

    $.ajax({
        url: '../checkPrivilegi',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            if (response)
                window.location.href = "../index.html";
        },
        error: function (xhr, status, error) {
            alert('Errore durante controllo sessione');
            console.log(xhr.responseText);
        }
    });

    $('#insertButton').click(function() {
        var name = $('#name').val();
        var description = $('#description').val();
        var price = $('#price').val();
        var quantity = $('#quantity').val();
        var category = $('#category').val();
        var image = $('#image').val();

        if (name == "" || description == "" || price == "" || quantity == "" || category == "" || image == "") {
            alert("Please fill all the fields");
        } else {
            $.ajax({
                url: 'register',
                type: 'GET',
                contentType: 'application/json',
                data: JSON.stringify({
                    name: name,
                    description: description,
                    price: price,
                    quantity: quantity,
                    category: category,
                    image: image
                }),
                success: function(response) {
                    alert("utente aggiunto")
                    window.location.href = "http://localhost:8080/products";
                },
                error: function(response) {
                    alert("Error adding product");
                }
            });
        }
    });
});