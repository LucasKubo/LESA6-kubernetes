package MeuRemedio.app.controllers.usuario;

import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.repository.UsuarioRepository_2;
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
    UsuarioRepository_2 usuarioRepository_2;

    @Autowired
    RemedioRepository remedioRepository;

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
        usuarioService.cadastrar(usuario, request);

        return "validacoes/AvisoVerificacaoEmail";
    }

    @RequestMapping(value = "/verificar_cadastro", method = RequestMethod.GET)
    public String verifyUser(@Param("code") String code) {
        if (usuarioService.verificarCodigoDeCadastro(code)) {
            return "validacoes/SucessoVerificacaoEmail";
            //TODO enviar confirmação de cadastro por email
        } else {
            return "validacoes/FalhaVerificacaoEmail";
        }
    }

    @RequestMapping(value = "/usuario/edit/atualizar_usuario", method = RequestMethod.GET)
    public String viewAtualizarUsuario(Model model) {
        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        model.addAttribute("usuario", usuarioLogado);

        return "atualizacoes/AtualizarUsuario";
    }

    @RequestMapping(value = "/usuario/edit/atualizar_usuario", method = RequestMethod.POST)
    public String atualizarUsuario(@RequestParam("US_Nome") String nome, @RequestParam("US_Sobrenome") String sobrenome,
                                   @RequestParam("US_Senha") String senha, @RequestParam(value = "US_NovaSenha", required = false) String novaSenha,
                                   @RequestParam("US_Sexo") String sexo) {

        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        String passUserLogged = usuarioLogado.getPassword();

        boolean validarSenha ;

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
        return TemplateError();
    }

    @PostMapping(value = "/usuario/edit/deletar_usuario/{id}") /*Validar como vai ser a chamada front para o metodo*/
    public String deletarUsuario (@PathVariable("id") long id ,@RequestParam("US_Senha") String senha ) {

        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        String passUserLogged = usuarioLogado.getPassword();

        boolean validarSenha;

        if (BCrypt.checkpw(senha, passUserLogged)) {
            validarSenha = true;

            if (validarSenha) {
                Remedio remedio = remedioRepository.findByUsuario(usuarioLogado);
                remedioRepository.delete(remedio);
                usuarioRepository_2.deleteById(id);
            }
            return "redirect:/logout";
        }
        return TemplateError();
    }

    public String TemplateError(){
        return "TemplateError";
    }
}