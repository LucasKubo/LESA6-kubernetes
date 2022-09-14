package MeuRemedio.app.models.usuarios;

import MeuRemedio.app.models.remedios.Remedio;
import lombok.Getter;
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
public class Financeiro implements Serializable {
    private static long serialVersionUID = 1L;
    @Id
    @Column(name = "GA_ID", nullable = false)
    private Long id;

    @NotNull @NotBlank
    private Date data;
    @NotNull @NotBlank
    private double valor;
    @NotNull @NotBlank
    private long qtdParcela;

    @NotBlank
    private LocalDate Criado_em;

    @OneToMany
    @JoinColumn(name = "FK_RM_id")
    @NotNull
    private List<Remedio> remedio;

    public Financeiro (Date data, double valor, long qtdParcela) {
        this.data = data; // Data da compra do remedio. Para o gráfico usar esse campo
        this.valor = valor;
        this.qtdParcela = qtdParcela;
        this.Criado_em = LocalDate.now(); // Data de cadastro
    }
//    public Gasto (){
//
//    }

    public Financeiro(List<Remedio> remedios, Date data, double valor, long qtdParcela) {
        this.remedio = remedios;
        this.data = data;
        this.valor = valor;
        this.qtdParcela = qtdParcela;
        this.Criado_em = LocalDate.now();
    }
}
