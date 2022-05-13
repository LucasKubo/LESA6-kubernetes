package MeuRemedio.app.models.remedios.agendamentos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "Agendamento")
public class Agendamento implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "AG_ID")
    private long id;

    @Column(name = "AG_Data_Inicio_Agendamento")
    private Date AG_DataInicio;

    @Column(name = "AG_Hora_Inicio_Agendamento")
    private Time AG_horaInicio;

    @Column(name = "AG_Data_Final_Agendamento")
    private Date AG_DataFinal;

    private long AG_Periodicidade;

    public Agendamento(long id, Date AG_DataInicio, Time AG_horaInicio, Date AG_DataFinal, long AG_Periodicidade) {
        this.id = id;
        this.AG_DataInicio = AG_DataInicio;
        this.AG_horaInicio = AG_horaInicio;
        this.AG_DataFinal = AG_DataFinal;
        this.AG_Periodicidade = AG_Periodicidade;
    }

    public Agendamento() {

    }
}
