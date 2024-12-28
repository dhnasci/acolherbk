package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAnaliticoSessao {

    private String psicologo;
    private String paciente;
    private String sessao;
    private String atendimento;
    private String feedback;

}
