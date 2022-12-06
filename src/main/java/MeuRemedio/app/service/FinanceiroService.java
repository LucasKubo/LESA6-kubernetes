package MeuRemedio.app.service;

import MeuRemedio.app.models.usuarios.Financeiro;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceiroService {

    public List<Financeiro> filtrarPorTempo(List<Financeiro> financeiro, String time) {
        LocalDate now = LocalDate.now();
        List<Financeiro> financeiroFiltrado = financeiro;

        if (time.equals("1")) {
            financeiroFiltrado = financeiro.stream()
                    .filter((e) -> LocalDate.parse(e.getData()).getMonth() == now.getMonth() && LocalDate.parse(e.getData()).getYear() == now.getYear())
                    .collect(Collectors.toList());
        } else if (time.equals("0")) {
            financeiroFiltrado = financeiro.stream()
                    .filter((e) -> LocalDate.parse(e.getData()).isAfter(now))
                    .collect(Collectors.toList());
        } else if (time.equals("99")) {
            financeiroFiltrado = financeiro.stream()
                    .filter((e) -> LocalDate.parse(e.getData()).isBefore(now))
                    .collect(Collectors.toList());
        }
        return financeiroFiltrado;
    }

}