package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NomePsicologoDto {

    private Integer id;
    private String psicologo;

}
