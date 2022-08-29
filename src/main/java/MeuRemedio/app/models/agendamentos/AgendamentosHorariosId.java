package MeuRemedio.app.models.agendamentos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentosHorariosId implements Serializable {

    private static final long serialVersionUID = 7699449173723614570L;


    private Long agendamento;

    private LocalDateTime horaDataNotificacao;
}
