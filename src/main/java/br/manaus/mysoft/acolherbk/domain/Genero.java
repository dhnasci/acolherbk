package br.manaus.mysoft.acolherbk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "genero")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Genero {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    public Genero(String descricao) {
        this.descricao = descricao;
    }
}
