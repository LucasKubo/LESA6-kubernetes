package MeuRemedio.app.controllers.login;


import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.IntervaloDias;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
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
    IntervaloDiasRepository intervaloDiasRepository;
    @Autowired
    ValidateAuthentication validateAuthentication;
    @RequestMapping(value = "/home")
        public String home(ModelMap model){
        if (!validateAuthentication.auth()) {
            return "Login";
        }

        List<Agendamento> agendamentos = agendamentoRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("agendamento", agendamentos);

        List<IntervaloDias> intervaloDias = intervaloDiasRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("intervaloDias", intervaloDias);

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
