package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteAlocadoDto {

    private String nomeCompleto;
    private String celular1;
    private String isWhatsapp1;
    private String celular2;
    private String registroGeral;
    private Integer idPaciente;

}
