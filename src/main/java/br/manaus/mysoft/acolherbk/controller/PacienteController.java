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
    Mapper mapper;

    @PostMapping(value = "/{perfil}")
    public ResponseEntity<Object> inserir(@RequestBody PacienteDto registro, @PathVariable Perfil perfil) {

        try {
            mapper = new Mapper();
            Profissao profissao = profissaoService.getByDescricao(registro.getProfissao());
            Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
            Genero genero = generoService.getByDescricao(registro.getGenero());
            List<EspecialidadePaciente> especialidades = preparaListaEspecialidadePaciente(registro.getEspecialidades());
            Paciente reg = service.insert(mapper.dtoToPaciente(registro, perfil, profissao, escolaridade, genero, especialidades));
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registro.getId()).toUri();
            return ResponseEntity.created(uri).body(reg);
        } catch (DataIntegrityViolationException e) {
            Throwable sqlException = e.getCause();
            String msg = (sqlException!=null) ? sqlException.getCause().getMessage() : e.getMessage();
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), msg, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        } catch (ObjetoException e1) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e1.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private List<EspecialidadePaciente> preparaListaEspecialidadePaciente(List<String> especialidades) throws ObjetoException {
        List<EspecialidadePaciente> especialidadePacientes = new ArrayList<>();
        for(String especialidade : especialidades) {
            EspecialidadePaciente especialidadePaciente = new EspecialidadePaciente();
            especialidadePaciente.setEspecialidade_id(especialidadeService.getByDescricao(especialidade).getId());
            especialidadePacientes.add(especialidadePaciente);
        }
        return especialidadePacientes;
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        List<Paciente> lista = service.listar();
        return ResponseEntity.ok().body(lista);
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
    public ResponseEntity<Object> buscarPacientesAgendados(@PathVariable String inicio, @PathVariable String  fim) {
        List<Paciente> lista = service.buscarPacientesAgendadosNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/triagem/{inicio}/{fim}")
    public ResponseEntity<Object> buscarPacientesTriagemRealizada(@PathVariable String inicio, @PathVariable String fim){
        List<Paciente> lista = service.buscarPacientesAlocadosNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/pendentes")
    public ResponseEntity<Object> buscarPendentesTriagem() {
        List<Paciente> lista = service.buscarPacientesPendentesDeTriagem();
        return ResponseEntity.ok().body(lista);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody PacienteDto registro){

        try {
            mapper = new Mapper();
            Profissao profissao = profissaoService.getByDescricao(registro.getProfissao());
            Escolaridade escolaridade = escolaridadeService.getByDescricao(registro.getEscolaridade());
            Genero genero = generoService.getByDescricao(registro.getGenero());
            List<EspecialidadePaciente> especialidades = preparaListaEspecialidadePaciente(registro.getEspecialidades());
            service.update(mapper.dtoToPaciente(registro, Perfil.ADMIN, profissao, escolaridade, genero, especialidades));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Throwable sqlException = e.getCause();
            String msg = (sqlException!=null) ? sqlException.getCause().getMessage() : e.getMessage();
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), msg, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id)  {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
