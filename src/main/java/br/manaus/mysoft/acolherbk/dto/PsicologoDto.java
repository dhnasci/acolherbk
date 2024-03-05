package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsicologoDto {
    private Integer id;
    private String nomeCompleto;
    private String celular1;
    private String isWhatsapp1;
    private String celular2;
    private String isWhatsapp2;
    private String login;
    private String perfil;
    private String email;
    private String crp;
}
