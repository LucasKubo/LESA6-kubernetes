package MeuRemedio.app.controllers.financeiro;


import MeuRemedio.app.models.dash.Dash;
import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.DashBoardsRepository;
import MeuRemedio.app.repository.FinanceiroRepository;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ServiceConfigurationError;

@Controller
public class FinanceiroController {

    @Autowired
    FinanceiroRepository controleFinanceiro;

    @Autowired
    RemedioRepository remedioRepository;

    @Autowired
    DashBoardsRepository dashBoardsRepository;

    @Autowired
    UserSessionService userSessionService;

    @GetMapping(value = "/remedios/controle_de_gastos")
    public String telaDeGastos (Model model){
        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        Iterable<Financeiro> financeiro = controleFinanceiro.findAllByUsuarioID(usuarioID.getId());
        model.addAttribute("financeiro", financeiro);

        return "listas/ListarGasto";
    }

    @GetMapping(value = "/remedios/controle_de_gastos/listar")
    public String telaDeGastosB (Model model){
        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        Iterable<Financeiro> financeiro = controleFinanceiro.findAllByUsuarioID(usuarioID.getId());
        model.addAttribute("financeiro", financeiro);

        return "listas/ListarGastoB";
    }

    @GetMapping(value = "/remedios/controle_de_gastos/cadastrar")
    public String telaDeGastosCadastro(Model model){

        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        List <Remedio> remedio = remedioRepository.findAllByUsuario(usuarioID);
        Collections.sort(remedio, Remedio::compareTo);
        model.addAttribute("remedio", remedio);
        return "cadastros/CadastroGasto";
    }

    @PostMapping(value ="/remedios/controle_de_gastos/cadastrar")
    public String cadastrarGasto (@RequestParam("GA_Valor") double valor, @RequestParam("GA_Data") String data,
                                  @RequestParam("GA_Parcela") long qtdParcela, @RequestParam(value = "AG_Remedios", required = false) List<Remedio> remedio,
                                  @RequestParam(value = "RM_Nome", required = false) String RM_Nome){
        try {
            Usuario usuarioID = new Usuario();
            usuarioID.setId(userSessionService.returnIdUsuarioLogado());

            Financeiro financeiroMedicamento = new Financeiro(remedio, data, valor, qtdParcela, usuarioID.getId());
            Dash dash = new Dash(usuarioID, valor, data);

            dashBoardsRepository.save(dash);
            controleFinanceiro.save(financeiroMedicamento);

            return "redirect:/remedios/controle_de_gastos";

        }catch (ServiceConfigurationError serviceConfigurationError) {
            return templateError();
        }
    }

    @RequestMapping(value ="/remedios/controle_de_gastos/deletar/{id}")
    public String deletarGasto (@PathVariable("id") long id){
        if (verificarPorId(id)) {
            controleFinanceiro.deleteById(id);

            return "redirect:/remedios/controle_de_gastos/listar";
        }
        return templateError();
    }

    @RequestMapping(value ="/remedios/controle_de_gastos/atualizar/{id}",  method = RequestMethod.GET)
    public String atualizarGasto (@PathVariable("id") long id, Model model) {
        if (!verificarPorId(id)) {
            return templateError();
        } else {
            Financeiro financeiro = controleFinanceiro.findById (id);
            model.addAttribute("financeiro", financeiro);

            Usuario usuarioID = new Usuario();
            usuarioID.setId(userSessionService.returnIdUsuarioLogado());

            List <Remedio> remedio = remedioRepository.findAllByUsuario(usuarioID);

            if (!remedio.isEmpty()){
                Collections.sort(remedio, Remedio::compareTo);
            }
            model.addAttribute("remedio", remedio);
            return "atualizacoes/AtualizarGasto";
        }
    }

    @PostMapping(value ="/remedios/controle_de_gastos/atualizar/{id}")
    public String atualizar (@PathVariable("id") long id, @RequestParam("GA_Valor") double valor, @RequestParam("GA_Data") String data,
                            @RequestParam("GA_Parcela") long qtdParcela, @RequestParam("FK_RM_ID") List<Remedio> remedio){
        try {
            Financeiro financeiro = controleFinanceiro.findById(id);

            if (Objects.nonNull(financeiro)) {
                financeiro.setData(data);
                financeiro.setValor(valor);
                financeiro.setQtdParcela(qtdParcela);
                financeiro.setRemedio(remedio);
                controleFinanceiro.save(financeiro);
            }
            return "redirect:/remedios/controle_de_gastos/listar";

        }catch (NullPointerException e){
            return templateError() + e;
        }
    }

    @GetMapping(value = "/gasto/remedio/{id}")
    public String gastoRemedioDireto(@PathVariable("id") long id, Model model){
        Remedio remedio = remedioRepository.findById(id);

        if (Objects.isNull(remedio)){
            templateError();
        }
        model.addAttribute("remedioD", remedio);

        return "cadastros/CadastroGastoDireto";
    }

    @PostMapping(value = "/gasto/remedio/{id}")
    public String cadastroGastoRemedioDireto(@PathVariable("id") long idRemedio, @RequestParam("GA_Valor") double valor,
                                             @RequestParam("GA_Data") String data, @RequestParam("GA_Parcela") long qtdParcela){
        Usuario usuarioID = new Usuario();
        usuarioID.setId(userSessionService.returnIdUsuarioLogado());

        Remedio remedioParaControle = remedioRepository.findById(idRemedio);

        if (Objects.isNull(remedioParaControle)){
            templateError();
        }

        Financeiro financeiroMedicamento = new Financeiro(Collections.singletonList(remedioParaControle), data, valor, qtdParcela, usuarioID.getId());
        Dash dash = new Dash(usuarioID, valor, data);

        dashBoardsRepository.save(dash);
        controleFinanceiro.save(financeiroMedicamento);

        return "redirect:/remedios/controle_de_gastos/listar";
    }

    public boolean verificarPorId (long id ) {
        return controleFinanceiro.existsById(id);
    }

    public String templateError(){
        return "TemplateError";
    }
}