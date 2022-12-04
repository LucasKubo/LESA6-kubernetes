package MeuRemedio.app.mocks;

import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import MeuRemedio.app.models.agendamentos.AgendamentosHorariosId;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AgendamentosHorariosMock {
    public static AgendamentosHorarios agendamentoHorariosMock(){
        AgendamentosHorariosId agendamentosHorariosId = new AgendamentosHorariosId();
        agendamentosHorariosId.setAgendamento(1L);
        LocalDateTime data = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).withSecond(0).withNano(0);
        agendamentosHorariosId.setHoraDataNotificacao(data);
        AgendamentosHorarios agendamento = new AgendamentosHorarios();
        agendamento.setId(agendamentosHorariosId);
        return agendamento;
    }
}
