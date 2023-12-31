package MeuRemedio.app.models.remedios;

import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "REMEDIO")
public class Remedio implements Comparable<Remedio>, Serializable  {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RM_ID", nullable = false)
    private Long RM_ID;

    @Column(nullable = false)
    @NotNull @NotEmpty
    @NotBlank
    private String RM_Nome;

    @Column(nullable = false)
    @NotNull @NotEmpty
    @NotBlank
    private String RM_Dosagem;

    @Column(nullable = false)
    @NotNull @NotEmpty
    @NotBlank
    private String RM_UnidadeDosagem;

    private Boolean RM_RetiradoSus ;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="RemedioCategoria",
                joinColumns= {@JoinColumn(name="remedio_rm_id")},
                inverseJoinColumns = {@JoinColumn(name="categoria_ct_id")})
    private List<Categoria> categoria = new ArrayList <Categoria> () ;

    @ManyToMany
    @JoinTable(name="AgendamentoRemedio",
            joinColumns= {@JoinColumn(name="remedio_rm_id")},
            inverseJoinColumns = {@JoinColumn(name="agendamento_ag_id")})
    private List <Agendamento> agendamentos = new ArrayList <> ();

   @ManyToOne @NotNull
   @JoinColumn(name = "Usuario_FK_Usuario")
    private Usuario usuario;

    private LocalDate Criado_em = LocalDate.now();

    public Remedio (String RM_Nome, String RM_Dosagem, String RM_UnidadeDosagem, Boolean RM_RetiradoSus,
                    Usuario usuario) {

        this.RM_Nome = RM_Nome;
        this.RM_Dosagem = RM_Dosagem;
        this.RM_UnidadeDosagem = RM_UnidadeDosagem;
        this.RM_RetiradoSus = RM_RetiradoSus;
        this.usuario = usuario;
    }

    public Remedio (String RM_Nome, String RM_Dosagem, String RM_UnidadeDosagem, Usuario usuario) {
        this.RM_Nome = RM_Nome;
        this.RM_Dosagem = RM_Dosagem;
        this.RM_UnidadeDosagem = RM_UnidadeDosagem;
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return RM_Nome + " - " + RM_Dosagem + " " + RM_UnidadeDosagem;
    }

    @Override
    public int compareTo(Remedio seguinte) {
        if (this.RM_Nome.compareToIgnoreCase(seguinte.RM_Nome) > 0)
            return 1;
        if (this.RM_Nome.compareToIgnoreCase(seguinte.RM_Nome) < 0)
            return -1;
        return 0;
    }


}
