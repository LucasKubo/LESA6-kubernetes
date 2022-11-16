package MeuRemedio.app.controllers.login;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioNotificationTokenRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    ValidateAuthentication validateAuthentication;


    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request) {
         if (validateAuthentication.auth() != true){
             Cookie[] cookies = request.getCookies();
             if(cookies!=null) {
                 for (Cookie cookie : cookies) {
                     if(cookie.getName().equals("tokenNotification")) {
                         var token = usuarioNotificationTokenRepository.findByIdToken(cookie.getValue());
                         usuarioNotificationTokenRepository.delete(token);
                         cookie.setMaxAge(0);
                     }
                 }
             }
            return "Login";
        }

        return "redirect:/home";
    }
    @RequestMapping(value = "/logout")
    public String logout() {
        return "Login";
    }


}
