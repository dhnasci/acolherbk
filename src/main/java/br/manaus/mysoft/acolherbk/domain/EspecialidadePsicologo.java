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
@Table(name="especialidadePsicologo")
public class EspecialidadePsicologo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "especialidadeid")
    private Especialidade especialidade;

    @ManyToOne
    @JoinColumn(name = "psicologoid")
    private Psicologo psicologo;

}
