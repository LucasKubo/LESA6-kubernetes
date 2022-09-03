package MeuRemedio.app.service;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.models.agendamentos.Agendamento;

import MeuRemedio.app.models.agendamentos.IntervaloDias;

import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;

import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.AgendamentosHorariosRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import MeuRemedio.app.repository.RecorrenciaRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Transactional
public class NotificationService {

    @Autowired
    AgendamentoRepository agendamentoRepository;

    @Autowired
    IntervaloDiasRepository intervaloDiasRepository;

    @Autowired
    RecorrenciaRepository recorrenciaRepository;

    @Autowired
    EnvioEmail envioEmail;

    @Autowired
    AgendamentosHorariosRepository agendamentosHorariosRepository;


    final String ZONEID = "America/Sao_Paulo";


    @Scheduled(cron = "0 0 * * * *", zone = ZONEID)
    public void deletarAntigos(){
        var instanteAgora = LocalDateTime.now(ZoneId.of(ZONEID));
        agendamentosHorariosRepository.deletarPorHorario(instanteAgora);
    }

    @Scheduled(cron = "0 */5 * * * *", zone = ZONEID)
    @Async
    public void enviarNotificacao(){

        final var horaAgora = LocalTime.parse(LocalTime.now(ZoneId.of(ZONEID)).format(horaFormatada()));
        final var dataAgora = LocalDate.parse(LocalDate.now(ZoneId.of(ZONEID)).format(dataFormatada()));
        Iterable<Agendamento> agendamentosAgora = agendamentoRepository.findAll();

        // Percorre todos os agendamentos registrados no banco de dados
        for (Agendamento agendamento : agendamentosAgora) {
            boolean intervalo = verificarDataAtualDentroIntervalo(agendamento, dataAgora);
            if (intervalo) {
                verificarHoraRemedio(agendamento, horaAgora);
            }
        }
    }

    //Verificando se data atual está no intervalo de data início e data fim
    private boolean verificarDataAtualDentroIntervalo(Agendamento agendamento, LocalDate dataAgora){
        LocalDate dataInicio = getDataInicio(agendamento);
        LocalDate dataFinal = getDataFinal(agendamento);
        return dataAgora.compareTo(dataInicio) >= 0 &&
                dataAgora.compareTo(dataFinal) <= 0;
    }

    private void verificarHoraRemedio(Agendamento agendamento, LocalTime horaAgora){

        final var instanteAgora = LocalDate.now(ZoneId.of("America/Sao_Paulo")).atTime(horaAgora);

        List<AgendamentosHorarios> horasRemedio = agendamentosHorariosRepository.findAllByIdAgendamento(agendamento.getId());

        for (int i = 0; i < horasRemedio.size(); i++) {
            if (instanteAgora.isEqual(horasRemedio.get(i).getId().getHoraDataNotificacao())){
                getDadosUsuario(agendamento, instanteAgora);
                break;
            }
        }
    }

    //Chama a classe email controller
    private void getDadosUsuario(Agendamento agendamento, LocalDateTime instanteAgora){
        List<Remedio> remedios = agendamento.getRemedio();   // Recebe todos os remédios associados ao agendamento
        Usuario usuario = remedios.get(0).getUsuario();    // Recebe dados do usuário associado ao primeiro remédio da lista
        envioEmail.emailNotificacaoRemedio(usuario, remedios, instanteAgora);
    }

    //Converte a data inicío para LocalDate no formato adequado
    private LocalDate getDataInicio(Agendamento agendamento){
        DateTimeFormatter formatter = dataFormatada();
        return LocalDate.parse(agendamento.getDataInicio(), formatter);
    }

    //Converte a data final para LocalDate no formato adequado
    private LocalDate getDataFinal(Agendamento agendamento){
        DateTimeFormatter formatter = dataFormatada();
        return LocalDate.parse(agendamento.getDataFinal(), formatter);
    }

    //Formata data
    private DateTimeFormatter dataFormatada(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    //Formata hora
    private DateTimeFormatter horaFormatada(){
        return DateTimeFormatter.ofPattern("HH:mm");
    }
}

