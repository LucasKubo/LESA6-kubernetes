package MeuRemedio.app.models.usuarios;

import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.agendamentos.AgendamentosHorariosId;
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
@Table(name = "usuario_notificationtoken")
public class UsuarioNotificationToken implements Serializable {

    @EmbeddedId
    UsuarioNotificationTokenId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "usuario", referencedColumnName = "us_id", insertable = false, updatable = false)
    private Usuario usuario = null;
}
