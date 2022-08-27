package MeuRemedio.app.repository;

import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AgendamentosHorariosRepository extends CrudRepository<AgendamentosHorarios, Long> {

    List<AgendamentosHorarios> findAllByIdAgendamento(Long idAgendamento);
}
