package MeuRemedio.app.controllers.login;


import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import MeuRemedio.app.models.agendamentos.IntervaloDias;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.*;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;


@Controller
public class IndexController {
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    AgendamentoRepository agendamentoRepository;

    @Autowired
    AgendamentosHorariosRepository agendamentosHorariosRepository;
    @Autowired
    IntervaloDiasRepository intervaloDiasRepository;
    @Autowired
    ValidateAuthentication validateAuthentication;

    @Autowired
    FinanceiroRepository financeiroRepository;

    final String ZONEID = "America/Sao_Paulo";

    @RequestMapping(value = "/home")
        public String home(ModelMap model, HttpServletRequest request){
        if (Objects.isNull(userSessionService.returnIdUsuarioLogado())){
            HttpSession session= request.getSession();
            SecurityContextHolder.clearContext();
            session.invalidate();
        }
        if (!validateAuthentication.auth()) {
            return "Login";
        }

        List<Agendamento> agendamentos = agendamentoRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("agendamento", agendamentos);

        List<IntervaloDias> intervaloDias = intervaloDiasRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("intervaloDias", intervaloDias);

        List<AgendamentosHorarios> horarios = agendamentosHorariosRepository.selecionarHorarios(userSessionService.returnIdUsuarioLogado(), instanteAgora);
        model.addAttribute("horarios", horarios);

        List <Financeiro> financeiro = financeiroRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());

        List<Double> gastos = new ArrayList<>();

        for (int i = 0; i < 12; i++){
            gastos.add(i, 0.0);
        }

        for (int i = 0; i < financeiro.size(); i++){
            var dataString = financeiro.get(i).getData();
            LocalDate data = LocalDate.parse(dataString);
            var mes = data.getMonth().ordinal();
            Double valorPorParcela = financeiro.get(i).getValor() / financeiro.get(i).getQtdParcela();
            for (int j = 0; j < financeiro.get(i).getQtdParcela(); j++){
                var mesParcela = mes + j;
                if (mesParcela < 12)
                    gastos.set(mesParcela, gastos.get(mesParcela) + valorPorParcela);
            }
        }
        model.addAttribute("gastos", gastos);

        return "Home";
        }

    @RequestMapping(value = "/")
    public String Index(){
        if (validateAuthentication.auth() != true){
            return "Index";
        }
        return "redirect:/home";
    }
    LocalDateTime instanteAgora = LocalDateTime.now(ZoneId.of(ZONEID));
}
