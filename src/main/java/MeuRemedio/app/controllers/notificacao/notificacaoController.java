package MeuRemedio.app.controllers.notificacao;

import MeuRemedio.app.models.usuarios.UsuarioNotificationToken;
import MeuRemedio.app.models.usuarios.UsuarioNotificationTokenId;
import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
public class notificacaoController {
    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Autowired
    UserSessionService userSessionService;


    @RequestMapping(value = "/getNotificationToken", method = RequestMethod.POST)
    public String receberTokenDeNotificacao(@RequestBody String token, HttpServletResponse response){
        System.out.println("Recebi o token: " + token);
        UsuarioNotificationToken usuarioNotificationToken = new UsuarioNotificationToken();
        UsuarioNotificationTokenId usuarioNotificationTokenId = new UsuarioNotificationTokenId();
        usuarioNotificationTokenId.setToken(token);
        usuarioNotificationTokenId.setUsuario(userSessionService.returnIdUsuarioLogado());
        usuarioNotificationToken.setId(usuarioNotificationTokenId);
        Cookie cookie = new Cookie("tokenNotification", token);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(cookie);
        var tokenSalvo = usuarioNotificationTokenRepository.findByIdToken(token);
        if (Objects.nonNull(tokenSalvo) && !usuarioNotificationToken.getId().getUsuario().equals(tokenSalvo.getId().getUsuario())){
            usuarioNotificationTokenRepository.delete(tokenSalvo);
        }
        usuarioNotificationTokenRepository.save(usuarioNotificationToken);
        return "Home";
    }
}
