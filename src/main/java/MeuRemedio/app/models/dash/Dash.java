package MeuRemedio.app.models.dash;

import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Dash {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne @NotNull
    @JoinColumn(name = "Usuario_FK_Usuario")
    private Usuario usuario;

    @Column(name = "Valor", nullable = false)
    private Double valor;

    @Column(name = "Data_da_compra", nullable = false)
    private String data;

    public Dash(Usuario usuario, Double valor, String data) {
        this.usuario = usuario;
        this.valor = valor;
        this.data = data;
    }
}
