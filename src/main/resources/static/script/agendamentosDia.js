function aumentarData(){
    var dataAtual = document.getElementById("dataSelecionar").innerHTML  
    var dateParts = dataAtual.split("/");
    // month is 0-based, that's why we need dataParts[1] - 1
    var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 
    dateObject.setDate(dateObject.getDate() + 1);
    dataSelecionar.innerHTML = dateObject.toLocaleDateString("pt-BR");

    exibir(dateObject)
}

function diminuirData(){
    var dataAtual = document.getElementById("dataSelecionar").innerHTML    
    var dateParts = dataAtual.split("/");
    // month is 0-based, that's why we need dataParts[1] - 1
    var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 
    dateObject.setDate(dateObject.getDate() - 1);

    var hoje = new Date()
    hoje.setHours(0,0,0,0)
    console.log(hoje)
    console.log(dateObject)
    if (dateObject >= hoje){
        dataSelecionar.innerHTML = dateObject.toLocaleDateString("pt-BR");
        exibir(dateObject)
    }
}

function exibir(dateObject){
    var dataSelecionar = document.getElementById("dataSelecionar")
    var horariosData = document.getElementsByClassName("datasAgendamento")
    var horariosExibir = document.getElementsByClassName("horariosExibir")

    var dateObjectString= dateObject.toLocaleDateString("pt-BR");

    for (i = 0; i< horariosData.length; i++){
        console.log(horariosData[i].innerHTML)
        if (horariosData[i].innerHTML != dateObjectString){
            horariosExibir[i].setAttribute("hidden", true)
        } else if (horariosData[i].innerHTML == dateObjectString){
            horariosExibir[i].removeAttribute("hidden")
        }
    }
}    