package MeuRemedio.app.mocks;

import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.IntervaloDias;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;

import java.time.LocalDate;
import java.util.Collections;

public class AgendamentoMock {

    public static Agendamento agendamentoMock(){
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setDataInicio(String.valueOf(LocalDate.now()));
        agendamento.setDataFinal(String.valueOf(LocalDate.now()));
        agendamento.setPeriodicidade(8);
        agendamento.setHoraInicio("10:00");
        agendamento.setAG_Criado_em(LocalDate.now());
        Remedio remedio = new Remedio();
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        remedio.setRM_ID(1L);
        remedio.setUsuario(usuario);
        agendamento.setRemedio(Collections.singletonList(remedio));
        return agendamento;
    }

    public static IntervaloDias intervaloDias(){
        IntervaloDias agendamento = new IntervaloDias();
        agendamento.setId(1L);
        agendamento.setDataInicio(String.valueOf(LocalDate.now()));
        agendamento.setDataFinal(String.valueOf(LocalDate.now()));
        agendamento.setPeriodicidade(8);
        agendamento.setHoraInicio("10:00");
        agendamento.setAG_Criado_em(LocalDate.now());
        agendamento.setIntervaloDias(2);
        Remedio remedio = new Remedio();
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        remedio.setRM_ID(1L);
        remedio.setUsuario(usuario);
        agendamento.setRemedio(Collections.singletonList(remedio));
        return agendamento;
    }
}
