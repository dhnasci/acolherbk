package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoTriagemDto {

    private Integer id;
    private String login;
    private String psicologo;
    private String paciente;
    private String diaAlocacao;
    private String status;

}
