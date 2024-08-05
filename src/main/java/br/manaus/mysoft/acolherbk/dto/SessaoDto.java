package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoDto {

    private Integer id;
    private Integer numeroSessao;
    private String status;
    private String dia;
    private String hora;
    private Integer idPaciente;
    private Integer idPsicologo;

}
