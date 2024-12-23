package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalDto {

    private String numPacientes;
    private String numPsicologos;
}
