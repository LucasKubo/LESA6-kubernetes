package MeuRemedio.app.service;

import MeuRemedio.app.models.agendamentos.Agendamento;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    public List<Agendamento> filtrarPorStatus(String ativos, List<Agendamento> agendamentos) {

        LocalDate now = LocalDate.now();

        if (ativos.equals("true")) {
            agendamentos = agendamentos.stream()
                    .filter((e) -> LocalDate.parse(e.getDataFinal()).isAfter(now)
                                || LocalDate.parse(e.getDataFinal()).isEqual(now))
                    .collect(Collectors.toList());
        } else {
            agendamentos = agendamentos.stream()
                    .filter((e) -> LocalDate.parse(e.getDataFinal()).isBefore(now))
                    .collect(Collectors.toList());
        }

        return agendamentos;
    }
}
