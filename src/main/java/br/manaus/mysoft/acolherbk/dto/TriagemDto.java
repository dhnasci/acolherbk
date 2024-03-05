package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriagemDto {

    private Integer id;
    private String login;
    private String psicologoId;
    private String pacienteId;
    private String diaAlocado;

}
