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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    UsuarioNotificationTokenRepository usuarioNotificationTokenRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RemedioRepository remedioRepository;

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

        Usuario usuario = usuarioRepository.findById(userSessionService.returnIdUsuarioLogado());
        List<Remedio> remedios = remedioRepository.findAllByUsuario(usuario);
        model.addAttribute("remedios", remedios);

        List<Agendamento> agendamentos = agendamentoRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("agendamento", agendamentos);

        List<IntervaloDias> intervaloDias = intervaloDiasRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("intervaloDias", intervaloDias);

        List<AgendamentosHorarios> horariosAG = agendamentosHorariosRepository.selecionarHorarios(userSessionService.returnIdUsuarioLogado(), instanteAgora);
        List<AgendamentosHorarios> horarios = new ArrayList<>();
        for (AgendamentosHorarios agendamentosHorarios : horariosAG) {
            if (agendamentosHorarios.getId().getHoraDataNotificacao().getDayOfMonth() == instanteAgora.getDayOfMonth()) {
                horarios.add(agendamentosHorarios);
            }
        }

        model.addAttribute("horarios", horarios);

        List <Financeiro> financeiro = financeiroRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());

        List<Double> gastos = new ArrayList<>();
        List<Double> gastosAnoSeguinte = new ArrayList<>();
        List<Double> gastosAnoAnterior = new ArrayList<>();

        int anoAtual = LocalDate.now().getYear();

        for (int i = 0; i < 12; i++){
            gastos.add(i, 0.0);
            gastosAnoSeguinte.add(i, 0.0);
            gastosAnoAnterior.add(i, 0.0);
        }

        for (int i = 0; i < financeiro.size(); i++){
            var dataString = financeiro.get(i).getData();
            LocalDate data = LocalDate.parse(dataString);
            var mes = data.getMonth().ordinal();
            Double valorPorParcela = financeiro.get(i).getValor() / financeiro.get(i).getQtdParcela();

            //Adicionar valores na lista se o ano for igual ao atual
            if (data.getYear() == anoAtual){
                for (int j = 0; j < financeiro.get(i).getQtdParcela(); j++){
                    var mesParcela = mes + j;
                    if (mesParcela < 12) {
                        gastos.set(mesParcela, gastos.get(mesParcela) + valorPorParcela);
                    } else {
                        gastosAnoSeguinte.set(mesParcela - 12, gastosAnoSeguinte.get(mesParcela - 12) + valorPorParcela);
                    }
            }
        }

            //Adicionar valores na lista se o ano for anterior ao atual
            else if (data.getYear() == anoAtual - 1){
                for (int j = 0; j < financeiro.get(i).getQtdParcela(); j++){
                    var mesParcela = mes + j;
                    if (mesParcela < 12) {
                        gastosAnoAnterior.set(mesParcela, gastosAnoAnterior.get(mesParcela) + valorPorParcela);
                    } else {
                        gastos.set(mesParcela - 12, gastos.get(mesParcela - 12) + valorPorParcela);
                    }
                }
            }

            //Adicionar valores na lista se o ano for seguinte ao atual
            else if (data.getYear() == anoAtual + 1){
                for (int j = 0; j < financeiro.get(i).getQtdParcela(); j++){
                    var mesParcela = mes + j;
                    if (mesParcela < 12) {
                        gastosAnoSeguinte.set(mesParcela, gastosAnoSeguinte.get(mesParcela) + valorPorParcela);
                    }
                }
            }

            else if (data.getYear() == anoAtual - 2){
                for (int j = 0; j < financeiro.get(i).getQtdParcela(); j++) {
                    var mesParcela = mes + j;
                    if (mesParcela > 11) {
                        gastosAnoAnterior.set(mesParcela - 12, gastosAnoAnterior.get(mesParcela - 12) + valorPorParcela);
                    }
                }
            }
        }

        model.addAttribute("gastos", gastos);
        model.addAttribute("gastosAnoSeguinte", gastosAnoSeguinte);
        model.addAttribute("gastosAnoAnterior", gastosAnoAnterior);

        return "Home";
        }

    @RequestMapping(value = "/")
    public String Index(HttpServletRequest request){
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
            return "Index";
        }
        return "redirect:/home";
    }
    LocalDateTime instanteAgora = LocalDateTime.now(ZoneId.of(ZONEID));
}
