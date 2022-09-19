package MeuRemedio.app.controllers.login;


import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.Authentication;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    AgendamentoRepository agendamentoRepository;
    @Autowired
    ValidateAuthentication validateAuthentication;
    @RequestMapping(value = "/home")
        public String home(ModelMap model){
        if (!validateAuthentication.auth()) {
            return "Login";
        }

        List<Agendamento> agendamentos = agendamentoRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("agendamento", agendamentos);
        return "Home";
        }

    @RequestMapping(value = "/")
    public String Index(){
        if (validateAuthentication.auth() != true){
            return "Index";
        }
        return "redirect:/home";
    }
}
