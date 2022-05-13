package MeuRemedio.app.models.remedios.agendamentos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "Recorrencia")
@PrimaryKeyJoinColumn(referencedColumnName = "AG_ID")
public class Recorrencia extends Agendamento implements Serializable {

    private static long serialVersionUID = 1L;

    @Column(name = "RE_Dias_Semana")
    private long RE_DiasSemana;

    public Recorrencia(long id, Date AG_DataInicio, Time AG_horaInicio, Date AG_DataFinal, long AG_Periodicidade, long RE_DiasSemana) {
        super(id, AG_DataInicio, AG_horaInicio, AG_DataFinal, AG_Periodicidade);
        this.RE_DiasSemana = RE_DiasSemana;
    }

    public Recorrencia() {

    }
}
