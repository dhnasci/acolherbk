package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.dto.ChartDto;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoDto;
import br.manaus.mysoft.acolherbk.dto.PacienteDto;
import br.manaus.mysoft.acolherbk.dto.TotalDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.*;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                    if (registro.getEscolaridade() == null) {
                        registro.setEscolaridade("Fundamental");
                    }
                    if (registro.getGenero() == null) {
                        registro.setGenero("Feminino");
                    }
                    Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
                    Genero genero = generoService.getByDescricao(registro.getGenero());
                    Paciente paciente = mapper.dtoToPaciente(registro, perfil, escolaridade, genero, null);
                    Paciente reg = service.insert(paciente);
                    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registro.getId()).toUri();
                    return ResponseEntity.created(uri).body(reg);
                } catch (DataIntegrityViolationException e) {
                    StandardError error = new StandardError(HttpStatus.CONFLICT.value(), "Erro de nome de paciente duplicado!", System.currentTimeMillis());
                    return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(error);
                } catch (Exception e) {
                    StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), String.format("%s - causa: %s", e.getMessage(), e.getCause().getMessage()), System.currentTimeMillis());
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
            List<PacienteAlocadoDto> lista = service.listarTodosPacientes();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/atendidos")
    public ResponseEntity<Object> listarAtendidos() {
        try {
            List<PacienteAlocadoDto> lista = service.listarTodosPacientesAtendidos();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/emTriagem")
    public ResponseEntity<Object> listarEmTriagem() {
        try {
            List<PacienteAlocadoDto> lista = service.listarTodosPacientesEmTriagem();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/cancelados")
    public ResponseEntity<Object> listarCancelados() {
        try {
            List<PacienteAlocadoDto> lista = service.listarTodosPacientesCancelados();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/pendentes")
    public ResponseEntity<Object> listarPendentes() {
        try {
            List<PacienteAlocadoDto> lista = service.listarTodosPacientesPendentes();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/emAtendimento")
    public ResponseEntity<Object> listarEmAtendimento() {
        try {
            List<PacienteAlocadoDto> lista = service.listarTodosPacientesEmAtendimento();
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/peloId/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            Paciente paciente = service.find(id);
            String escolaridade = escolaridadeService.find(paciente.getEscolaridadeid()).getDescricao();
            String genero = generoService.find(paciente.getGeneroid()).getDescricao();
            return ResponseEntity.ok().body(mapper.pacienteToDto(paciente, escolaridade, genero));
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

    @GetMapping(value = "/pendentesTriagem")
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

    @GetMapping(value = "/triagem/todos/{psicologoId}")
    public ResponseEntity<Object> buscarTodosPacientesAlocadosAoPsicologo(@PathVariable Integer psicologoId) {
        try {
            List<PacienteAlocadoDto> lista = service.buscarTodosPacientesAlocadosAoPsicologo(psicologoId);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/triagem/atendidos/{psicologoId}")
    public ResponseEntity<Object> buscarPacientesAlocadosAoPsicologoAtendidos(@PathVariable Integer psicologoId) {
        try {
            List<PacienteAlocadoDto> lista = service.buscarPacientesAlocadosAoPsicologoAtendidos(psicologoId);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/triagem/emAtendimento/{psicologoId}")
    public ResponseEntity<Object> buscarPacientesAlocadosAoPsicologoEmAtendimento(@PathVariable Integer psicologoId) {
        try {
            List<PacienteAlocadoDto> lista = service.buscarPacientesAlocadosAoPsicologoEmAtendimento(psicologoId);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/triagem/emTriagem/{psicologoId}")
    public ResponseEntity<Object> buscarPacientesAlocadosAoPsicologoEmTriagem(@PathVariable Integer psicologoId) {
        try {
            List<PacienteAlocadoDto> lista = service.buscarPacientesAlocadosAoPsicologoEmTriagem(psicologoId);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/triagem/cancelados/{psicologoId}")
    public ResponseEntity<Object> buscarPacientesAlocadosAoPsicologoCancelados(@PathVariable Integer psicologoId) {
        try {
            List<PacienteAlocadoDto> lista = service.buscarPacientesAlocadosAoPsicologoCancelados(psicologoId);
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping(value = "/{perfil}")
    public ResponseEntity<Object> update(@RequestBody PacienteDto registro, @PathVariable Perfil perfil) {
        try {
            Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
            Genero genero = generoService.getByDescricao(registro.getGenero());
            Paciente pacienteConsultado = service.find(registro.getId());
            pacienteConsultado.setEscolaridadeid(escolaridade.getId());
            pacienteConsultado.setGeneroid(genero.getId());
            Paciente paciente = mapper.dtoToPaciente(registro, pacienteConsultado);
            service.update(paciente);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            StandardError error = new StandardError(HttpStatus.CONFLICT.value(), "Erro de nome de paciente duplicado!", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(error);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), String.format("%s - causa: %s", e.getMessage(), e.getCause().getMessage()), System.currentTimeMillis());
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

    @GetMapping(value = "/graficoStatusAtendimento", produces = "application/json")
    public ResponseEntity<Object> obterGraficoStatusAtendimento() {
        try {
            ChartDto grafico = service.obterStatusAtendimentoParaGrafico();
            return ResponseEntity.ok().body(grafico);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/graficoDistribuicaoFaixaEtaria", produces = "application/json")
    public ResponseEntity<Object> obterGraficoDistribuicaoFaixaEtaria() {
        try {
            ChartDto grafico = service.obterDistribuicaoFaixaEtariaParaGrafico();
            return ResponseEntity.ok().body(grafico);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/gettingTotals")
    public ResponseEntity<Object> obterTotais() {

        try {
            TotalDto totais = service.obterTotais();
            return ResponseEntity.ok().body(totais);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
