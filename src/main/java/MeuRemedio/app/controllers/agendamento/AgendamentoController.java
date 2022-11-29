package MeuRemedio.app.controllers.agendamento;

import MeuRemedio.app.controllers.remedio.RemedioController;
import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.IntervaloDias;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.AgendamentosHorariosRepository;
import MeuRemedio.app.repository.IntervaloDiasRepository;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.service.CalculaHorariosNotificacao;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AgendamentoController {
    @Autowired
    ValidateAuthentication validateAuthentication;

    @Autowired
    AgendamentoRepository agendamentoRepository;

    @Autowired
    IntervaloDiasRepository intervaloDiasRepository;

    @Autowired
    RemedioRepository remedioRepository;

    @Autowired
    RemedioController remedioController;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    AgendamentosHorariosRepository agendamentosHorariosRepository;

    private final CalculaHorariosNotificacao calculaHorariosNotificacao;

    final String REDIRECT = "redirect:/agendamentos";


    @RequestMapping(value = "/agendamentos", method = RequestMethod.GET)
    public String viewAgendamentos(ModelMap model) {
        if (!validateAuthentication.auth()) {
            return "Login";
        }

        List<Agendamento> agendamentos = agendamentoRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("agendamento", agendamentos);

        List<IntervaloDias> intervaloDias = new ArrayList<>();
        for (Agendamento agendamento : agendamentos) {
            Optional<IntervaloDias> intervalo = intervaloDiasRepository.findById(agendamento.getId());
            if (intervalo.isPresent()) {
                intervaloDias.add(intervalo.get());
            }
        }
        model.addAttribute("intervaloDias", intervaloDias);
        return "listas/ListaAgendamentos";
    }

    @RequestMapping(value = "/cadastro_agendamentos", method = RequestMethod.GET)
    public String viewCadastroAgendamento(ModelMap model) {
        if (!validateAuthentication.auth()) {
            return "Login";
        }

        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());
        List<Remedio> remedio = remedioRepository.findAllByUsuario(usuarioID);
        if (!remedio.isEmpty()) {
            Collections.sort(remedio, Remedio::compareTo);
        }
        model.addAttribute("remedio", remedio);

        return "cadastros/CadastroAgendamento";
    }

    @RequestMapping(value = "/agendamentos/listar", method = RequestMethod.GET)
    public String viewAgendamentosB(ModelMap model) {
        if (!validateAuthentication.auth()) {
            return "Login";
        }

        List<Agendamento> agendamentos = agendamentoRepository.findAllByUsuarioID(userSessionService.returnIdUsuarioLogado());
        model.addAttribute("agendamento", agendamentos);

        List<IntervaloDias> intervaloDias = new ArrayList<>();
        for (Agendamento agendamento : agendamentos) {
            Optional<IntervaloDias> intervalo = intervaloDiasRepository.findById(agendamento.getId());
            if (intervalo.isPresent()) {
                intervaloDias.add(intervalo.get());
            }
        }
        model.addAttribute("intervaloDias", intervaloDias);

        return "listas/ListarAg";
    }

    @RequestMapping(value = "/cadastro_agendamentos", method = RequestMethod.POST)
    public String cadastrarAgendamento (@RequestParam("AG_Remedios") List<Remedio> remedios,
                                        @RequestParam("AG_DataInicio") String AG_DataInicio,
                                        @RequestParam("AG_HoraInicio") String AG_horaInicio,
                                        @RequestParam("AG_DataFinal") String AG_DataFinal,
                                        @RequestParam("AG_Periodicidade") long AG_Periodicidade,
                                        @RequestParam(value = "intervaloDias", required = false) Long intervaloDias) {

        Agendamento id;
        if (intervaloDias != null) {
            IntervaloDias intervalo = new IntervaloDias(AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade,
                    remedios, userSessionService.returnIdUsuarioLogado(), intervaloDias);

            id = intervaloDiasRepository.save(intervalo);
        } else {
            Agendamento agendamento = new Agendamento(AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade,
                    remedios, userSessionService.returnIdUsuarioLogado());

            id = agendamentoRepository.save(agendamento);
        }
        salvarHorariosAgendamentos(id);

        return "redirect:/agendamentos/listar";
    }


    public void salvarHorariosAgendamentos(Agendamento id) {
        calculaHorariosNotificacao.calcular(id);
    }

    @RequestMapping(value = "/deletar_agendamento/{id}")
    public String deletarAgendamento(@PathVariable("id") long id, HttpServletRequest request) {
        Agendamento agendamento = agendamentoRepository.findById(id);
        String url = request.getHeader("referer");
        agendamentoRepository.delete(agendamento);
        return "redirect:" + url;
    }

    @RequestMapping(value = "/agendamento/remedio/{id}", method = RequestMethod.GET)
    public String cadastrarAgendamentoRemedio(@PathVariable("id") long id, Model model) {
        Remedio remedio = remedioRepository.findById(id);

        if (Objects.isNull(remedio)) {
            templateError();
        }
        model.addAttribute("remedioD", remedio);
        return "cadastros/CadastroAgendamentoDireto";
    }

    @RequestMapping(value = "/agendamento/remedio/{id}", method = RequestMethod.POST)
    public String cadastrarAgendamentoRemedio(@PathVariable("id") long idRemedio,
                                              @RequestParam("AG_DataInicio") String AG_DataInicio,
                                              @RequestParam("AG_HoraInicio") String AG_horaInicio,
                                              @RequestParam("AG_DataFinal") String AG_DataFinal,
                                              @RequestParam("AG_Periodicidade") long AG_Periodicidade,
                                              @RequestParam(value = "intervaloDias", required = false) Long intervaloDias) {

        Remedio remedio = remedioRepository.findById(idRemedio);
        Agendamento id;

        if (intervaloDias != null) {
            IntervaloDias intervalo = new IntervaloDias(AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade,
                    Collections.singletonList(remedio), userSessionService.returnIdUsuarioLogado(), intervaloDias);
            id = intervaloDiasRepository.save(intervalo);
        } else {
            Agendamento agendamento = new Agendamento(AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade,
                    Collections.singletonList(remedio), userSessionService.returnIdUsuarioLogado());

            id = agendamentoRepository.save(agendamento);
        }
        salvarHorariosAgendamentos(id);

        return "redirect:/agendamentos/listar";
    }

    @RequestMapping(value = "/atualizar_agendamento/{id}", method = RequestMethod.GET)
    public String atualizarRemedio(@PathVariable("id") long id, Model model) {
        if (!verificarPorId(id)) {
            return templateError();

        } else {
            Usuario usuarioID = new Usuario();
            usuarioID.setId(userSessionService.returnIdUsuarioLogado());

            List<Remedio> remedio = remedioRepository.findAllByUsuario(usuarioID);
            if (!remedio.isEmpty()) {
                Collections.sort(remedio, Remedio::compareTo);
            }
            model.addAttribute("remedio", remedio);

            Agendamento agendamento = agendamentoRepository.findById(id);
            model.addAttribute("agendamento", agendamento);

            Optional<IntervaloDias> intervaloDias = intervaloDiasRepository.findById(id);
            if (intervaloDias.isPresent()) {
                model.addAttribute("intervaloDias", intervaloDias.get());
            }
            return "atualizacoes/AtualizarAgendamento";
        }
    }

    @RequestMapping(value = "/atualizar_agendamento/{id}", method = RequestMethod.POST)
    @Transactional
    public String atualizarDadosAgendamento(@PathVariable("id") long id,
                                            @RequestParam("AG_Remedios") List<Remedio> remedios,
                                            @RequestParam("AG_DataInicio") String AG_DataInicio,
                                            @RequestParam("AG_HoraInicio") String AG_horaInicio,
                                            @RequestParam("AG_DataFinal") String AG_DataFinal,
                                            @RequestParam("AG_Periodicidade") long AG_Periodicidade,
                                            @RequestParam(value = "intervaloDias", required = false) Long intervaloDias) {
        if (!verificarPorId(id)) {
            return remedioController.
                    templateError();
        }

        Optional<IntervaloDias> intervaloExiste = intervaloDiasRepository.findById(id);
        if (intervaloExiste.isPresent() && intervaloDias == null) {
            Agendamento agendamento = new Agendamento();

            agendamento.setRemedio(remedios);
            agendamento.setDataInicio(AG_DataInicio);
            agendamento.setHoraInicio(AG_horaInicio);
            agendamento.setDataFinal(AG_DataFinal);
            agendamento.setPeriodicidade(AG_Periodicidade);
            agendamento.setUsuarioID(userSessionService.returnIdUsuarioLogado());
            intervaloDiasRepository.deleteById(id);
            Agendamento ag = agendamentoRepository.save(agendamento);
            salvarHorariosAgendamentos(ag);

        } else if (intervaloExiste.isPresent() && intervaloDias != null) {
            IntervaloDias atualizarIntervalo = intervaloExiste.get();

            atualizarIntervalo.setId(id);
            atualizarIntervalo.setRemedio(remedios);
            atualizarIntervalo.setDataInicio(AG_DataInicio);
            atualizarIntervalo.setHoraInicio(AG_horaInicio);
            atualizarIntervalo.setDataFinal(AG_DataFinal);
            atualizarIntervalo.setPeriodicidade(AG_Periodicidade);
            atualizarIntervalo.setIntervaloDias(intervaloDias);

            agendamentosHorariosRepository.deleteAllByIdAgendamento(id);
            IntervaloDias it = intervaloDiasRepository.save(atualizarIntervalo);
            salvarHorariosAgendamentos(it);
        } else if (intervaloDias == null) {
            Agendamento agendamento = agendamentoRepository.findById(id);

            agendamento.setRemedio(remedios);
            agendamento.setDataInicio(AG_DataInicio);
            agendamento.setHoraInicio(AG_horaInicio);
            agendamento.setDataFinal(AG_DataFinal);
            agendamento.setPeriodicidade(AG_Periodicidade);

            agendamentosHorariosRepository.deleteAllByIdAgendamento(id);
            Agendamento ag = agendamentoRepository.save(agendamento);
            salvarHorariosAgendamentos(ag);

        } else {
            IntervaloDias adicionarIntervalo = new IntervaloDias(AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade,
                    remedios, userSessionService.returnIdUsuarioLogado(), intervaloDias);

            adicionarIntervalo.setId(id);
            agendamentoRepository.deleteById(id);
            agendamentosHorariosRepository.deleteAllByIdAgendamento(id);
            IntervaloDias it = intervaloDiasRepository.save(adicionarIntervalo);
            salvarHorariosAgendamentos(it);
        }
        return REDIRECT;
    }

    public boolean verificarPorId(long id) {
        return agendamentoRepository.existsById(id);
    }

    public String templateError() {
        return "TemplateError";
    }
}