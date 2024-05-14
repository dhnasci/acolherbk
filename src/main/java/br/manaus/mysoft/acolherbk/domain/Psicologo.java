package br.manaus.mysoft.acolherbk.domain;

import br.manaus.mysoft.acolherbk.enums.Perfil;
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
@Table(name="psicologo")
public class Psicologo {

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

    @Column(name = "login")
    private String login;
    @JsonIgnore

    @Column(name = "senha")
    private String senha;

    @Column(name = "perfil")
    private Perfil perfil;

    @Column(name = "crp")
    private String crp;

    @Column(name = "email")
    private String email;

    @Column(name = "cadastro")
    private LocalDateTime cadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "psicologo")
    private List<EspecialidadePsicologo> especialidades;

    @JsonIgnore
    @OneToMany(mappedBy = "psicologo")
    private List<HorarioPsi> horarios;

    @JsonIgnore
    @OneToMany(mappedBy = "psicologo")
    private List<Triagem> triagems;

    @JsonIgnore
    @OneToMany(mappedBy = "psicologo")
    private List<Sessao> sessoes;

}
