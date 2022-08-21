package MeuRemedio.app.models.usuarios;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
public class Gasto implements Serializable {
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

    public Gasto (Date data, double valor, long qtdParcela) {
        this.data = data;
        this.valor = valor;
        this.qtdParcela = qtdParcela;
        this.Criado_em = LocalDate.now();
    }
    public Gasto (){

    }
}
