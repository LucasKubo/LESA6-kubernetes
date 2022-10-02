package MeuRemedio.app.service;

import MeuRemedio.app.enums.StatusEmail;
import MeuRemedio.app.models.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    public StatusEmail sendEmail(Usuario usuario, String assunto, String mensagem) throws MessagingException {
        String emailRemetente = "8balls.integratedproject@gmail.com";

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(emailRemetente, "Meu Remédio");
            message.setTo(usuario.getEmail());
            message.setSubject(assunto);
            message.setText(mensagem);
            emailSender.send(mimeMessage);

            return StatusEmail.SENT;

        }catch (MailAuthenticationException | UnsupportedEncodingException e){
            throw new MailAuthenticationException(e);
        }
    }
    public StatusEmail sendEmail(String email, String assunto, String mensagem) throws MessagingException, UnsupportedEncodingException {
        String emailRemetente = "8balls.integratedproject@gmail.com";

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(emailRemetente, "Meu Remédio");
            message.setTo(email);
            message.setSubject(assunto);
            message.setText(mensagem);
            emailSender.send(mimeMessage);

            return StatusEmail.SENT;

        }catch (MailAuthenticationException e){
            throw new MailAuthenticationException(e);
        }
    }
}
