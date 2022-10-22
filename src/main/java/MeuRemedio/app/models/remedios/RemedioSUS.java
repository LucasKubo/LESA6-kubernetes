package MeuRemedio.app.models.remedios;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Remedios_SUS")
public class RemedioSUS implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "RS_ID", nullable = false)
    private long RS_ID;

    @NotNull @NotBlank
    private String RS_Nome;
    @NotNull @NotBlank
    private String RS_DisponivelSus;

    @NotBlank
    private LocalDate Criado_em = LocalDate.now();


    public RemedioSUS (){

    }

    public RemedioSUS (String RS_Nome, String RS_DisponivelSus) {
        this.RS_Nome = RS_Nome;
        this.RS_DisponivelSus = RS_DisponivelSus;
        this.Criado_em = LocalDate.now();
    }
}
