package MeuRemedio.app.controllers.notificacao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class notificacaoController {
    @RequestMapping(value = "/getNotificationToken", method = RequestMethod.POST)
    public String receberTokenDeNotificacao(@RequestBody String token){
        System.out.println("Recebi o token: " + token);
        return "Home";
    }
}
