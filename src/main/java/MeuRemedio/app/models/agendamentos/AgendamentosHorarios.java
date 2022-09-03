package MeuRemedio.app.models.agendamentos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agendamentosHorarios")
public class AgendamentosHorarios implements Serializable {

    @EmbeddedId
    AgendamentosHorariosId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "agendamento", referencedColumnName = "ag_id", insertable = false, updatable = false)
    private Agendamento agendamento = null;
}
