package MeuRemedio.app.models.usuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioNotificationTokenId implements Serializable {

    private static final long serialVersionUID = 7699449173723614570L;

    private String token;
    private Long usuario;
}