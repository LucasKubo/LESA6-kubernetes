//Esta funcao se encarrega de trocar o idioma da p�gina
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




$(document).ready(function () {
$("#sidebar").mCustomScrollbar({
theme: "minimal"
});

$('#dismiss, .overlay').on('click', function () {
$('#sidebar').removeClass('active');
$('.overlay').removeClass('active');
});

$('#sidebarCollapse').on('click', function () {
$('#sidebar').addClass('active');
$('.overlay').addClass('active');
$('.collapse.in').toggleClass('in');
$('a[aria-expanded=true]').attr('aria-expanded', 'false');
});
});


$('select').selectpicker();


$(function () {
$('.selectpicker').selectpicker();
});

function mensagemDosagem(){
const mensagem = document.getElementById("mensagemDosagem");
mensagem.innerHTML = "EX: 10, 50, 500"
}
function retirarMensagemDosagem(){
const mensagem = document.getElementById("mensagemDosagem");
mensagem.innerHTML = ""
}
function mensagemUnidade(){
const mensagem = document.getElementById("mensagemUnidade");
mensagem.innerHTML = "EX: miligrama (mg), gramas (g) mililitro (ml), gotas e microgotas"
}
function retirarMensagemUnidade(){
const mensagem = document.getElementById("mensagemUnidade");
mensagem.innerHTML = ""
}



function mensagemPeriodicidade(){
const mensagem = document.getElementById("mensagemPeriodocidade");
mensagem.innerHTML = "EX: 1, 2 ou 3 (horas)"
}
function retirarMensagemPeriodicidade(){
const mensagem = document.getElementById("mensagemPeriodocidade");
mensagem.innerHTML = ""
}

function mensagemDataFim(){
const mensagem = document.getElementById("mensagemFimAgendamento");
mensagem.innerHTML = "Informe a data de término do agendamento"
}
function retirarMensagemDataFim(){
const mensagem = document.getElementById("mensagemFimAgendamento");
mensagem.innerHTML = ""
}

function mensagemDataInicio(){
const mensagem = document.getElementById("mensagemInicioAgendamento");
mensagem.innerHTML = "Informe a data de início do agendamento"
}
function retirarMensagemDataInicio(){
const mensagem = document.getElementById("mensagemInicioAgendamento");
mensagem.innerHTML = ""
}

function mensagemHora(){
const mensagem = document.getElementById("mensagemHora");
mensagem.innerHTML = "Informe o horário que deverá tomar o remédio"
}
function retirarMensagemHora(){
const mensagem = document.getElementById("mensagemHora");
mensagem.innerHTML = ""
}

function validaDataFinal(){
    const dataFinal = document.getElementById("AG_DataFinal").value;
    const dataInicio = document.getElementById("AG_DataInicio").value;

    var dataFinalData = new Date(dataFinal + " ")
    var dataInicioData = new Date(dataInicio+ " ")
    var hoje = new Date()
    hoje.setHours(0,0,0,0);

    let error = document.getElementById("error-date");
    let errorHoje = document.getElementById("error-hoje");
    let errorDataLimite = document.getElementById("error-dataLimite");

    if(dataFinalData != null && dataInicioData !=null){
        var diffTempo = dataFinalData - dataInicioData;
        const diferenca = Math.ceil(diffTempo / (1000 * 60 * 60 * 24)); 
        console.log(diferenca)
        if (diferenca > 1825){
            errorDataLimite.innerHTML = "Não é possível cadastrar agendamentos com prazo maior de 5 anos.";
            document.querySelector('#confirmar').disabled = false;
        }
    }
    console.log(dataFinalData)
    console.log(dataInicioData)
    console.log(hoje)
    if (dataInicioData > dataFinalData){
        error.innerHTML = "A data final não pode ser menor que a data inicial";
        document.querySelector('#confirmar').disabled = true;
    } else {
        error.innerHTML = "";
    }
    if (dataFinalData < hoje){
        errorHoje.innerHTML = "Não é possível cadastrar um agendamento já finalizado.";
        document.querySelector('#confirmar').disabled = true;
    } else {
        errorHoje.innerHTML = "";
    }
    if(dataInicioData <= dataFinalData && dataFinalData >= hoje){
        error.innerHTML = "";
        errorHoje.innerHTML = "";
        document.querySelector('#confirmar').disabled = false;
    }
}

function checarBox(){
    let error = document.getElementById("error-date");
    let errorHoje = document.getElementById("error-hoje");

    if (document.getElementById('exampleCheck1').checked === false){
        error.innerHTML = "";
        errorHoje.innerHTML = "";
        document.querySelector('#confirmar').disabled = false;
    }
}
