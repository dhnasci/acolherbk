package br.manaus.mysoft.acolherbk.utils;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.dto.PacienteDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mapper {
    public static LocalDateTime converteParaData(String inicio) {
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(inicio, formatoDia);
    }
    public static List<String> preparaEscolaridade(List<Escolaridade> lista) {
        List<String> escolaridades = new ArrayList<>();
        for( Escolaridade escolaridade : lista) {
            escolaridades.add(escolaridade.getDescricao());
        }
        return escolaridades;
    }

    public static List<String> preparaProfissao(List<Profissao> lista) {
        List<String> profissoes = new ArrayList<>();
        for( Profissao profissao : lista) {
            profissoes.add(profissao.getDescricao());
        }
        return profissoes;
    }

    public static List<String> preparaGenero(List<Genero> lista) {
        List<String> generos = new ArrayList<>();
        for (Genero genero : lista) {
            generos.add(genero.getDescricao());
        }
        return generos;
    }

    public static List<String> preparaEspecialidade(List<Especialidade> lista) {
        List<String> especialidades = new ArrayList<>();
        for (Especialidade especialidade : lista) {
            especialidades.add(especialidade.getDescricao());
        }
        return especialidades;
    }

    public static List<String> preparaHorarios(List<Horario> lista) {
        List<String> horarios = new ArrayList<>();
        for (Horario horario : lista) {
            horarios.add(converteHorario(horario));
        }
        return horarios;
    }

    private static String converteHorario(Horario horario) {
        return horario.getDia().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")) + " - " + horario.getTurno().name();
    }

    public  Paciente dtoToPaciente(PacienteDto registro, Perfil perfil, Profissao profissao, Escolaridade escolaridade, Genero genero, List<EspecialidadePaciente> especialidades) {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto(registro.getNomeCompleto());
        paciente.setCadastro(LocalDateTime.now());
        paciente.setIdade(Integer.parseInt( (registro.getIdade()==null)? "0":registro.getIdade() ));
        paciente.setCelular1(registro.getCelular1());
        paciente.setCelular2(registro.getCelular2());
        paciente.setIsWhatsapp1(Boolean.parseBoolean(registro.getIsWhatsapp1()));
        paciente.setIsWhatsapp2(Boolean.parseBoolean(registro.getIsWhatsapp2()));
        paciente.setNomeIndicacao(registro.getNomeIndicacao());
        paciente.setJaFezTerapia(Boolean.parseBoolean(registro.getJaFezTerapia()));
        paciente.setQueixa(registro.getQueixa());
        paciente.setRegistroGeral(registro.getRegistroGeral());
        paciente.setRenda(Float.parseFloat( (registro.getRenda()==null)?"0.00":registro.getRenda() ));
        if (profissao!=null) {
            paciente.setProfissao_id(profissao.getId());
        }
        if (escolaridade!=null) {
            paciente.setEscolaridade_id(escolaridade.getId());
        }
        if (genero!=null) {
            paciente.setGenero_id(genero.getId());
        }

        if(perfil.equals(Perfil.TRIAGEM) && especialidades!=null){
            paciente.setEspecialidades(especialidades);
        }
        return paciente;
    }

}
