package br.manaus.mysoft.acolherbk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "especialidade")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Especialidade {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    public Especialidade(String descricao) {
        this.descricao = descricao;
    }
}
