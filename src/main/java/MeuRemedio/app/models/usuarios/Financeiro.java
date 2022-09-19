package MeuRemedio.app.models.usuarios;

import MeuRemedio.app.models.remedios.Remedio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
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

    @NotNull
    private LocalDate Criado_em;

    @OneToMany @NotNull
    private List <Remedio> remedio;

    public Financeiro (String data, double valor, long qtdParcela) {
        this.data = data; // Data da compra do remedio. Para o gráfico usar esse campo
        this.valor = valor;
        this.qtdParcela = qtdParcela;
        this.Criado_em = LocalDate.now(); // Data de cadastro
    }

    public Financeiro (List<Remedio> remedios, String data, double valor, long qtdParcela) {
        this.remedio = remedios;
        this.data = data;
        this.valor = valor;
        this.qtdParcela = qtdParcela;
        this.Criado_em = LocalDate.now();
    }
}
