package br.manaus.mysoft.acolherbk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDto {

    private String id;
    private String nomeCompleto;
    private String celular1;
    private String isWhatsapp1;
    private String celular2;
    private String isWhatsapp2;
    private String nomeIndicacao;
    private String jaFezTerapia;
    private String queixa;
    private String idade;
    private String renda;
    private String cadastro;
    private String registroGeral;
    private String profissao;
    private String genero;
    private String escolaridade;
    private List<String> especialidades;
    
}
