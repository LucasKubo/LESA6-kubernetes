package MeuRemedio.app.controllers.Remedio;

import MeuRemedio.app.controllers.remedio.RemedioController;
import MeuRemedio.app.mocks.AgendamentoMock;
import MeuRemedio.app.mocks.FinanceiroMock;
import MeuRemedio.app.mocks.RemedioMock;
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


    @BeforeEach
    public void init(){
        ReflectionTestUtils.setField(remedioController, "validateAuthentication", validateAuthentication);
        ReflectionTestUtils.setField(remedioController, "userSessionService", userSessionService);
        ReflectionTestUtils.setField(remedioController, "remedioRepository", remedioRepository);
        ReflectionTestUtils.setField(remedioController, "controleFinanceiro", controleFinanceiro);
        ReflectionTestUtils.setField(remedioController, "agendamentoRepository", agendamentoRepository);
    }


    @DisplayName("Deve retornar a lista de remedios")
    @Test
    public void verListaRemedios(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());


        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());

        String rm = remedioController.telaCadastroRemedio(modelMap);
        Assertions.assertEquals(rm, "Login");
    }

    @DisplayName("Deve retornar Cadastrar remedios")
    @Test
    public void viewCadastroAgendamento(){
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());

        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(new ArrayList<>());

        String result = remedioController.telaCadastroRemedio(modelMap);
        Assertions.assertEquals(result, "cadastros/CadastroRemedios");
    }

    @DisplayName("Deve deletar um remedio cadastrado")
    @Test
    public void deletar(){
        Remedio remedio = RemedioMock.remedioMock();
        Mockito.when(remedioRepository.existsById(any())).thenReturn(true);
        Mockito.when(remedioRepository.findById(1l)).thenReturn(remedio);
        Mockito.when(controleFinanceiro.findAllByRemedio(remedio)).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        Mockito.when(agendamentoRepository.findAllByRemedio(remedio)).thenReturn(Collections.singletonList(AgendamentoMock.agendamentoMock()));
        String del = remedioController.deletarRemedio(remedio.getRM_ID());
        Assertions.assertEquals(del, "redirect:/remedios");
    }

    @DisplayName("Deve retornar erro ao deletar um remedio não cadastrado")
    @Test
    public void ErroAodeletar(){
        Remedio remedio = RemedioMock.remedioMock();
        String del = remedioController.deletarRemedio(remedio.getRM_ID() + 550);
        Assertions.assertEquals(del, remedioController.templateError());
    }

    @DisplayName("Deve Cadastrar Remedio com agendamento e gasto ")
    @Test
    public void cadastrarAgendamento() throws Exception {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        String result = remedioController.CadastroRemedio("Doralgina","10","MG", "True", true,
                "2022-01-01","00:00","2022-01-01", 24L, 2L, "25,00", 25.00,1L);

        Assertions.assertEquals(result, "redirect:/remedios/remedios_cadastro");
    }
}
