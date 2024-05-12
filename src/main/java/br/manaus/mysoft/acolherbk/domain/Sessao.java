package br.manaus.mysoft.acolherbk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="sessao")
public class Sessao {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numerosessao")
    private Integer numeroSessao;  // pode ter no máximo 4 sessões

    @Column(name = "ispacienteatendido")
    private Boolean isPacienteAtendido;

    @Column(name = "diaagendado")
    private LocalDateTime diaAgendado;

    @Column(name = "loginpsicologo")
    private String loginPsicologo;
    private String feedback;
    private String motivo_cancelamento;
    private Boolean is_cancelado;

    @ManyToOne
    @JoinColumn(name = "psicologoid")
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "pacienteid")
    private Paciente paciente;

}
