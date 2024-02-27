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
    private String registroGeral;
    private Boolean jaFezTerapia;
    private String queixa;
    private Integer idade;
    private Float renda;
    private LocalDateTime cadastro;

    private Integer profissao_id;
    private Integer genero_id;
    private Integer escolaridade_id;

    @OneToMany(mappedBy = "paciente")
    private List<EspecialidadePaciente> especialidades;

    @OneToMany(mappedBy = "paciente")
    private List<HorarioPaciente> horarios;

    @OneToMany(mappedBy = "paciente")
    private List<Triagem> triagems;

    @OneToMany(mappedBy = "paciente")
    private List<Sessao> sessoes;

}
