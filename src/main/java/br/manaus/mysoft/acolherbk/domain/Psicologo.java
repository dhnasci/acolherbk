package br.manaus.mysoft.acolherbk.domain;

import br.manaus.mysoft.acolherbk.enums.Perfil;
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

    private String nomeCompleto;
    private String celular1;
    private Boolean isWhatsapp1;
    private String celular2;
    private Boolean isWhatsapp2;
    private String login;
    private String senha;
    private Perfil perfil;
    private String CRP;
    private String email;
    private LocalDateTime cadastro;

    @OneToMany(mappedBy = "psicologo")
    private List<Especialidade> especialidades;

    @OneToMany(mappedBy = "psicologo")
    private List<HorarioPsi> horarios;

    @OneToMany(mappedBy = "psicologo")
    private List<Triagem> triagems;

    @OneToMany(mappedBy = "psicologo")
    private List<Sessao> sessoes;

}
