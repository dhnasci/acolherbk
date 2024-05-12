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

    @Column(name = "ispacientealocado")
    private Boolean isPacienteAlocado;
    @Column(name = "diaalocacao")
    private LocalDateTime diaAlocacao;
    @Column(name = "loginalocador")
    private String loginAlocador;
    @Column(name = "iscancelado")
    private Boolean isCancelado;

    @ManyToOne
    @JoinColumn(name = "psicologoid")
    private Psicologo psicologo;

    @ManyToOne
    @JoinColumn(name = "pacienteid")
    private Paciente paciente;

}
