package MeuRemedio.app.controllers;

import MeuRemedio.app.enums.MensagemEmail;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.service.EmailService;
import MeuRemedio.app.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EnvioEmail {
    @Autowired
    EmailService emailService;
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;

    @Autowired
    private FirebaseMessagingService firebaseService;

    public void emailRecuperarSenha (Usuario usuario, String codigo, String contextPath) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "8balls.integratedproject@gmail.com";
        String senderName = "Meu Remédio";
        String subject = "Recuperar senha";
        String content = "Prezado, "
                + "Por favor, clique no link abaixo para recuperar sua senha:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">RECUPERAR SENHA</a></h3>"
                + "Atenção ! O link é válido por <b>30 minutos</b> <br>"
                + "<br>"
                + "Obrigado,<br>"
                + "Meu Remédio.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(usuario.getEmail());
        helper.setSubject(subject);

        String verifyURL = contextPath + "/recuperar_senha?code=" + codigo;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }

    public void emailNotificacaoRemedio(Usuario usuario, List<Remedio> remedios, LocalDateTime instanteAgora) throws MessagingException, FirebaseMessagingException {
        String assunto = MensagemEmail.NOTIFICACAO_REMEDIO.getDescricao();

        String horaFormatada = instanteAgora.getHour() + ":" + (instanteAgora.getMinute()<10?"0":"") + instanteAgora.getMinute();
        StringBuilder remediosString = new StringBuilder("");
        for (Remedio remedio : remedios) {
            remediosString.append(remedio.getRM_Nome()    + " " +
                            remedio.getRM_Dosagem() + " " +
                            remedio.getRM_UnidadeDosagem())
                    .append("\n");
        }
        String msg = "Olá, " + usuario.getNome() + " " + usuario.getSobrenome() +
                "! Agora são " + horaFormatada + " e já está na hora de tomar os seus remédios: \n" + remediosString;

        emailService.sendEmail(usuario, assunto, msg);
        usuarioNotificationTokenRepository.findAllByIdUsuario(usuario.getId()).forEach(usuarioNotificationToken -> {
            try {
                firebaseService.sendNotification(assunto, msg, usuarioNotificationToken.getId().getToken());
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void emailValidacaoCadastro(Usuario user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "8balls.integratedproject@gmail.com";
        String senderName = "Meu Remédio";
        String subject = "Por favor, verifique seu cadastro";
        String content = "Prezado [[name]],<br>"
                + "Por favor, clique no link abaixo para verificar seu registro:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFICAR</a></h3>"
                + "Obrigado,<br>"
                + "Meu Remédio.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getNome());
        String verifyURL = siteURL + "/verificar_cadastro?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }

    public void emailDeletarCadastro(Usuario user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "8balls.integratedproject@gmail.com";
        String senderName = "Meu Remédio";
        String subject = "Sua conta foi excluída";
        String content = "Prezado [[name]],<br>"
                + "Sua conta foi excluída com sucesso. Caso queira voltar a utilizar o Meu Remédio, faça um novo cadastro."
                + "<br>Obrigado,<br>"
                + "Meu Remédio.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getNome());
        helper.setText(content, true);
        mailSender.send(message);
    }
}
