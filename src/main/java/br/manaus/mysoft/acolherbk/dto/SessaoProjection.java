package br.manaus.mysoft.acolherbk.dto;

public interface SessaoProjection {
    Integer getNumeroSessao();
    Integer getId();
    Boolean getIsCancelado();
    Boolean getIsAtendido();
    String getDiaAgendado();
}
