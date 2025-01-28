package br.manaus.mysoft.acolherbk.utils;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.dto.*;
import br.manaus.mysoft.acolherbk.enums.Perfil;

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

    private static final String FORMATO_DATA_PADRAO = "dd-MM-yy HH:mm";
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

    public static LocalDateTime converteParaData(String inicio)  {
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern(FORMATO_DATA_PADRAO);
        return LocalDateTime.parse(inicio, formatoDia);
    }
    public static LocalDateTime converteParaDataBD(String inicio)  {
        inicio = inicio.replace(".0", "");
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

    public static List<NomePsicologoDto> preparaPsicologosNomes(List<Psicologo> lista) {
        return lista.stream()
                .map( psi -> NomePsicologoDto.builder()
                        .psicologo(psi.getNomeCompleto())
                        .id(psi.getId()).build() )
                .collect(Collectors.toList());
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

    public  Paciente dtoToPaciente(PacienteDto registro, Perfil perfil, Escolaridade escolaridade, Genero genero, List<EspecialidadePaciente> especialidades) {
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
        paciente.setRegistroGeral(registro.getRegistroGeral());;
        paciente.setProfissao(registro.getProfissao());
        paciente.setRenda(Float.parseFloat( (registro.getRenda()==null)?"0.00":registro.getRenda() ));
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

    public  Paciente dtoToPaciente(PacienteDto registro, Paciente paciente) {
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
        paciente.setRegistroGeral(registro.getRegistroGeral());;
        paciente.setProfissao(registro.getProfissao());
        paciente.setRenda(Float.parseFloat( (registro.getRenda()==null)?"0.00":registro.getRenda() ));
        return paciente;
    }

    public PacienteDto pacienteToDto(Paciente paciente, String escolaridade, String genero) {
        PacienteDto dto = new PacienteDto();
        dto.setNomeCompleto(paciente.getNomeCompleto());
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern(FORMATO_DATA_PADRAO);
        dto.setCadastro(paciente.getCadastro().format(formatoDia));
        dto.setIdade(String.valueOf(paciente.getIdade()));
        dto.setCelular1(paciente.getCelular1());
        dto.setCelular2(paciente.getCelular2());
        dto.setIsWhatsapp1(String.valueOf(paciente.getIsWhatsapp1()));
        dto.setIsWhatsapp2(String.valueOf(paciente.getIsWhatsapp2()));
        dto.setNomeIndicacao(paciente.getNomeIndicacao());
        dto.setJaFezTerapia(String.valueOf(paciente.getJaFezTerapia()));
        dto.setQueixa(paciente.getQueixa());
        dto.setRegistroGeral(paciente.getRegistroGeral());
        dto.setProfissao(paciente.getProfissao());
        dto.setRenda(String.valueOf(paciente.getRenda()));
        dto.setEscolaridade(escolaridade);
        dto.setGenero(genero);
        return dto;
    }


    public Sessao dtoToSessao(SessaoDto dto, Psicologo psicologo, Paciente paciente) {
        Sessao section = new Sessao();
        section.setNumeroSessao(dto.getNumeroSessao());
        String dataCompleta = String.format("%s %s", dto.getDia(), dto.getHora());
        section.setDiaAgendado(converteParaData(dataCompleta));
        section.setLoginPsicologo(psicologo.getLogin());
        section.setPsicologo(psicologo);
        section.setPaciente(paciente);
        return section;
    }

    public ChartDto fromStatusAtendimentoToGrafico(List<StatusAtendimentoProjection> projections) {
        ChartDto chart = new ChartDto();
        List<String> labels = projections.stream()
                .map( proj -> proj.getStatus() )
                .collect(Collectors.toList());
        chart.setLabels(labels);
        List<Integer> valores = projections.stream()
                .map( proj -> proj.getTotal())
                .collect(Collectors.toList());
        DataSet[] datasets = new DataSet[1];
        DataSet dataset = new DataSet();
        dataset.setData(valores);
        Integer tamanho = valores.size();
        dataset.setBackgroundColor(getBackgroundColorBarChart(tamanho));
        dataset.setLabel("Atendimento 2024");
        dataset.setBorderWidth(1);
        dataset.setBorderColor(getBackgroundColorRoscaChart(tamanho));
        datasets[0] = dataset;
        chart.setDatasets(datasets);
        return chart;
    }

    private List<String> getBackgroundColorBarChart(Integer tamanho) {
        List<String> backgrounds = new ArrayList<>();
        backgrounds.add("rgb(255, 99, 132, 0.2)");
        backgrounds.add("rgb(255, 159, 64, 0.2)");
        backgrounds.add("rgb(255, 205, 86, 0.2)");
        backgrounds.add("rgb(75, 192, 192, 0.2)");
        backgrounds.add("rgb(54, 162, 235, 0.2)");
        return ajustaTamanho(backgrounds, tamanho);
    }

    private List<String> ajustaTamanho(List<String> backgrounds, Integer tamanhoDesejado) {
        List<String> listaAjustada = new ArrayList<>(backgrounds);
        if (tamanhoDesejado < 0) {
            throw new IllegalArgumentException("O tamanho desejado não pode ser negativo.");
        }

        if (listaAjustada.size() > tamanhoDesejado) {
            // Reduz a lista ao tamanho desejado
            listaAjustada = listaAjustada.subList(0, tamanhoDesejado);
        } else if (listaAjustada.size() < tamanhoDesejado) {
            // Preenche com strings vazias até o tamanho desejado
            while (listaAjustada.size() < tamanhoDesejado) {
                listaAjustada.add("");
            }
        }

        return listaAjustada;
    }

    public ChartDto fromDistribuicaoFaixaEtariaToGrafico(List<FaixaEtariaDistribuicaoProjection> projections) {
        ChartDto chart = new ChartDto();
        List<String> labels = projections.stream()
                .map( proj -> proj.getFaixaEtaria() )
                .collect(Collectors.toList());
        chart.setLabels(labels);
        List<Integer> valores = projections.stream()
                .map( proj -> proj.getQuantidade() )
                .collect(Collectors.toList());
        DataSet[] datasets = new DataSet[1];
        DataSet dataset = new DataSet();
        dataset.setData(valores);
        dataset.setBackgroundColor(getBackgroundColorRoscaChart(valores.size()));
        datasets[0] = dataset;
        chart.setDatasets(datasets);
        return chart;
    }

    private List<String> getBackgroundColorRoscaChart(Integer tamanho) {
        List<String> backgrounds = new ArrayList<>();
        backgrounds.add("rgb(255, 99, 132)");
        backgrounds.add("rgb(255, 159, 64)");
        backgrounds.add("rgb(255, 205, 86)");
        backgrounds.add("rgb(75, 192, 192)");
        backgrounds.add("rgb(54, 162, 235)");
        return ajustaTamanho(backgrounds, tamanho);
    }
}
