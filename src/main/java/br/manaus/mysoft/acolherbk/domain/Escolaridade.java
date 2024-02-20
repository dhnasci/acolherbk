package br.manaus.mysoft.acolherbk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "escolaridade")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Escolaridade {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    @JsonIgnore
    @OneToOne(mappedBy = "escolaridade")
    private Paciente paciente;

    public Escolaridade(String descricao) {
        this.descricao = descricao;
    }
}
