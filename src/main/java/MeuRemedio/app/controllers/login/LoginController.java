package MeuRemedio.app.controllers.login;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    ValidateAuthentication validateAuthentication;


    @Autowired
    UsuarioRepository usuarioRepository;


    @RequestMapping(value = "/login")
    public String login() {
         if (validateAuthentication.auth() != true){
            return "Login";
        }

        return "redirect:/home";
    }
    @RequestMapping(value = "/logout")
    public String logout() {
        return "Login";
    }


}
