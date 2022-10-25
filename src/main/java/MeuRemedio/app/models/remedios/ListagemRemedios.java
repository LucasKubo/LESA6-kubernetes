package MeuRemedio.app.models.remedios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "ListagemRemedios")
@NoArgsConstructor
public class ListagemRemedios implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "RS_ID", nullable = false)
    private long id;

    @NotNull @NotBlank
    @Column(name = "RS_Nome")
    private String nome;
    @NotNull @NotBlank
    @Column(name = "RS_DisponivelSus")
    private Boolean disponivelSus;

    public ListagemRemedios(String nome, Boolean disponivelSus) {
        this.nome = nome;
        this.disponivelSus = disponivelSus;
    }
}
