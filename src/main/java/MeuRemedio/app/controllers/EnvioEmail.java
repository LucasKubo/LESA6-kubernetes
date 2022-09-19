package MeuRemedio.app.controllers;

import MeuRemedio.app.enums.MensagemEmail;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.service.EmailService;
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

    public void emailRecuperarSenha (String email, String codigo){
        String assunto = MensagemEmail.RECUPERACAO_SENHA.getDescricao();
        String mensagem = MensagemEmail.RECUPERACAO_MENSAGEM.getDescricao() + codigo ;

       emailService.sendEmail(email, assunto, mensagem );
    }


    public void emailConfirmCadastro (Usuario usuario){
        String link = "https://meuremedioapp.herokuapp.com/login";
        String msgBoasVindas = "Olá, " + usuario.getNome() + " " + usuario.getSobrenome();
        String assunto = MensagemEmail.CADASTRO_REALIZADO.getDescricao();
        String mensagem = msgBoasVindas + MensagemEmail.CADASTRO_MENSAGEM.getDescricao() + link;

        emailService.sendEmail(usuario, assunto, mensagem );
    }



    public void emailCadastroRemedio(Usuario usuario, Remedio remedios){
        String assunto = MensagemEmail.REMEDIO_CADASTRADO.getDescricao();
        String msg = MensagemEmail.CADASTRO_REMEDIO.getDescricao() + remedios.getRM_Nome() + "''. " +
                "Fique atento aos horários e siga as restrições médicas !";

        emailService.sendEmail(usuario, assunto, msg);
    }

    public void emailNotificacaoRemedio(Usuario usuario, List<Remedio> remedios, LocalDateTime instanteAgora){
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
    }

    public void emailValidacaoCadastro(Usuario user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "8balls.integratedproject@gmail.com";
        String senderName = "Meu Remédio";
        String subject = "Please verify your registration";
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
}
