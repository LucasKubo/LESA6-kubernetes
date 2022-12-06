package MeuRemedio.app.controllers.Remedio;

import MeuRemedio.app.controllers.remedio.RemedioController;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.test.util.ReflectionTestUtils;
import MeuRemedio.app.repository.RemedioRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import MeuRemedio.app.service.UserSessionService;
import static org.mockito.ArgumentMatchers.any;
import MeuRemedio.app.models.remedios.Remedio;
import org.junit.jupiter.api.DisplayName;
import MeuRemedio.app.mocks.RemedioMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ui.ModelMap;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import org.mockito.Mockito;
import java.util.ArrayList;
import org.mockito.Mock;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RemedioControllerTest {

    @Mock
    ValidateAuthentication validateAuthentication;

    @Mock
    ModelMap modelMap;

    @Mock
    RemedioRepository remedioRepository;

    @Mock
    RemedioController remedioController;

    @Mock
    UserSessionService userSessionService;

    @BeforeEach
    public void init(){
      //  ReflectionTestUtils.setField(remedioController, "remedioController", remedioController);
        ReflectionTestUtils.setField(remedioController, "validateAuthentication", validateAuthentication);
        ReflectionTestUtils.setField(remedioController, "userSessionService", userSessionService);
        ReflectionTestUtils.setField(remedioController, "remedioRepository", remedioRepository);
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


    @DisplayName("Deve retornar falso ao deletar um remedio não cadastrado") // OK
    @Test
    public void deletarError(){
        Remedio remedio = RemedioMock.remedioMock();
        String del = remedioController.deletarRemedio(99999);

        Assertions.assertFalse(Boolean.parseBoolean(del));
    }

    @DisplayName("Deve deletar um remedio")
    @Test
    public void deletar(){
        Remedio remedio = RemedioMock.remedioMock();
        String del = remedioController.deletarRemedio(remedio.getRM_ID());
        Assertions.assertEquals(del, "redirect:/remedios");
    }


    @DisplayName("Deve retornar falso ao cadastrar um remedio invalido") // OK
    @Test
    public void CadastrarRemediosComDadosInvalidos() throws Exception {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        String result = remedioController.CadastroRemedio("Doralgina","10","MG", "True", true,
                "2022-01-01","00:00","2021-01-01", 24L, 2L, "25,00", 25.00,1L);

        Assertions.assertFalse(Boolean.parseBoolean(result));
    }

    @DisplayName("Deve cadastrar um remedio ")
    @Test
    public void CadastrarRemedios() throws Exception {
        List<Remedio> remedios = Collections.singletonList(RemedioMock.remedioMock());
        String result = remedioController.CadastroRemedio("Doralgina","10","MG", "True", true,
                "2022-01-01","00:00","2022-01-01", 24L, 2L, "2022-01-01", 25.00,1L);

        Assertions.assertEquals(result, "redirect:/remedios/remedios_cadastro");
    }
}