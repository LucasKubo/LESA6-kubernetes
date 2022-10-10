package MeuRemedio.app.service;

import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import MeuRemedio.app.models.agendamentos.AgendamentosHorariosId;
import MeuRemedio.app.models.agendamentos.IntervaloDias;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.AgendamentosHorariosRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CalculaHorariosNotificacao {

    @Autowired
    IntervaloDiasRepository intervaloDiasRepository;

    @Autowired
    AgendamentosHorariosRepository agendamentosHorariosRepository;
    public void calcular(Agendamento agendamento){

        var instanteInicio =
                getDataInicio(agendamento).atTime(LocalTime.parse(agendamento.getHoraInicio(), horaFormatada()));
        final var instanteFinal
                = getDataFinal(agendamento).atTime(23, 59);

        //Montar lista de todas as horas para tomar o remédio
        List<LocalDateTime> horasRemedio = new ArrayList<>();
        horasRemedio.add(instanteInicio);

        boolean isIntervaloDias = verificarseIntervaloDias(agendamento);
        if (isIntervaloDias){
            IntervaloDias intervalo = intervaloDiasRepository.getById(agendamento.getId());

            //Monta lista de horários com intervalos
            while (instanteInicio.isBefore(instanteFinal)){
                int dia = instanteInicio.getDayOfMonth();
                instanteInicio = instanteInicio.plusHours(agendamento.getPeriodicidade());
                if(instanteInicio.getDayOfMonth() == dia){
                    horasRemedio.add(instanteInicio);
                } else {
                    instanteInicio = instanteInicio.plusDays(intervalo.getIntervaloDias());
                }
            }
        } else {
            //Monta lista de horários sem intervalo
            while (instanteInicio.isBefore(instanteFinal)) {
                instanteInicio = instanteInicio.plusHours(agendamento.getPeriodicidade());
                if (instanteInicio.isBefore(instanteFinal)) {
                    horasRemedio.add(instanteInicio);
                } else {
                    break;
                }
            }
        }

        for (int i = 0; i < horasRemedio.size(); i++){
            AgendamentosHorariosId agendamentosHorariosId = new AgendamentosHorariosId(agendamento.getId(), horasRemedio.get(i));
            AgendamentosHorarios agendamentosHorarios = new AgendamentosHorarios(agendamentosHorariosId, agendamento);
            agendamentosHorariosRepository.save(agendamentosHorarios);
        }
    }

    public boolean verificarseIntervaloDias(Agendamento agendamento){
        Optional<IntervaloDias> intervaloDias = intervaloDiasRepository.findById(agendamento.getId());
        return intervaloDias.isPresent();
    }

    //Converte a data inicío para LocalDate no formato adequado
    public LocalDate getDataInicio(Agendamento agendamento){
        DateTimeFormatter formatter = dataFormatada();
        return LocalDate.parse(agendamento.getDataInicio(), formatter);
    }

    //Converte a data fianl para LocalDate no formato adequado
    public LocalDate getDataFinal(Agendamento agendamento){
        DateTimeFormatter formatter = dataFormatada();
        return LocalDate.parse(agendamento.getDataFinal(), formatter);
    }

    //Formata data
    public DateTimeFormatter dataFormatada(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    //Formata hora
    public DateTimeFormatter horaFormatada(){
        return DateTimeFormatter.ofPattern("HH:mm");
    }
}
