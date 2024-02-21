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

    private Integer numeroSessao;  // pode ter no máximo 4 sessões
    private Boolean isPacienteAtendido;
    private LocalDateTime diaAgendado;
    private String loginPsicologo;
    private String feedback;
    private String motivoCancelamento;
    private Boolean isCancelado;

    @ManyToOne
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

}
