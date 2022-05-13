package MeuRemedio.app.models.remedios;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "Remedio_Brasil")
public class RemedioBrasil implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "RB_ID", nullable = false)
    private long CT_ID;

    @NotNull
    private String RB_Nome;
    @NotNull
    private String RB_FormaFarmaceutica;
    @NotNull
    private String RB_CodigoATC;
    @NotNull
    private String RB_Componente;

    public RemedioBrasil(long CT_ID, String RB_Nome, String RB_FormaFarmaceutica, String RB_CodigoATC, String RB_Componente) {
        this.CT_ID = CT_ID;
        this.RB_Nome = RB_Nome;
        this.RB_FormaFarmaceutica = RB_FormaFarmaceutica;
        this.RB_CodigoATC = RB_CodigoATC;
        this.RB_Componente = RB_Componente;
    }

    public RemedioBrasil() {
    }
}
