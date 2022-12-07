package MeuRemedio.app.controllers.usuario;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.repository.*;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.UsuarioService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    EnvioEmail emailCadastro;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    UsuarioRepository_2 usuarioRepository_2;
    @Mock
    RemedioRepository remedioRepository;
    @Mock
    AgendamentoRepository agendamentoRepository;
    @Mock
    ValidateAuthentication validateAuthentication;
    @Mock
    UserSessionService userSessionService;
    @Mock
    FinanceiroRepository financeiroRepository;
    @Mock
    UsuarioService usuarioService;
    @Mock
    SessionRegistry sessionRegistryImpl;
    @Mock
    DashBoardsRepository dashBoardsRepository;
    @InjectMocks
    UsuarioController usuarioController;

    @Mock
    HttpServletRequest httpServletRequest;

    @Test
    @DisplayName("Deve exibir tela cadastro usuário")
    public void telaCadasUsuario(){
        Mockito.when(validateAuthentication.auth()).thenReturn(true);
        String result = usuarioController.telaCadasUsuario();
        Assertions.assertEquals("redirect:/home", result);
    }

    @Test
    @DisplayName("Deve falhar exibir tela cadastro usuário")
    public void telaCadasUsuarioFalha(){
        Mockito.when(validateAuthentication.auth()).thenReturn(false);
        String result = usuarioController.telaCadasUsuario();
        Assertions.assertEquals("cadastros/CadastroUsuario", result);
    }

    @Test
    @DisplayName("Deve cadastrar usuário")
    public void CadastrarUsuario() throws MessagingException, UnsupportedEncodingException {
        Mockito.when(usuarioService.verificaExistencia(any())).thenReturn(false);
        String result = usuarioController.CadastrarUsuario("Teste", "teste", "teste@teste.com", "12345678",
                "2000-01-01", "M", httpServletRequest);
        Assertions.assertEquals("validacoes/AvisoVerificacaoEmail", result);
    }
    @Test
    @DisplayName("Deve Falhar cadastrar usuário")
    public void CadastrarUsuarioFalha() throws MessagingException, UnsupportedEncodingException {
        Mockito.when(usuarioService.verificaExistencia(any())).thenReturn(true);
        String result = usuarioController.CadastrarUsuario("Teste", "teste", "teste@teste.com", "12345678",
                "2000-01-01", "M", httpServletRequest);
        Assertions.assertEquals("redirect:/cadastro?emailExistente", result);
    }
}