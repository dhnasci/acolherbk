package br.manaus.mysoft.acolherbk.domain;

import br.manaus.mysoft.acolherbk.enums.Turno;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="horario")
public class Horario {

    public Horario(DayOfWeek dia, Turno turno) {
        this.dia = dia;
        this.turno = turno;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private DayOfWeek dia;
    private Turno turno;

    @OneToOne(mappedBy = "horario")
    private HorarioPaciente horarioPaciente;

    @OneToOne(mappedBy = "horario")
    private HorarioPsi horarioPsicologo;

}
