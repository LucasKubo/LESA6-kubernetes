package MeuRemedio.app.models.usuarios;

import MeuRemedio.app.models.remedios.Remedio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Financeiro implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Column(name = "GA_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String data;
    @NotNull
    private double valor;
    @NotNull
    private long qtdParcela;

    @Column(name = "usuario_us_id")
    private Long usuarioID;

    @NotNull
    private LocalDate Criado_em;

    @ManyToMany()
    @JoinTable(name = "financeiro_remedio",
            joinColumns = {@JoinColumn(name = "GA_ID")},
            inverseJoinColumns = {@JoinColumn(name = "RM_ID")})
    private List <Remedio> remedio = new ArrayList<>();


    public Financeiro (List<Remedio> remedios, String data, double valor, long qtdParcela, long usuarioId) {
        this.remedio = remedios;
        this.data = data; // Data da compra do remedio. Para o gráfico usar esse campo
        this.valor = valor;
        this.qtdParcela = qtdParcela;
        this.Criado_em = LocalDate.now();  // Data de cadastro
        this.usuarioID = usuarioId;
    }

    public Financeiro (String ga_data, double ga_valor, long ga_parcela, List<Remedio> ag_remedios, long usuarioID) {
        this.data = ga_data;
        this.valor = ga_valor;
        this.qtdParcela = ga_parcela;
        this.remedio = ag_remedios;
        this.usuarioID = usuarioID;
        this.Criado_em = LocalDate.now();
    }
}