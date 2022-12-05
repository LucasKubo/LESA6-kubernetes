package MeuRemedio.app.service;

import MeuRemedio.app.mocks.AgendamentoMock;
import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.IntervaloDias;
import MeuRemedio.app.repository.AgendamentosHorariosRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CalculaHorariosNotificacaoTest {

    @Mock
    IntervaloDiasRepository intervaloDiasRepository;

    @Mock
    AgendamentosHorariosRepository agendamentosHorariosRepository;

    @InjectMocks
    CalculaHorariosNotificacao calculaHorariosNotificacao;

    @Test
    public void deveCalcularHorarios(){
        Assertions.assertDoesNotThrow(()-> calculaHorariosNotificacao.calcular(AgendamentoMock.agendamentoMock()));
    }

    @Test
    public void deveCalcularHorariosComIntervalo(){
        Mockito.when(intervaloDiasRepository.findById(any())).thenReturn(Optional.of(AgendamentoMock.intervaloDias()));
        Mockito.when(intervaloDiasRepository.getById(any())).thenReturn(AgendamentoMock.intervaloDias());
        Assertions.assertDoesNotThrow(()-> calculaHorariosNotificacao.calcular(AgendamentoMock.agendamentoMock()));
    }
}