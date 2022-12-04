package MeuRemedio.app.service;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.mocks.AgendamentoMock;
import MeuRemedio.app.mocks.AgendamentosHorariosMock;
import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.AgendamentosHorariosRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import MeuRemedio.app.repository.RecorrenciaRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.mail.MessagingException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Spy
    @InjectMocks
    private NotificationService notificationService;

    @Mock
    EnvioEmail envioEmail;

    @Mock
    AgendamentosHorariosRepository agendamentosHorariosRepository;

    @Mock
    IntervaloDiasRepository intervaloDiasRepository;

    @Mock
    RecorrenciaRepository recorrenciaRepository;

    @DisplayName("Deve validar envio de notificações")
    @Test
    public void verificarDataAtualTest() throws MessagingException, FirebaseMessagingException {
        Agendamento agendamentoMock = AgendamentoMock.agendamentoMock();
        AgendamentosHorarios agendamentosHorarios = AgendamentosHorariosMock.agendamentoHorariosMock();
        Mockito.when(agendamentoRepository.findAll()).thenReturn(Collections.singletonList(agendamentoMock));
        Mockito.when(agendamentosHorariosRepository.findAllByIdAgendamento(any())).thenReturn(Collections.singletonList(agendamentosHorarios));
        Assertions.assertDoesNotThrow(() -> notificationService.enviarNotificacao());
    }

    @DisplayName("Deve validar deletes de horarios antigos")
    @Test
    public void deveDeletarAntigos()  {
        Assertions.assertDoesNotThrow(() -> notificationService.deletarAntigos());
    }

}