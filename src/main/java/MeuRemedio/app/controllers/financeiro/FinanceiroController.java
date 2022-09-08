package MeuRemedio.app.controllers.financeiro;


import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Gasto;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.repository.FinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class FinanceiroController {

    @Autowired
    FinanceiroRepository controleFinanceiro;

    @Autowired
    Usuario usuario;

    @GetMapping(value = "/remedios/controle_de_gastos")
    public String telaDeGastos(){
        return "TelaDeGastos.html";
    }

    @PostMapping(value ="/remedios/controle_de_gastos")
    public String cadastrarGasto(@RequestParam("GA_Valor") double valor, @RequestParam("GA_Data") Date data,
                                 @RequestParam("GA_QtdParcelas") long qtdParcela, @RequestParam("FK_RM_ID") List<Remedio> remedio){

        Gasto gastoMedicamento = new Gasto(remedio, data, valor, qtdParcela);
        controleFinanceiro.save(gastoMedicamento);

        return "TelaDeGastos.html";
    }

    @PostMapping(value ="/remedios/controle_de_gastos/deletar_gato/{id}")
    public String deletarGasto(long id){
        controleFinanceiro.deleteById(id);

        return "algumaTela.html";
    }
}
