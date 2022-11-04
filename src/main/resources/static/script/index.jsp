//Esta fun��o se encarrega de trocar o idioma da p�gina
$(document).ready(function () {
        
        //id do elemento html = "#locales" (� um select)
        $("#locales").change(function () {
            
            //Variavel armazena o value do elemento "#locales"
            var selectedOption = $('#locales').val();
            
            // Verifica e seta o idioma escolhido
            if (selectedOption != '') {
                window.location.replace('?lang=' + selectedOption);
            }
        });
    });
