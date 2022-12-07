
package MeuRemedio.app.controllers.Remedio;

import MeuRemedio.app.controllers.agendamento.AgendamentoController;
import MeuRemedio.app.controllers.remedio.RemedioController;
import MeuRemedio.app.mocks.AgendamentoMock;
import MeuRemedio.app.mocks.FinanceiroMock;
import MeuRemedio.app.mocks.RemedioMock;
import MeuRemedio.app.mocks.UsuarioMock;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.repository.*;
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
import org.springframework.beans.factory.annotation.Autowired;
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
public class RemedioControllerTest {

    @Mock
    ValidateAuthentication validateAuthentication;

    @Mock
    ModelMap modelMap;

    @Mock
    RemedioRepository remedioRepository;

    @InjectMocks
    RemedioController remedioController;

    @Mock
    UserSessionService userSessionService;
    @Mock
    FinanceiroRepository controleFinanceiro;

    @Mock
    AgendamentoRepository agendamentoRepository;

    @Mock
    AgendamentoController agendamentoController;
    @Mock
    UsuarioRepository usuarioRepository;


    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(remedioController, "validateAuthentication", validateAuthentication);
        ReflectionTestUtils.setField(remedioController, "userSessionService", userSessionService);
        ReflectionTestUtils.setField(remedioController, "remedioRepository", remedioRepository);
        ReflectionTestUtils.setField(remedioController, "controleFinanceiro", controleFinanceiro);
        ReflectionTestUtils.setField(remedioController, "agendamentoRepository", agendamentoRepository);
        ReflectionTestUtils.setField(remedioController, "agendamentoController", agendamentoController);
    }


    @DisplayName("Deve retornar a tela de cadastro de remedios")
    @Test
    public void verTelaCadastroRemedios() {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());


        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());

        String rm = remedioController.telaCadastroRemedio(modelMap);
        Assertions.assertEquals(rm, "cadastros/CadastroRemedios");
    }

    @DisplayName("Deve retornar a tela de listar remedios com Modal")
    @Test
    public void verTelaCadastroRemediosModal() {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());


        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());

        String rm = remedioController.telaCadastroRemedioModal(modelMap);
        Assertions.assertEquals(rm, "listas/ListaRemediosB");
    }

    @DisplayName("Deve retornar a tela de listar remedios com Modal")
    @Test
    public void verTelaListar() {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());


        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());

        String rm = remedioController.listaRemedios(modelMap);
        Assertions.assertEquals(rm, "listas/ListaRemedios");
    }

    @DisplayName("Deve retornar Cadastrar remedios")
    @Test
    public void viewCadastroAgendamento() {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());

        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());

        String result = remedioController.telaCadastroRemedio(modelMap);
        Assertions.assertEquals(result, "cadastros/CadastroRemedios");
    }

    @DisplayName("Deve retornar falso ao deletar um remedio não cadastrado")
    @Test
    public void deletarError() {
        Remedio remedio = RemedioMock.remedioMock();
        String del = remedioController.deletarRemedio(99999);

        Assertions.assertFalse(Boolean.parseBoolean(del));
    }

    @DisplayName("Deve deletar um remedio")
    @Test
    public void deletar() {
        Remedio remedio = RemedioMock.remedioMock();
        Mockito.when(remedioRepository.existsById(any())).thenReturn(true);
        Mockito.when(remedioRepository.findById(1l)).thenReturn(remedio);
        Mockito.when(controleFinanceiro.findAllByRemedio(remedio)).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        Mockito.when(agendamentoRepository.findAllByRemedio(remedio)).thenReturn(Collections.singletonList(AgendamentoMock.agendamentoMock()));
        String del = remedioController.deletarRemedio(remedio.getRM_ID());
        Assertions.assertEquals(del, "redirect:/remedios");
    }

    @DisplayName("Deve cadastrar um remedio ")
    @Test
    public void CadastrarRemedios() throws Exception {
        //Mockito.when(agendamentoController.cadastrarAgendamento(any(), any(), any(), any(), any(), any())).thenReturn("string");
        Mockito.when(userSessionService.returnUsernameUsuario()).thenReturn("1");
        Mockito.when(usuarioRepository.findByEmail(any())).thenReturn(UsuarioMock.usuarioMock());
        String result = remedioController.CadastroRemedio("Doralgina", "10", "MG", "True", true,
                "2022-01-01", "00:00", "2022-01-01", 24L, 2L, "2022-01-01", 25.00, 1L);

        Assertions.assertEquals(result, "redirect:/remedios/remedios_cadastro");
    }
}