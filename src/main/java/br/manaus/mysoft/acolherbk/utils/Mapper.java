package br.manaus.mysoft.acolherbk.utils;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.dto.HorarioDto;
import br.manaus.mysoft.acolherbk.dto.PacienteDto;
import br.manaus.mysoft.acolherbk.dto.PsicologoDto;
import br.manaus.mysoft.acolherbk.dto.TriagemDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.enums.Turno;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Mapper {

    public HashMap<String, DayOfWeek> diaDaSemana;

    public Mapper() {
        diaDaSemana = new HashMap<>();
        diaDaSemana.put("segunda-feira", DayOfWeek.MONDAY);
        diaDaSemana.put("terça-feira", DayOfWeek.TUESDAY);
        diaDaSemana.put("quarta-feira", DayOfWeek.WEDNESDAY);
        diaDaSemana.put("quinta-feira", DayOfWeek.THURSDAY);
        diaDaSemana.put("sexta-feira", DayOfWeek.FRIDAY);
        diaDaSemana.put("sábado", DayOfWeek.SATURDAY);

    }

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

    public static List<String> preparaEspecialidadePaciente(List<EspecialidadePaciente> lista) {
        List<String> especialidades = new ArrayList<>();
        for (EspecialidadePaciente especialidade : lista) {
            especialidades.add(especialidade.getEspecialidade().getDescricao());
        }
        return especialidades;
    }

    public static List<String> preparaPsicologosNomes(List<Psicologo> lista) {
        List<String> psicologosNomes = new ArrayList<>();
        for (Psicologo psicologo : lista) {
            psicologosNomes.add(psicologo.getNomeCompleto());
        }
        return psicologosNomes;
    }

    public static List<String> preparaEspecialidadePsicologo(List<EspecialidadePsicologo> lista) {
        List<String> especialidades = new ArrayList<>();
        for (EspecialidadePsicologo especialidade : lista) {
            especialidades.add(especialidade.getEspecialidade().getDescricao());
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

    public static List<String> preparaHorariosPaciente(List<HorarioPaciente> lista) {
        List<String> horarios = new ArrayList<>();
        for (HorarioPaciente horario : lista) {
            horarios.add(converteHorarioPaciente(horario));
        }
        return horarios;
    }

    public static List<String> preparaHorariosPsicologo(List<HorarioPsi> lista) {
        List<String> horarios = new ArrayList<>();
        for (HorarioPsi horario : lista) {
            horarios.add(converteHorarioPsi(horario));
        }
        return horarios;
    }

    private static String converteHorario(Horario horario) {
        return horario.getDia().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")) + ", " + horario.getTurno().name();
    }

    private static String converteHorarioPaciente(HorarioPaciente horario) {
        return horario.getHorario().getDia().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")) + ", " + horario.getHorario().getTurno().name();
    }

    private static String converteHorarioPsi(HorarioPsi horario) {
        return horario.getHorario().getDia().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")) + ", " + horario.getHorario().getTurno().name();
    }

    public static Triagem toTriagem(TriagemDto triagemDto) {
        Triagem triagem = new Triagem();
        triagem.setId(triagemDto.getId());
        triagem.setLoginAlocador(triagemDto.getLogin());
        triagem.setDiaAlocacao(LocalDateTime.now());
        return triagem;
    }

    public static Psicologo toPsicologo(PsicologoDto registro) {
        Psicologo psicologo = new Psicologo();
        psicologo.setId(registro.getId());
        psicologo.setNomeCompleto(registro.getNomeCompleto());
        psicologo.setLogin(registro.getLogin());
        psicologo.setPerfil(Perfil.valueOf(registro.getPerfil()));
        psicologo.setCrp(registro.getCrp());
        psicologo.setCadastro(LocalDateTime.now());
        psicologo.setCelular1(registro.getCelular1());
        psicologo.setCelular2(registro.getCelular2());
        psicologo.setIsWhatsapp1(Boolean.valueOf(registro.getIsWhatsapp1()));
        psicologo.setIsWhatsapp2(Boolean.valueOf(registro.getIsWhatsapp2()));
        psicologo.setEmail(registro.getEmail());
        return psicologo;
    }

    public  List<HorarioDto> converteParaHorarioDto(List<Object[]> resultList) {
        return resultList.stream()
                .map(this::toHorarioDto)
                .collect(Collectors.toList());
    }

    private HorarioDto toHorarioDto(Object[] result) {
        return HorarioDto.builder()
                .id((Integer) result[0])
                .nome((String) result[1])
                .dia(((DayOfWeek) result[2]).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")))
                .turno(((Enum) result[3]).name())
                .build();
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
            paciente.setProfissaoid(profissao.getId());
        }
        if (escolaridade!=null) {
            paciente.setEscolaridadeid(escolaridade.getId());
        }
        if (genero!=null) {
            paciente.setGeneroid(genero.getId());
        }

        if(!perfil.equals(Perfil.PSICOLOGO) && especialidades!=null){
            paciente.setEspecialidades(especialidades);
        }
        return paciente;
    }



}
