package MeuRemedio.app.controllers;

import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.UsuarioService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class UsuarioController {
    @Autowired
    EnvioEmail emailCadastro;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    ValidateAuthentication validateAuthentication;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UsuarioService usuarioService;

    final String REDIRECT = "redirect:/home";

    @RequestMapping(value = "/cadastro")
    public String telaCadasUsuario() {

        if (!validateAuthentication.auth()) {
            return "cadastros/CadastroUsuario";
        }
        return REDIRECT;
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public String CadastrarUsuario(@RequestParam("US_Nome") String nome, @RequestParam("US_Sobrenome") String sobrenome,
                                   @RequestParam("US_Email") String email, @RequestParam("US_Senha") String senha,
                                   @RequestParam("US_DataNascimento") String dataNascimento, @RequestParam("US_Sexo") String sexo, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        String emailLowerCase = email.toLowerCase();

        boolean usuarioExistente = usuarioService.verificaExistencia(email);

        if (usuarioExistente) {
            return "redirect:/cadastro?emailExistente";
        }

        Usuario usuario = new Usuario(nome, sobrenome, emailLowerCase,
                new BCryptPasswordEncoder().encode(senha), dataNascimento, sexo);

        usuarioRepository.save(usuario);
        // emailCadastro.emailConfirmCadastro(usuarioCadastro);
        usuarioService.cadastrar(usuario, request);

        //emailCadastro.emailConfirmCadastro(usuario);
        return "redirect:/login";
    }

    @RequestMapping(value = "/verificar_cadastro", method = RequestMethod.GET)
    public String verifyUser(@Param("code") String code) {
        if (usuarioService.verificarCodigoDeCadastro(code)) {
            return "SucessoVerificacaoEmail";
        } else {
            return "FalhaVerificacaoEmail";
        }
    }

    @RequestMapping(value = "/atualizar_usuario", method = RequestMethod.GET)
    public String viewAtualizarUsuario(Model model) {
        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        model.addAttribute("usuario", usuarioLogado);

        return "atualizacoes/AtualizarUsuario";
    }

    @RequestMapping(value = "/atualizar_usuario", method = RequestMethod.POST)
    public String atualizarUsuario(@RequestParam("US_Nome") String nome, @RequestParam("US_Sobrenome") String sobrenome,
                                   @RequestParam("US_Senha") String senha, @RequestParam(value = "US_NovaSenha", required = false) String novaSenha,
                                   @RequestParam("US_Sexo") String sexo) {

        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        String passUserLogged = usuarioLogado.getPassword();

        boolean validarSenha = false;

        if (BCrypt.checkpw(senha, passUserLogged)) {
            validarSenha = true;

            if (validarSenha && (novaSenha.isEmpty() || novaSenha == null)) {
                usuarioLogado.setNome(nome);
                usuarioLogado.setSobrenome(sobrenome);
                usuarioLogado.setSexo(sexo);

                usuarioRepository.save(usuarioLogado);

                return REDIRECT;
            }
            if (!(novaSenha == null || novaSenha.isEmpty()) && (validarSenha)) {
                usuarioLogado.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
                usuarioLogado.setNome(nome);
                usuarioLogado.setSobrenome(sobrenome);
                usuarioLogado.setSexo(sexo);

                usuarioRepository.save(usuarioLogado);
                
                return REDIRECT;
            }
        }
        return "TemplateError";
    }
}