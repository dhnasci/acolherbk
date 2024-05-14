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

    @Column(name = "numero_sessao")
    private Integer numeroSessao;  // pode ter no máximo 4 sessões

    @Column(name = "is_paciente_atendido")
    private Boolean isPacienteAtendido;

    @Column(name = "dia_agendado")
    private LocalDateTime diaAgendado;

    @Column(name = "login_psicologo")
    private String loginPsicologo;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;

    @Column(name = "is_cancelado")
    private Boolean isCancelado;

    @ManyToOne
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

}
