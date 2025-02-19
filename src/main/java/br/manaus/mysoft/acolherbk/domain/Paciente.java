package br.manaus.mysoft.acolherbk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "celular1")
    private String celular1;

    @Column(name = "is_whatsapp1")
    private Boolean isWhatsapp1;

    @Column(name = "celular2")
    private String celular2;

    @Column(name = "is_whatsapp2")
    private Boolean isWhatsapp2;

    @Column(name = "nome_indicacao")
    private String nomeIndicacao;

    @Column(name = "registro_geral")
    private String registroGeral;

    @Column(name = "ja_fez_terapia")
    private Boolean jaFezTerapia;

    @Column(name = "queixa")
    private String queixa;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "renda")
    private Float renda;

    @Column(name = "cadastro")
    private LocalDateTime cadastro;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "profissao_id")
    private Integer profissaoid;

    @Column(name = "genero_id")
    private Integer generoid;

    @Column(name = "escolaridade_id")
    private Integer escolaridadeid;

    @JsonIgnore
    @OneToMany(mappedBy = "paciente")
    private List<EspecialidadePaciente> especialidades;

    @JsonIgnore
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<HorarioPaciente> horarios;

    @JsonIgnore
    @OneToMany(mappedBy = "paciente")
    private List<Triagem> triagems;

    @JsonIgnore
    @OneToMany(mappedBy = "paciente")
    private List<Sessao> sessoes;

    @ManyToOne
    @JoinColumn(name="empresa_id")
    private Empresa empresa;

}
