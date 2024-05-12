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
@Table(name="triagem")
public class Triagem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Boolean is_paciente_alocado;
    private LocalDateTime dia_alocacao;
    private String login_alocador;
    private Boolean is_cancelado;

    @ManyToOne
    @JoinColumn(name = "psicologoid")
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "pacienteid")
    private Paciente paciente;

}
