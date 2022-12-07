package MeuRemedio.app.controllers.agendamento;

import MeuRemedio.app.controllers.remedio.RemedioController;
import MeuRemedio.app.mocks.AgendamentoMock;
import MeuRemedio.app.mocks.RemedioMock;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.AgendamentosHorariosRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.service.CalculaHorariosNotificacao;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AgendamentoControllerTest {
    @Mock
    ValidateAuthentication validateAuthentication;
    @Mock
    AgendamentoRepository agendamentoRepository;
    @Mock
    IntervaloDiasRepository intervaloDiasRepository;
    @Mock
    RemedioRepository remedioRepository;
    @Mock
    RemedioController remedioController;
    @Mock
    UserSessionService userSessionService;
    @Mock
    AgendamentosHorariosRepository agendamentosHorariosRepository;

    @Mock
    CalculaHorariosNotificacao calculaHorariosNotificacao;

    @Mock
    ModelMap modelMap;
    @Mock
    Model model;

    @Mock
    HttpServletRequest httpServletRequest;
    @InjectMocks
    AgendamentoController agendamentoController;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(agendamentoController, "validateAuthentication", validateAuthentication);
        ReflectionTestUtils.setField(agendamentoController, "userSessionService", userSessionService);
        ReflectionTestUtils.setField(agendamentoController, "agendamentoRepository", agendamentoRepository);
        ReflectionTestUtils.setField(agendamentoController, "intervaloDiasRepository", intervaloDiasRepository);
        ReflectionTestUtils.setField(agendamentoController, "remedioRepository", remedioRepository);
        ReflectionTestUtils.setField(agendamentoController, "agendamentosHorariosRepository", agendamentosHorariosRepository);
        ReflectionTestUtils.setField(agendamentoController, "remedioController", remedioController);
    }

    @DisplayName("Deve retornar Lista agendamento antigos")
    @Test
    public void viewAgendamentos(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(agendamentoRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(AgendamentoMock.agendamentoMock()));
        Mockito.when(intervaloDiasRepository.findById(any())).thenReturn(Optional.of(AgendamentoMock.intervaloDias()));
        String result = agendamentoController.viewAgendamentos(modelMap, "false");
        Assertions.assertEquals(result, "listas/ListaAgendamentos");
    }

    @DisplayName("Deve retornar Lista agendamentos ativos")
    @Test
    public void viewAgendamentosTrue(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(agendamentoRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(AgendamentoMock.agendamentoMock()));
        Mockito.when(intervaloDiasRepository.findById(any())).thenReturn(Optional.of(AgendamentoMock.intervaloDias()));
        String result = agendamentoController.viewAgendamentos(modelMap, "true");
        Assertions.assertEquals(result, "listas/ListaAgendamentos");
    }

    @DisplayName("Deve retornar Login na Lista Agendamento")
    @Test
    public void viewAgendamentosLogin(){
        String result = agendamentoController.viewAgendamentos(modelMap, "ativos");
        Assertions.assertEquals(result, "Login");
    }

    @DisplayName("Deve retornar Lista agendamento B")
    @Test
    public void viewAgendamentosB(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(agendamentoRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(AgendamentoMock.agendamentoMock()));
        Mockito.when(intervaloDiasRepository.findById(any())).thenReturn(Optional.of(AgendamentoMock.intervaloDias()));
        String result = agendamentoController.viewAgendamentosB(modelMap);
        Assertions.assertEquals(result, "listas/ListarAg");
    }

    @DisplayName("Deve retornar Login na Lista Agendamento")
    @Test
    public void viewAgendamentosLoginB(){
        String result = agendamentoController.viewAgendamentosB(modelMap);
        Assertions.assertEquals(result, "Login");
    }

    @DisplayName("Deve retornar Cadastrar agendamento")
    @Test
    public void viewCadastroAgendamento(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());
        String result = agendamentoController.viewCadastroAgendamento(modelMap);
        Assertions.assertEquals(result, "cadastros/CadastroAgendamento");
    }

    @DisplayName("Deve retornar Cadastrar agendamento com ID")
    @Test
    public void cadastrarAgendamentoRemedio(){
        String result = agendamentoController.cadastrarAgendamentoRemedio(1L, model);
        Assertions.assertEquals(result, "cadastros/CadastroAgendamentoDireto");
    }

    @DisplayName("Deve retornar Login na Cadastrar agendamento")
    @Test
    public void viewCadastroAgendamentoLogin(){
        String result = agendamentoController.viewCadastroAgendamento(modelMap);
        Assertions.assertEquals(result, "Login");
    }

    @DisplayName("Deve deletar agendamento")
    @Test
    public void deletarAgendamento(){
        String result = agendamentoController.deletarAgendamento(1, httpServletRequest);
        Assertions.assertEquals(result, "redirect:null");
    }

    @DisplayName("Deve Cadastrar agendamento com intervalo")
    @Test
    public void cadastrarAgendamento(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        String result = agendamentoController.cadastrarAgendamento(remedios, "2022-01-01", "00:00",
                "2022-01-02", 1L, 2L);
        Assertions.assertEquals(result, "redirect:/agendamentos/listar");
    }

    @DisplayName("Deve Cadastrar agendamento sem intervalo")
    @Test
    public void cadastrarAgendamentoSemIntervalo(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        String result = agendamentoController.cadastrarAgendamento(remedios, "2022-01-01", "00:00",
                "2022-01-02", 1L, null);
        Assertions.assertEquals(result, "redirect:/agendamentos/listar");
    }

    @DisplayName("Deve Cadastrar agendamento pelo Id remedio com intervalo")
    @Test
    public void cadastrarAgendamentoB(){
        String result = agendamentoController.cadastrarAgendamentoRemedio(1, "2022-01-01", "00:00",
                "2022-01-02", 1L, 2L);
        Assertions.assertEquals(result, "redirect:/agendamentos/listar");
    }

    @DisplayName("Deve Cadastrar agendamento pelo Id remedio sem intervalo")
    @Test
    public void cadastrarAgendamentoSemIntervaloB(){
        String result = agendamentoController.cadastrarAgendamentoRemedio(1, "2022-01-01", "00:00",
                "2022-01-02", 1L, null);
        Assertions.assertEquals(result, "redirect:/agendamentos/listar");
    }

    @DisplayName("Deve Atualizar Dados Agendamento sem intervalo")
    @Test
    public void atualizarDadosAgendamento(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        Mockito.when(agendamentoRepository.existsById(any())).thenReturn(true);
        Mockito.when(agendamentoRepository.findById(1)).thenReturn(AgendamentoMock.agendamentoMock());
        Mockito.when(agendamentoRepository.save(any())).thenReturn(AgendamentoMock.agendamentoMock());
        String result = agendamentoController.atualizarDadosAgendamento(1, remedios,"2022-01-01", "00:00",
                "2022-01-02", 1L, null);
        Assertions.assertEquals(result, "redirect:/agendamentos?ativos=true");
    }
    @DisplayName("Deve Atualizar Dados Agendamento com intervalo")
    @Test
    public void atualizarDadosAgendamentoComIntervalo(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        Mockito.when(agendamentoRepository.existsById(any())).thenReturn(true);
        String result = agendamentoController.atualizarDadosAgendamento(1, remedios,"2022-01-01", "00:00",
                "2022-01-02", 1L, 1l);
        Assertions.assertEquals(result, "redirect:/agendamentos?ativos=true");
    }
}