package MeuRemedio.app.security.remedio;


import MeuRemedio.app.controllers.EnvioEmail;
import MeuRemedio.app.controllers.agendamento.AgendamentoController;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.AgendamentoRepository;
import MeuRemedio.app.repository.FinanceiroRepository;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.repository.UsuarioRepository;
import MeuRemedio.app.service.UserSessionService;
import MeuRemedio.app.service.utils.ValidateAuthentication;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class RemedioController {

    private String username;


    @Autowired
    EnvioEmail emailController;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RemedioRepository remedioRepository;

    @Autowired
    AgendamentoController agendamentoController;
    @Autowired
    ValidateAuthentication validateAuthentication;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    FinanceiroRepository controleFinanceiro;

    @Autowired
    AgendamentoRepository agendamentoRepository;

    final String REDIRECT="redirect:/remedios";

    @RequestMapping(value = "/remedios_cadastro")
    public String telaCadastroRemedio(ModelMap model) {
        if (!validateAuthentication.auth()) {
            return "Login";
        }
        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        List <Remedio> remedio  = remedioRepository.findAllByUsuario(usuarioID);
        model.addAttribute("remedio", remedio);


        return "cadastros/CadastroRemedios";
    }

    @RequestMapping(value = "/remedios_cadastro", method = RequestMethod.POST)
    public String CadastroRemedio(@RequestParam("RM_Nome") String RM_Nome, @RequestParam("RM_Dosagem") String RM_Dosagem,
                                  @RequestParam("RM_UnidadeDosagem") String RM_UnidadeDosagem, @RequestParam("RM_RetiradoSus") String RM_RetiradoSus,
                                  @RequestParam(value = "exampleCheck1", required = false) Boolean check,
                                  @RequestParam(value = "AG_DataInicio", required = false)  String AG_DataInicio,
                                  @RequestParam(value = "AG_HoraInicio", required = false) String AG_horaInicio,
                                  @RequestParam(value = "AG_DataFinal", required = false)  String AG_DataFinal ,
                                  @RequestParam(value = "AG_Periodicidade", required = false) long AG_Periodicidade,
                                  @RequestParam(value = "intervaloDias", required = false) Long intervaloDias /*,
                                  @RequestParam(defaultValue = "2022/01/01", value = "GA_Data", required = false) String GA_Data,
                                  @RequestParam(value = "GA_Valor", required = false) double GA_Valor,
                                  @RequestParam(value = "GA_Parcela", required = false) long GA_Parcela */) throws Exception {

        boolean auxRetiradoSUS;

        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        auxRetiradoSUS = RM_RetiradoSus.equals("Sim");
        Remedio remedio = new Remedio(RM_Nome, RM_Dosagem, RM_UnidadeDosagem, auxRetiradoSUS, usuarioID);
        Remedio rem = remedioRepository.save(remedio);

/*        Financeiro financeiro = new Financeiro (GA_Data, GA_Valor, GA_Parcela, Collections.singletonList(rem), usuarioID.getId());
        controleFinanceiro.save(financeiro);
*/
        if (Objects.nonNull(check) && check) {
            cadastrarAgendamento(Collections.singletonList(remedio), AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade, intervaloDias);
        }

        Usuario usuario = usuarioRepository.findByEmail(userSessionService.returnUsernameUsuario());
        emailController.emailCadastroRemedio(usuario, remedio);

        return REDIRECT;
    }

    public void cadastrarAgendamento (List<Remedio> remedios, String AG_DataInicio, String AG_horaInicio,
                                     String AG_DataFinal, long AG_Periodicidade, Long intervaloDias) throws Exception {

        if(Objects.isNull(remedios) || Objects.isNull(AG_DataInicio) || Objects.isNull(AG_horaInicio) ||
                Objects.isNull(AG_DataFinal)){
            throw new Exception();
        }
        agendamentoController.cadastrarAgendamento(remedios, AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade, intervaloDias);
    }

    @RequestMapping(value = "/remedios")
    public String listaRemedios (ModelMap model) {

        if (!validateAuthentication.auth()) {
            return "Login";
        }
        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        List <Remedio> remedio = remedioRepository.findAllByUsuario(usuarioID);
        model.addAttribute("remedio", remedio);

        return "listas/ListaRemedios";
    }

    @RequestMapping(value = "/deletar_remedio/{id}")
    public String deletarRemedio (@PathVariable("id") long id) {
        if (verificarPorId(id)) {
            var remedio = remedioRepository.findById(id);
            //controleFinanceiro.deleteByRemedio(id);
            var agendamentos = agendamentoRepository.findAllByRemedio(remedio);
            for (int i = 0; i < agendamentos.size(); i++){
                if (agendamentos.get(i).getRemedio().size() == 1) {
                    agendamentoRepository.deleteById(agendamentos.get(i).getId());
                }
            }
            remedioRepository.deleteById(id);
            return REDIRECT;
        }
        return templateError();
    }

    @RequestMapping(value = "/atualizar_remedio/{id}", method = RequestMethod.GET)
    public String atualizarRemedio(@PathVariable("id") long id, Model model) {
        if (!verificarPorId(id)) {
            return templateError();
        } else {
            Remedio remedio = remedioRepository.findById(id);
            model.addAttribute("remedio", remedio);
            return "atualizacoes/AtualizarRemedios";
        }
    }

    @RequestMapping(value = "/atualizar_remedio/{id}", method = RequestMethod.POST)
    public String atualizarDadosRemedio (@PathVariable("id") long id,  @RequestParam("RM_Nome") String RM_Nome, @RequestParam("RM_Dosagem") String RM_Dosagem,
                                       @RequestParam("RM_UnidadeDosagem") String RM_UnidadeDosagem, @RequestParam("RM_RetiradoSus") String RM_RetiradoSus)  {
        boolean auxRetiradoSUS;

        if (RM_RetiradoSus.equals("Sim")) {
            auxRetiradoSUS = true;
        } else {
            auxRetiradoSUS = false;
        }

        if (!verificarPorId(id)) {
             return templateError();
        } else {
            Remedio remedio = remedioRepository.findById(id);

            remedio.setRM_Nome(RM_Nome);
            remedio.setRM_Dosagem(RM_Dosagem);
            remedio.setRM_UnidadeDosagem(RM_UnidadeDosagem);
            remedio.setRM_RetiradoSus(auxRetiradoSUS);

            remedioRepository.save(remedio);
            return REDIRECT;
        }
    }

    //função responsável por achar um id dentro do banco. Retorna true se encontrar
    public boolean verificarPorId (long id ) {
        return remedioRepository.existsById(id);
    }

    //Essa função deve retornar uma tela customizada de erro.
    public String templateError(){
        return "TemplateError";
    }
}