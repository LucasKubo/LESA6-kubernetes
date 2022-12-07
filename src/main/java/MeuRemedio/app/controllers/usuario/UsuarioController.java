package MeuRemedio.app.controllers.usuario;

import MeuRemedio.app.configuration.CustomLogoutSuccessHandler;
import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.*;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.UsuarioService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
    AgendamentoRepository agendamentoRepository;

    @Autowired
    ValidateAuthentication validateAuthentication;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    FinanceiroRepository financeiroRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    SessionRegistry sessionRegistryImpl;

    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Autowired
    DashBoardsRepository dashBoardsRepository;
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
    return "redirect:/usuario/edit/atualizar_usuario?senhaInvalida";
    }

    @RequestMapping(value = "/usuario/edit/deletar_usuario", method = RequestMethod.GET)
    public String deletarUsuario(Model model) {
        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        model.addAttribute("usuario", usuarioLogado);

        return "ExcluirUser";
    }

    @PostMapping(value = "/usuario/edit/deletar_usuario") /*Validar como vai ser a chamada front para o metodo*/
    @Transactional
    public void deletarUsuario (@RequestParam("US_Senha") String senha, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws MessagingException, IOException, ServletException {

        String EmailUsuarioLogado = userSessionService.returnUsernameUsuario();
        Usuario usuarioLogado = usuarioRepository.findByEmail(EmailUsuarioLogado);
        String passUserLogged = usuarioLogado.getPassword();

        boolean validarSenha = false;

        if (BCrypt.checkpw(senha, passUserLogged)) {
            validarSenha = true;
        }
            if (validarSenha) {
                List<Remedio> remedio = remedioRepository.findAllByUsuario(usuarioLogado);
                List<Financeiro> gastos = financeiroRepository.findAllByUsuarioID(usuarioLogado.getId());
                financeiroRepository.deleteAll(gastos);
                remedioRepository.deleteAll(remedio);
                dashBoardsRepository.deleteAllByUsuario(usuarioLogado);
                usuarioNotificationTokenRepository.deleteAllByIdUsuario(usuarioLogado.getId());
                usuarioRepository_2.deleteById(usuarioLogado.getId());
                emailCadastro.emailDeletarCadastro(usuarioLogado);
                request.getSession().invalidate();
                response.sendRedirect("/login?contaExcluida");
            } else {
               response.sendRedirect( "redirect:/usuario/edit/deletar_usuario?senhaInvalida");
            }
    }
    public String TemplateError(){
        return "TemplateError";
    }
}