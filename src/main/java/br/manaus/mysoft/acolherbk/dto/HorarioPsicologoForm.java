package br.manaus.mysoft.acolherbk.dto;

import lombok.Data;

@Data
public class HorarioPsicologoForm {

    private Integer id;
    private String horario;
    private Integer psicologoId;
}
