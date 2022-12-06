package MeuRemedio.app.controllers.login;

import MeuRemedio.app.mocks.*;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.*;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    UserSessionService userSessionService;
    @Mock
    AgendamentoRepository agendamentoRepository;

    @Mock
    AgendamentosHorariosRepository agendamentosHorariosRepository;
    @Mock
    IntervaloDiasRepository intervaloDiasRepository;
    @Mock
    ValidateAuthentication validateAuthentication;
    @Mock
    FinanceiroRepository financeiroRepository;
    @Mock
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    RemedioRepository remedioRepository;
    @Mock
    ModelMap modelMap;
    @Mock
    HttpServletRequest httpServletRequest;

    @InjectMocks
    IndexController indexController;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(indexController, "validateAuthentication", validateAuthentication);
        ReflectionTestUtils.setField(indexController, "userSessionService", userSessionService);
        ReflectionTestUtils.setField(indexController, "agendamentoRepository", agendamentoRepository);
        ReflectionTestUtils.setField(indexController, "intervaloDiasRepository", intervaloDiasRepository);
        ReflectionTestUtils.setField(indexController, "remedioRepository", remedioRepository);
        ReflectionTestUtils.setField(indexController, "agendamentosHorariosRepository", agendamentosHorariosRepository);
        ReflectionTestUtils.setField(indexController, "financeiroRepository", financeiroRepository);
        ReflectionTestUtils.setField(indexController, "usuarioNotificationTokenRepository", usuarioNotificationTokenRepository);
    }

    @Test
    public void deveRetornarHomeAuthenticationNull(){
        Assertions.assertEquals("Login", indexController.home(modelMap, httpServletRequest));
    }

    @Test
    public void voiddeveRetornarHome(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(financeiroRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        Assertions.assertEquals("Home", indexController.home(modelMap, httpServletRequest));
    }

    @Test
    public void deveRetornarHomeGastoAnoPassado(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(financeiroRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(FinanceiroMock.gastoMockAnoPassado()));
        Assertions.assertEquals("Home", indexController.home(modelMap, httpServletRequest));
    }
    @Test
    public void deveRetornarHomeGastoAnoRetrasado(){
        Financeiro gasto = FinanceiroMock.gastoMockAnoPassado();
        gasto.setData("2020-05-01");
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(financeiroRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(gasto));
        Assertions.assertEquals("Home", indexController.home(modelMap, httpServletRequest));
    }


    @Test
    public void deveRetornarHomeGastoAnoQueVem(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Mockito.when(financeiroRepository.findAllByUsuarioID(any())).thenReturn(Collections.singletonList(FinanceiroMock.gastoMockAnoQueVem()));
        Assertions.assertEquals("Home", indexController.home(modelMap, httpServletRequest));
    }

    @Test
    public void deveRetornarIndex(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Assertions.assertEquals("redirect:/home", indexController.Index(httpServletRequest));
    }
    @Test
    public void deveRetornarIndexFail(){
        Cookie[] cookies = new Cookie[1];
        Cookie cookie = new Cookie("tokenNotification", "1234");
        cookies[0] = cookie;
        Mockito.when(httpServletRequest.getCookies()).thenReturn(cookies);
        Assertions.assertEquals("Index", indexController.Index(httpServletRequest));
    }
}