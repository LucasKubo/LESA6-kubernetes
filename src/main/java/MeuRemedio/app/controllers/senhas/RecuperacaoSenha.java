package MeuRemedio.app.controllers.senhas;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.models.usuarios.Usuario_code;
import MeuRemedio.app.repository.UserCodeRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

@Controller
public class RecuperacaoSenha {
    @Autowired
    EnvioEmail envioEmail;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    UserCodeRepository usuarioCode;


    protected String emailUsuario;
    @RequestMapping(value = "/enviarEmail", method = RequestMethod.GET)
    public String receberEmail(){

        return "EmailRecuperacao";
    }

    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.GET)
    public String atualizarSenha(){
        return "RecuperarSenha";
    }

    @RequestMapping(value = "/enviarEmail", method = RequestMethod.POST)
    public String receberEmail (@RequestParam("US_Email") String email) throws MessagingException, UnsupportedEncodingException {

       Usuario verificarEmailUsuarioExistente =  usuarioRepository.findByEmail(email);
       Usuario_code verificarEmailCod = usuarioCode.findByEmail(email);

       /*Se quebrar o envio de email, remover esse IF*/

        if(Objects.nonNull(verificarEmailCod)) {
                verificarEmailCod.setCodigo(codigoValidacao());
                usuarioCode.save(verificarEmailCod);
                envioEmail.emailRecuperarSenha(verificarEmailCod.getEmail(), verificarEmailCod.getCodigo());
        }

       if (Objects.nonNull(verificarEmailUsuarioExistente) && (Objects.isNull(verificarEmailCod))) {
           Usuario_code user = new Usuario_code(email, codigoValidacao());
           usuarioCode.save(user);

           Usuario_code userEmail = usuarioCode.findByEmail(email);
           envioEmail.emailRecuperarSenha(userEmail.getEmail(), userEmail.getCodigo());

           return "redirect:/login?em_env";

       } else
           if (Objects.nonNull(verificarEmailUsuarioExistente) && (Objects.nonNull(verificarEmailCod))) {
                return "redirect:/enviarEmail?code_env";
       }
           return "redirect:/enviarEmail?emailFail";
    }

    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.POST)
    public String atualizarSenha (@RequestParam("US_Codigo") String codigo, @RequestParam("US_Senha") String senha) {
        Usuario_code userCodigo = usuarioCode.findByCodigo(codigo);

      if (Objects.nonNull(userCodigo) ){
           Usuario usuario = usuarioRepository.findByEmail(userCodigo.getEmail());
           usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

           usuarioRepository.save(usuario);
           usuarioCode.delete(userCodigo);

           return "redirect:/login?att";
      }
            return "redirect:/recuperar_senha?codigoErro";
    }

    public String codigoValidacao (){
        int[] codigo = new int [8];
        SecureRandom random = new SecureRandom();
        String codValidacao= "";

        for (int i = 0; i < 8; i ++ ){
            codigo[i] = random. nextInt(10);
            codValidacao += "" + codigo[i];
        }
        return codValidacao;
    }
}

