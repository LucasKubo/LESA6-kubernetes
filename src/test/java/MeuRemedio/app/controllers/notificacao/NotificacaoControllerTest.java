package MeuRemedio.app.controllers.notificacao;

import MeuRemedio.app.mocks.UsuarioMock;
import MeuRemedio.app.models.usuarios.UsuarioNotificationToken;
import MeuRemedio.app.models.usuarios.UsuarioNotificationTokenId;
import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.UserSessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NotificacaoControllerTest {

    @Mock
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Mock
    UserSessionService userSessionService;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    HttpServletResponse httpServletResponse;
    @InjectMocks
    NotificacaoController notificacaoController;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(notificacaoController, "userSessionService", userSessionService);
    }

    @Test
    public void deveReceberToken(){
        UsuarioNotificationTokenId tokenId = new UsuarioNotificationTokenId();
        tokenId.setUsuario(1L);
        tokenId.setToken("1234");
        UsuarioNotificationToken token = new UsuarioNotificationToken();
        token.setId(tokenId);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(usuarioNotificationTokenRepository.findByIdToken(any())).thenReturn(token);
        String result = notificacaoController.receberTokenDeNotificacao("1234", httpServletResponse);
        Assertions.assertEquals("Home", result);
    }
}