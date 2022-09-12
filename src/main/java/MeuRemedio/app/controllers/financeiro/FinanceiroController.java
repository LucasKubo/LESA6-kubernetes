package MeuRemedio.app.controllers.financeiro;


import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.FinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.ServiceConfigurationError;

@Controller
public class FinanceiroController {

    @Autowired
    FinanceiroRepository controleFinanceiro;

    @Autowired
    Usuario usuario;

    @Autowired
    Financeiro financeiro;

    @GetMapping(value = "/remedios/controle_de_gastos")
    public String telaDeGastos(Model model){
        return "TelaDeGastos.html";
    }

    @GetMapping(value = "/remedios/controle_de_gastos/cadastrar")
    public String telaDeGastosCadastro(Model model){
        return "TelaDeGastos.html";
    }


    @PostMapping(value ="/remedios/controle_de_gastos/cadastrar")
    public String cadastrarGasto(@RequestParam("GA_Valor") double valor, @RequestParam("GA_Data") Date data,
                                 @RequestParam("GA_QtdParcelas") long qtdParcela, @RequestParam("FK_RM_ID") List<Remedio> remedio){
        try {
            Financeiro financeiroMedicamento = new Financeiro(remedio, data, valor, qtdParcela);
            controleFinanceiro.save(financeiroMedicamento);
            return "redirect:/controle_de_gastos";

        }catch (ServiceConfigurationError serviceConfigurationError) {
            return "TelaParaCadastraDeNovo";
        }
    }

    @PostMapping(value ="/remedios/controle_de_gastos/deletar/{id}")
    public String deletarGasto (@PathVariable("id") long id){
        if (verificarPorId(id)) {
            controleFinanceiro.deleteById(id);

            return "redirect:/controle_de_gastos";
        }
        return "DeuErroTentarNovamente.html";
    }

    @PostMapping(value ="/remedios/controle_de_gastos/atualizar/{id}")
    public String atualizar(@PathVariable("id") long id, @RequestParam("GA_Valor") double valor, @RequestParam("GA_Data") Date data,
                            @RequestParam("GA_QtdParcelas") long qtdParcela, @RequestParam("FK_RM_ID") List<Remedio> remedio){
        try {
            if (verificarPorId(id)) {
                financeiro.setData(data);
                financeiro.setValor(valor);
                financeiro.setQtdParcela(qtdParcela);
                financeiro.setRemedio(remedio);

                controleFinanceiro.save(financeiro);
            }
            return "redirect:/controle_de_gastos";

        }catch (NullPointerException e){
            return "TelaDeAtualizar.html" + e;
        }
    }

    public boolean verificarPorId (long id ) {
        return controleFinanceiro.existsById(id);
    }
}
