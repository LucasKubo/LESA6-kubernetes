package MeuRemedio.app.controllers.Remedio;

import MeuRemedio.app.controllers.remedio.RemedioController;
import MeuRemedio.app.mocks.RemedioMock;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


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
        ReflectionTestUtils.setField(remedioController,"remedioController", remedioController);
        ReflectionTestUtils.setField(remedioController, "validateAuthentication", validateAuthentication);
        ReflectionTestUtils.setField(remedioController, "userSessionService", userSessionService);
        ReflectionTestUtils.setField(remedioController, "remedioRepository", remedioRepository);
    }


    @Test
    public void verListaRemedios(){
        Remedio remedio = RemedioMock.remedioMock();

        List<Remedio> lista = new ArrayList<>();
        lista.add(remedio);

        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(lista);
        String rm = remedioController.telaCadastroRemedio(modelMap);
        Assertions.assertEquals(rm, "Login");
    }
}
