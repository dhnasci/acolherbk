package br.manaus.mysoft.acolherbk.dto;

import lombok.Data;

@Data
public class HorarioPacienteForm {

    private Integer id;
    private String horario;
    private Integer pacienteId;
}
