package MeuRemedio.app.controllers.senhas;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.TokenResetarSenhaRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.RecuperarSenhaService;
import MeuRemedio.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Controller
public class RecuperacaoSenha {
    @Autowired
    EnvioEmail envioEmail;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RecuperarSenhaService recuperarSenhaService;
    @Autowired
    TokenResetarSenhaRepository tokenResetarSenhaRepository;


    protected String emailUsuario;
    @RequestMapping(value = "/enviarEmail", method = RequestMethod.GET)
    public String receberEmail(){return "EmailRecuperacao";}

    @RequestMapping(value = "/enviarEmail", method = RequestMethod.POST)
    public String receberEmail (@RequestParam("US_Email") String email, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        Usuario usuario =  usuarioRepository.findByEmail(email);

        if(usuario == null){
            return "redirect:/enviarEmail?emailFail";
        }

        String token = UUID.randomUUID().toString();
        usuarioService.criarTokenResetarSenha(usuario, token);
        envioEmail.emailRecuperarSenha(usuario,token, getSiteURL(request));
        return "redirect:/login?em_env";
    }

    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.GET)
    public String atualizarSenha(@RequestParam("code") String token){
        String validadeToken = recuperarSenhaService.validarTokenResetarSenha(token);
        if(validadeToken == null){
            return "RecuperarSenha";
        } else if (validadeToken.equals("invalidToken")) {
            return "redirect:/login?em_fail";
        }else{
            return "redirect:/login?exp_token";
        }
    }

    @RequestMapping(value = "/recuperar_senha", method = RequestMethod.POST)
    @Transactional
    public String atualizarSenha (@RequestParam("code") String token, @RequestParam("US_Senha") String senha) {
        Usuario usuario = tokenResetarSenhaRepository.findByToken(token).getUsuario();
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
        usuarioRepository.save(usuario);
        tokenResetarSenhaRepository.deleteByToken(token);
        return "redirect:/login?att";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}

