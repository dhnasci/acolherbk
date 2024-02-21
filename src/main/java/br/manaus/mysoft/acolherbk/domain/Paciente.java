package br.manaus.mysoft.acolherbk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String nomeCompleto;
    private String celular1;
    private Boolean isWhatsapp1;
    private String celular2;
    private Boolean isWhatsapp2;
    private String nomeIndicacao;

    @OneToOne
    @JoinColumn(name = "profissao_id")
    private Profissao profissao;
    private Boolean jaFezTerapia;
    private String queixa;
    private Integer idade;
    private Float renda;
    private LocalDateTime cadastro;

    @OneToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @OneToOne
    @JoinColumn(name = "escolaridade_id")
    private Escolaridade escolaridade;

    @OneToOne
    @JoinColumn(name = "especialidade_id")
    private Especialidade especialidade;

    @OneToMany(mappedBy = "paciente")
    private List<HorarioPaciente> horarios;

    @OneToMany(mappedBy = "paciente")
    private List<Triagem> triagems;

    @OneToMany(mappedBy = "paciente")
    private List<Sessao> sessoes;

}
