package MeuRemedio.app.service;

import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.utils.IAuthentication;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserSessionServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    private IAuthentication authenticationFacade;
    @Mock
    private Authentication authentication;

    @InjectMocks
    UserSessionService userSessionService;

    @Test
    public void deveRetornarUsernameUsuario(){
        Mockito.when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        Assertions.assertDoesNotThrow(() -> userSessionService.returnUsernameUsuario());
    }

    @Test
    public void deveRetornarIdUsuarioLogado(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Mockito.when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        Mockito.when(usuarioRepository.findByEmail(any())).thenReturn(usuario);
        Long result = userSessionService.returnIdUsuarioLogado();
        Assertions.assertEquals(result, usuario.getId());
    }
}