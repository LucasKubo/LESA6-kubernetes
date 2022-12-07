package MeuRemedio.app.controllers.financeiro;

import MeuRemedio.app.mocks.FinanceiroMock;
import MeuRemedio.app.mocks.RemedioMock;
import MeuRemedio.app.repository.DashBoardsRepository;
import MeuRemedio.app.repository.FinanceiroRepository;
import MeuRemedio.app.repository.RemedioRepository;
import MeuRemedio.app.service.FinanceiroService;
import MeuRemedio.app.service.UserSessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.ServiceConfigurationError;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FinanceiroControllerTest {

    @InjectMocks
    FinanceiroController financeiroController;
    @Mock
    FinanceiroRepository controleFinanceiro;
    @Mock
    RemedioRepository remedioRepository;
    @Mock
    DashBoardsRepository dashBoardsRepository;
    @Mock
    UserSessionService userSessionService;
    @Mock
    FinanceiroService financeiroService;
    @Mock
    Model model;

    @DisplayName("Deve exibir tela de gasto")
    @Test
    public void telaDeGastos(){
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(controleFinanceiro.findAllByUsuarioID(1L)).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        Mockito.when(financeiroService.filtrarPorTempo(any(), any())).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        String result = financeiroController.telaDeGastos(model, "2022-01-01 12:00");
        Assertions.assertEquals("listas/ListarGasto", result);
    }

    @DisplayName("Deve exibir tela de gasto B")
    @Test
    public void telaDeGastosB(){
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(controleFinanceiro.findAllByUsuarioID(1L)).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        Mockito.when(financeiroService.filtrarPorTempo(any(), any())).thenReturn(Collections.singletonList(FinanceiroMock.gastoMock()));
        String result = financeiroController.telaDeGastosB(model, "2022-01-01 12:00");
        Assertions.assertEquals("listas/ListarGastoB", result);
    }

    @DisplayName("Deve exibir tela de cadastrar gasto")
    @Test
    public void telaDeGastosCadastro(){
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(Collections.singletonList(RemedioMock.remedioMock()));
        String result = financeiroController.telaDeGastosCadastro(model);
        Assertions.assertEquals("cadastros/CadastroGasto", result);
    }

    @DisplayName("Deve cadastrar gasto")
    @Test
    public void cadastrarGasto(){
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        String result = financeiroController.cadastrarGasto(50.0, "2022-01-01", 1L, Collections.singletonList(RemedioMock.remedioMock()));
        Assertions.assertEquals("redirect:/remedios/controle_de_gastos?time=1", result);
    }

    @DisplayName("Deve falhar ao cadastrar gasto")
    @Test
    public void cadastrarGastoFalha(){
        Mockito.when(dashBoardsRepository.save(any())).thenThrow(ServiceConfigurationError.class);
        String result = financeiroController.cadastrarGasto(50.0, "2022-01-01", 1L, Collections.singletonList(RemedioMock.remedioMock()));
        Assertions.assertEquals("TemplateError", result);
    }

    @DisplayName("Deve falhar ao deletar gasto")
    @Test
    public void deletarGastoFalha(){
        String result = financeiroController.deletarGasto(1L);
        Assertions.assertEquals("TemplateError", result);
    }
    @DisplayName("Deve deletar gasto")
    @Test
    public void deletarGasto(){
        Mockito.when(controleFinanceiro.existsById(1L)).thenReturn(true);
        String result = financeiroController.deletarGasto(1L);
        Assertions.assertEquals("redirect:/remedios/controle_de_gastos/listar?time=1", result);
    }

    @DisplayName("Deve exibir tela atualizar gasto")
    @Test
    public void atualizarGasto(){
        Mockito.when(controleFinanceiro.existsById(1L)).thenReturn(true);
        Mockito.when(controleFinanceiro.findById(1L)).thenReturn(FinanceiroMock.gastoMock());
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        Mockito.when(remedioRepository.findAllByUsuario(any())).thenReturn(Collections.singletonList(RemedioMock.remedioMock()));
        String result = financeiroController.atualizarGasto(1L, model);
        Assertions.assertEquals("atualizacoes/AtualizarGasto", result);
    }
    @DisplayName("Deve falhar ao exibir tela atualizar gasto")
    @Test
    public void atualizarGastoErro(){
        String result = financeiroController.atualizarGasto(1L, model);
        Assertions.assertEquals("TemplateError", result);
    }

    @DisplayName("Deve atualizar gasto")
    @Test
    public void atualizar(){
        Mockito.when(controleFinanceiro.findById(1L)).thenReturn(FinanceiroMock.gastoMock());
        String result = financeiroController.atualizar(1L, 50.0, "2022-01-01", 1L, Collections.singletonList(RemedioMock.remedioMock()));
        Assertions.assertEquals("redirect:/remedios/controle_de_gastos/listar?time=1", result);
    }

    @DisplayName("Deve falhar ao atualizar gasto")
    @Test
    public void atualizarFalha(){
        Mockito.when(controleFinanceiro.findById(1L)).thenThrow(NullPointerException.class);
        String result = financeiroController.atualizar(1L, 50.0, "2022-01-01", 1L, Collections.singletonList(RemedioMock.remedioMock()));
        Assertions.assertEquals("TemplateErrorjava.lang.NullPointerException", result);
    }
    @DisplayName("Deve exibir tela de cadastrar gasto direto")
    @Test
    public void gastoRemedioDireto(){
        Mockito.when(remedioRepository.findById(1L)).thenReturn(RemedioMock.remedioMock());
        String result = financeiroController.gastoRemedioDireto(1L, model);
        Assertions.assertEquals("cadastros/CadastroGastoDireto", result);
    }
    @DisplayName("Deve falhar exibir tela de cadastrar gasto direto")
    @Test
    public void gastoRemedioDiretoFalha(){
        Mockito.when(remedioRepository.findById(1L)).thenReturn(null);
        String result = financeiroController.gastoRemedioDireto(1L, model);
        Assertions.assertEquals("cadastros/CadastroGastoDireto", result);
    }

    @DisplayName("Deve cadastrar gasto direto")
    @Test
    public void cadastroGastoRemedioDireto(){
        Mockito.when(remedioRepository.findById(1L)).thenReturn(RemedioMock.remedioMock());
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        String result = financeiroController.cadastroGastoRemedioDireto(1L, 50.0, "2022-01-01", 1L);
        Assertions.assertEquals("redirect:/remedios/controle_de_gastos/listar?time=1", result);
    }

    @DisplayName("Deve falhar cadastrar gasto direto")
    @Test
    public void cadastroGastoRemedioDiretoFalha(){
        Mockito.when(remedioRepository.findById(1L)).thenReturn(null);
        Mockito.when(userSessionService.returnIdUsuarioLogado()).thenReturn(1L);
        String result = financeiroController.cadastroGastoRemedioDireto(1L, 50.0, "2022-01-01", 1L);
        Assertions.assertEquals("redirect:/remedios/controle_de_gastos/listar?time=1", result);
    }

}