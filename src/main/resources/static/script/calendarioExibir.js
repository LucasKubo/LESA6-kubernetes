document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    const dataConst = Date.now();
    const hoje = new Date(dataConst);

    var id = document.getElementsByClassName('idAgendamento');
    var remedios = document.getElementsByClassName('remedios');
    var dataInicio = document.getElementsByClassName('dataInicio');
    var dataFinal = document.getElementsByClassName('dataFinal');

    var idIT = document.getElementsByClassName('idIntervaloDias');
    var remediosIT = document.getElementsByClassName('remediosIT');
    var dataInicioIT = document.getElementsByClassName('dataInicioIT');
    var dataFinalIT = document.getElementsByClassName('dataFinalIT');
    var intervaloIT = document.getElementsByClassName('intervaloDias');

    var listaAgendamentos = new Array();

    for (var i = 0; i < remediosIT.length; i++) {
        if(intervaloIT[i].innerHTML == 1){
            break;
        }
        var dataFinalDate = new Date(dataFinalIT[i].innerHTML)
        var dataFinalAjustada = new Date();
        dataFinalAjustada.setMonth(dataFinalDate.getMonth())
        dataFinalAjustada.setDate(dataFinalDate.getDate() + 2);
        dataFinalAjustada.setFullYear(dataFinalDate.getFullYear())
        var dataInicioAjustada = new Date(dataInicioIT[i].innerHTML);
        var dataInicioIntervalo = new Date();
        dataInicioIntervalo.setMonth(dataInicioAjustada.getMonth())
        dataInicioIntervalo.setDate(dataInicioAjustada.getDate() + 1)
        dataInicioIntervalo.setFullYear(dataInicioAjustada.getFullYear())
        while (dataInicioIntervalo < dataFinalAjustada) {
            var ag = {
                "groupId": idIT[i].innerHTML,
                "title": remediosIT[i].innerHTML,
                "start": dataInicioIntervalo.toLocaleDateString('en-CA'),
                "end": dataInicioIntervalo.toLocaleDateString('en-CA')
            }
            listaAgendamentos.push(ag);
            dataInicioIntervalo.setDate(dataInicioIntervalo.getDate() + parseInt(intervaloIT[i].innerHTML));
        }
    }

    for (var i = 0; i < remedios.length; i++) {

        if (listaAgendamentos.filter(e => e.groupId === id[i].innerHTML).length > 0) {
            console.log("Já adicionado")
        } else {
            var dataFinalDate = new Date(dataFinal[i].innerHTML)
            var dataFinalAjustada = new Date();
            dataFinalAjustada.setMonth(dataFinalDate.getMonth())
            dataFinalAjustada.setDate(dataFinalDate.getDate() + 2);
            dataFinalAjustada.setFullYear(dataFinalDate.getFullYear())
            var ag = {
                "groupId": id[i].innerHTML,
                "title": remedios[i].innerHTML,
                "start": dataInicio[i].innerHTML,
                "end": dataFinalAjustada.toLocaleDateString('en-CA')
            }
            listaAgendamentos.push(ag);
        }
    }
    var calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: ['interaction', 'dayGrid'],
        defaultDate: hoje,
        editable: false,
        eventLimit: true, // allow "more" link when too many events
        events: listaAgendamentos,
        dateClick: function(info){
            window.location.href="/cadastro_agendamentos?date=" + info.dateStr
        }
    });

    calendar.render();
});