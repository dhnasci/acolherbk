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

    private Boolean isPacienteAlocado;
    private LocalDateTime diaAlocacao;
    private String loginAlocador;
    private Boolean isCancelado;

    @ManyToOne
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

}
