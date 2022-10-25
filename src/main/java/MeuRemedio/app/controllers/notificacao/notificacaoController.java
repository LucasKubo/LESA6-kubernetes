package MeuRemedio.app.controllers.notificacao;

import MeuRemedio.app.models.usuarios.UsuarioNotificationToken;
import MeuRemedio.app.models.usuarios.UsuarioNotificationTokenId;
import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class notificacaoController {
    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;
    @Autowired
    UserSessionService userSessionService;


    @RequestMapping(value = "/getNotificationToken", method = RequestMethod.POST)
    public String receberTokenDeNotificacao(@RequestBody String token){
        System.out.println("Recebi o token: " + token);
        UsuarioNotificationToken usuarioNotificationToken = new UsuarioNotificationToken();
        UsuarioNotificationTokenId usuarioNotificationTokenId = new UsuarioNotificationTokenId();
        usuarioNotificationTokenId.setToken(token);
        usuarioNotificationTokenId.setUsuario(userSessionService.returnIdUsuarioLogado());
        usuarioNotificationToken.setId(usuarioNotificationTokenId);
        usuarioNotificationTokenRepository.save(usuarioNotificationToken);
        return "Home";
    }
}
