package MeuRemedio.app.models.usuarios;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Usuario_code {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private long US_CD_Code;


    @NotNull
    private String codigo;

    @NotNull
    private String email;


    public Usuario_code (String email, String codigo) {
        this.email = email;
        this.codigo = codigo;
    }
}
