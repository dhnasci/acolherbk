package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoDto;
import br.manaus.mysoft.acolherbk.dto.PacienteDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.*;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/paciente")
public class PacienteController {

    private static final String FALTA_PREENCHER_CAMPOS_OBRIGATORIOS = "Falta preencher campos obrigatórios!";
    private static final String PERFIL_NAO_AUTORIZADO = "Perfil não autorizado!";

    PacienteService service;
    EscolaridadeService escolaridadeService;
    ProfissaoService profissaoService;
    GeneroService generoService;
    EspecialidadeService especialidadeService;
    EspecialidadePacienteService especialidadePacienteService;
    HorarioPacienteService horarioPacienteService;
    HorarioService horarioService;

    private Mapper mapper = new Mapper();

    @Autowired
    public PacienteController(PacienteService service, EscolaridadeService escolaridadeService, ProfissaoService profissaoService, GeneroService generoService, EspecialidadeService especialidadeService, EspecialidadePacienteService especialidadePacienteService, HorarioPacienteService horarioPacienteService, HorarioService horarioService) {
        this.service = service;
        this.escolaridadeService = escolaridadeService;
        this.profissaoService = profissaoService;
        this.generoService = generoService;
        this.especialidadeService = especialidadeService;
        this.especialidadePacienteService = especialidadePacienteService;
        this.horarioPacienteService = horarioPacienteService;
        this.horarioService = horarioService;
    }

    @PostMapping(value = "/{perfil}")
    public ResponseEntity<Object> inserir(@RequestBody PacienteDto registro, @PathVariable Perfil perfil) {
        if (!perfil.equals(Perfil.PSICOLOGO)) {
            if (registro.getNomeCompleto() != null && registro.getCelular1() !=null && registro.getRegistroGeral() != null && registro.getQueixa() != null) {
                try {
                    if (registro.getProfissao() == null) {
                        registro.setProfissao("Estudante");
                    }
                    if (registro.getEscolaridade() == null) {
                        registro.setEscolaridade("Fundamental");
                    }
                    if (registro.getGenero() == null) {
                        registro.setGenero("Feminino");
                    }
                    Profissao profissao = profissaoService.getByDescricao(registro.getProfissao());
                    Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
                    Genero genero = generoService.getByDescricao(registro.getGenero());
                    Paciente paciente = mapper.dtoToPaciente(registro, perfil, profissao, escolaridade, genero, null);
                    Paciente reg = service.insert(paciente);
                    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registro.getId()).toUri();
                    return ResponseEntity.created(uri).body(reg);
                } catch (DataIntegrityViolationException e) {
                    Throwable sqlException = e.getCause();
                    String msg = (sqlException != null) ? sqlException.getCause().getMessage() : e.getMessage();
                    StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), msg, System.currentTimeMillis());
                    return ResponseEntity.badRequest().body(error);
                }
            } else {
                StandardError error = new StandardError(HttpStatus.FORBIDDEN.value(), FALTA_PREENCHER_CAMPOS_OBRIGATORIOS, System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            StandardError error = new StandardError(HttpStatus.UNAUTHORIZED.value(), PERFIL_NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private List<HorarioPaciente> converteHorarios(List<String> horarios, Paciente paciente) {
        List<HorarioPaciente> horarioPacientes = new ArrayList<>();
        for (String horario : horarios) {
            HorarioPaciente horarioPaciente = new HorarioPaciente();
            horarioPaciente.setHorario(horarioService.getByDescricao(horario));
            horarioPaciente.setPaciente(paciente);
            horarioPacientes.add(horarioPaciente);
        }
        return horarioPacientes;
    }

    private List<EspecialidadePaciente> preparaListaEspecialidadePaciente(List<String> especialidades, Paciente paciente) throws ObjetoException {
        List<EspecialidadePaciente> especialidadePacientes = new ArrayList<>();
        for (String especialidade : especialidades) {
            EspecialidadePaciente especialidadePaciente = new EspecialidadePaciente();
            especialidadePaciente.setEspecialidade((especialidadeService.getByDescricao(especialidade)));
            especialidadePaciente.setPaciente(paciente);
            especialidadePacientes.add(especialidadePaciente);
        }
        return especialidadePacientes;
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        try {
            List<PacienteDto> lista = toDto(service.listar());
            return ResponseEntity.ok().body(lista);
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private List<PacienteDto> toDto(List<Paciente> listaPacientes) throws ObjetoException {
        List<PacienteDto> listaDto = new ArrayList<>();
        for (Paciente paciente : listaPacientes) {
            PacienteDto dto = new PacienteDto();
            dto.setId(paciente.getId());
            dto.setNomeCompleto(paciente.getNomeCompleto());
            dto.setCelular1(paciente.getCelular1());
            if (paciente.getIsWhatsapp1() != null) {
                dto.setIsWhatsapp1(paciente.getIsWhatsapp1().toString());
            } else {
                dto.setIsWhatsapp1("");
            }
            dto.setCelular2(paciente.getCelular2());
            if (paciente.getIsWhatsapp2() != null) {
                dto.setIsWhatsapp2(paciente.getIsWhatsapp2().toString());
            } else {
                dto.setIsWhatsapp2("");
            }
            dto.setNomeIndicacao(paciente.getNomeIndicacao());
            dto.setJaFezTerapia(paciente.getJaFezTerapia().toString());
            dto.setQueixa(paciente.getQueixa());
            dto.setIdade(String.valueOf(paciente.getIdade()));
            dto.setRenda(String.valueOf(paciente.getRenda()));
            DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
            dto.setCadastro(paciente.getCadastro().format(formatoDia));
            dto.setRegistroGeral(paciente.getRegistroGeral());
            dto.setProfissao(profissaoService.find(paciente.getProfissaoid()).getDescricao());
            dto.setGenero(generoService.find(paciente.getGeneroid()).getDescricao());
            dto.setEscolaridade(escolaridadeService.find(paciente.getEscolaridadeid()).getDescricao());
            dto.setEspecialidades(Mapper.preparaEspecialidadePaciente(especialidadePacienteService.obterEspecialidadesPorPaciente(paciente)));
            dto.setHorarios(Mapper.preparaHorariosPaciente(horarioPacienteService.obterHorariosPaciente(paciente)));
            listaDto.add(dto);
        }
        return listaDto;
    }

    @GetMapping(value = "/peloId/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            Paciente paciente = service.find(id);
            return ResponseEntity.ok().body(paciente);
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping(value = "/{nome}")
    public ResponseEntity<Object> buscarPorNome(@PathVariable String nome) {
        try {
            List<Paciente> lista = service.buscarPorNome(nome);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping(value = "/{inicio}/{fim}")
    public ResponseEntity<Object> buscarPacientesAgendados(@PathVariable String inicio, @PathVariable String fim) {
        try {
            List<Paciente> lista = service.buscarPacientesAgendadosNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/triagem/{inicio}/{fim}")
    public ResponseEntity<Object> buscarPacientesTriagemRealizada(@PathVariable String inicio, @PathVariable String fim) {
        try {
            List<Paciente> lista = service.buscarPacientesAlocadosNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/pendentes")
    public ResponseEntity<Object> buscarPendentesTriagem() {
        try {
            List<Paciente> lista = service.buscarPacientesPendentesDeTriagem();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/triagem/{psicologoId}")
    public ResponseEntity<Object> buscarPacientesAlocadosAoPsicologo(@PathVariable Integer psicologoId) {
        try {
            List<PacienteAlocadoDto> lista = service.buscarPacientesAlocadosAoPsicologo(psicologoId);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping(value = "/{perfil}")
    public ResponseEntity<Object> update(@RequestBody PacienteDto registro, @PathVariable Perfil perfil) {
        try {
            Profissao profissao = profissaoService.getByDescricao(registro.getProfissao());
            Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
            Genero genero = generoService.getByDescricao(registro.getGenero());
//            List<EspecialidadePaciente> especialidades = preparaListaEspecialidadePaciente(registro.getEspecialidades(), service.find(registro.getId()));
            Paciente paciente = mapper.dtoToPaciente(registro, perfil, profissao, escolaridade, genero, null);
            service.update(paciente);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
