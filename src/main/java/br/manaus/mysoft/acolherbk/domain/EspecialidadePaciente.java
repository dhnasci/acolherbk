package br.manaus.mysoft.acolherbk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="especialidade_paciente")
public class EspecialidadePaciente {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "especialidade_id")
    private Especialidade especialidade;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

}
