package MeuRemedio.app.controllers;

import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.models.usuarios.UsuarioNotificationToken;
import MeuRemedio.app.models.usuarios.UsuarioNotificationTokenId;
import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.service.EmailService;
import MeuRemedio.app.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EnvioEmailTest {
    @Mock
    EmailService emailService;
    @Mock
    JavaMailSender mailSender;
    @Mock
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Mock
    private FirebaseMessagingService firebaseService;

    @Mock
    MimeMessage message;
    @InjectMocks
    EnvioEmail envioEmail;

    @DisplayName("Deve enviar email recuperar senha")
    @Test
    public void emailRecuperarSenha() throws MessagingException, UnsupportedEncodingException {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        Mockito.when(mailSender.createMimeMessage()).thenReturn(message);
        Assertions.assertDoesNotThrow(() -> envioEmail.emailRecuperarSenha(usuario, "1", "1"));
    }

    @DisplayName("Deve enviar email Notificação Remédio")
    @Test
    public void emailNotificacaoRemedio() throws MessagingException, UnsupportedEncodingException, FirebaseMessagingException {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@teste.com");
        Remedio remedio = new Remedio();
        remedio.setRM_ID(1l);
        remedio.setRM_Nome("teste");
        remedio.setRM_Dosagem("1");
        remedio.setRM_UnidadeDosagem("1");
        UsuarioNotificationTokenId tokenId = new UsuarioNotificationTokenId();
        tokenId.setToken("1234");
        tokenId.setUsuario(1L);
        UsuarioNotificationToken token = new UsuarioNotificationToken();
        token.setId(tokenId);
        Mockito.when(usuarioNotificationTokenRepository.findAllByIdUsuario(any())).thenReturn(Collections.singletonList(token));
        Assertions.assertDoesNotThrow(() -> envioEmail.emailNotificacaoRemedio(usuario, Collections.singletonList(remedio), LocalDateTime.now()));
    }

    @DisplayName("Deve enviar email validação cadastro")
    @Test
    public void emailValidacaoCadastro() throws MessagingException, UnsupportedEncodingException {
        Usuario usuario = new Usuario();
        usuario.setNome("teste");
        usuario.setVerificationCode("abcd");
        usuario.setEmail("teste@teste.com");
        Mockito.when(mailSender.createMimeMessage()).thenReturn(message);
        Assertions.assertDoesNotThrow(() -> envioEmail.emailValidacaoCadastro(usuario, "https://meuremedioapp.herokuapp.com"));
    }

    @DisplayName("Deve enviar email excluir conta")
    @Test
    public void emailDeletarCadastro() throws MessagingException, UnsupportedEncodingException {
        Usuario usuario = new Usuario();
        usuario.setNome("teste");
        usuario.setVerificationCode("abcd");
        usuario.setEmail("teste@teste.com");
        Mockito.when(mailSender.createMimeMessage()).thenReturn(message);
        Assertions.assertDoesNotThrow(() -> envioEmail.emailDeletarCadastro(usuario));
    }
}