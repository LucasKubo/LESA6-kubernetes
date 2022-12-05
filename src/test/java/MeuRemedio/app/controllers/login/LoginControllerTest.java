package MeuRemedio.app.controllers.login;

import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    ValidateAuthentication validateAuthentication;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Mock
    HttpServletRequest httpServletRequest;
    @InjectMocks
    LoginController loginController;

    @Test
    public void deveRetornarIndex(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        Assertions.assertEquals("redirect:/home", loginController.login(httpServletRequest));
    }
    @Test
    public void deveRetornarIndexFail(){
        Cookie[] cookies = new Cookie[1];
        Cookie cookie = new Cookie("tokenNotification", "1234");
        cookies[0] = cookie;
        Mockito.when(httpServletRequest.getCookies()).thenReturn(cookies);
        Assertions.assertEquals("Login", loginController.login(httpServletRequest));
    }

    @Test
    public void deveRetornarLogout(){
        Assertions.assertEquals("Login", loginController.logout());
    }
}