package MeuRemedio.app.repository;


import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AgendamentosHorariosRepository extends CrudRepository<AgendamentosHorarios, Long> {

    List<AgendamentosHorarios> findAllByIdAgendamento(Long idAgendamento);

//    @Query("DELETE FROM AgendamentosHorarios WHERE id.agendamento =:codigo")
//    void deletarTodosPorId(@Param("codigo") Long codigo);


    void deleteAllByIdAgendamento(Long idAgendamento);

    @Modifying
    @Query("DELETE FROM AgendamentosHorarios WHERE id.horaDataNotificacao <:agora")
    void deletarPorHorario(@Param("agora")LocalDateTime agora);


    @Query(value = "from AgendamentosHorarios " +
            "WHERE id.horaDataNotificacao >=:agora " +
            "AND DATE(id.horaDataNotificacao) <CURRENT_DATE + 1" +
            " AND agendamento.usuarioID =:id ORDER BY id.horaDataNotificacao")
    List<AgendamentosHorarios> selecionarHorarios(@Param("id") Long id, @Param("agora")LocalDateTime agora);
}
