package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.*;
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

    @Autowired
    PacienteService service;

    @Autowired
    EscolaridadeService escolaridadeService;
    @Autowired
    ProfissaoService profissaoService;
    @Autowired
    GeneroService generoService;
    @Autowired
    EspecialidadeService especialidadeService;
    @Autowired
    EspecialidadePacienteService especialidadePacienteService;
    @Autowired
    HorarioPacienteService horarioPacienteService;

    Mapper mapper;

    @PostMapping(value = "/{perfil}")
    public ResponseEntity<Object> inserir(@RequestBody PacienteDto registro, @PathVariable Perfil perfil) {

        try {
            mapper = new Mapper();
            Profissao profissao = profissaoService.getByDescricao(registro.getProfissao());
            Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
            Genero genero = generoService.getByDescricao(registro.getGenero());
            List<EspecialidadePaciente> especialidades = preparaListaEspecialidadePaciente(registro.getEspecialidades(), null);
            Paciente paciente = mapper.dtoToPaciente(registro, perfil, profissao, escolaridade, genero, especialidades);
            try {
                Paciente reg = service.insert(paciente);
                especialidadePacienteService.salvar(especialidades, reg);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registro.getId()).toUri();
                return ResponseEntity.created(uri).body(reg);
            } catch (Exception e) {
                StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } catch (DataIntegrityViolationException e) {
            Throwable sqlException = e.getCause();
            String msg = (sqlException != null) ? sqlException.getCause().getMessage() : e.getMessage();
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), msg, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        } catch (ObjetoException e1) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e1.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
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
            dto.setIsWhatsapp1(paciente.getIsWhatsapp1().toString());
            dto.setCelular2(paciente.getCelular2());
            dto.setIsWhatsapp2(paciente.getIsWhatsapp2().toString());
            dto.setNomeIndicacao(paciente.getNomeIndicacao());
            dto.setJaFezTerapia(paciente.getJaFezTerapia().toString());
            dto.setQueixa(paciente.getQueixa());
            dto.setIdade(String.valueOf(paciente.getIdade()));
            dto.setRenda(String.valueOf(paciente.getRenda()));
            DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
            dto.setCadastro(paciente.getCadastro().format(formatoDia));
            dto.setRegistroGeral(paciente.getRegistroGeral());
            dto.setProfissao(profissaoService.find(paciente.getProfissao_id()).getDescricao());
            dto.setGenero(generoService.find(paciente.getGenero_id()).getDescricao());
            dto.setEscolaridade(escolaridadeService.find(paciente.getEscolaridade_id()).getDescricao());
            dto.setEspecialidades(Mapper.preparaEspecialidadePaciente(especialidadePacienteService.obterEspecialidadesPorPaciente(paciente)));
            dto.setHorarios(Mapper.preparaHorariosPaciente(horarioPacienteService.obterHorariosPaciente(paciente)));
            listaDto.add(dto);
        }
        return listaDto;
    }

    @GetMapping(value = "/{id}")
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
        List<Paciente> lista = service.buscarPorNome(nome);
        return ResponseEntity.ok().body(lista);

    }

    @GetMapping(value = "/{inicio}/{fim}")
    public ResponseEntity<Object> buscarPacientesAgendados(@PathVariable String inicio, @PathVariable String fim) {
        List<Paciente> lista = service.buscarPacientesAgendadosNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/triagem/{inicio}/{fim}")
    public ResponseEntity<Object> buscarPacientesTriagemRealizada(@PathVariable String inicio, @PathVariable String fim) {
        List<Paciente> lista = service.buscarPacientesAlocadosNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/pendentes")
    public ResponseEntity<Object> buscarPendentesTriagem() {
        List<Paciente> lista = service.buscarPacientesPendentesDeTriagem();
        return ResponseEntity.ok().body(lista);
    }

    @PutMapping(value = "/{perfil}")
    public ResponseEntity<Object> update(@RequestBody PacienteDto registro, @PathVariable Perfil perfil) {
        try {
            mapper = new Mapper();
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
