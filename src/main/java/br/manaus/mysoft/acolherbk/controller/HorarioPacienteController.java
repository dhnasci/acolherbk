package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.dto.HorarioDto;
import br.manaus.mysoft.acolherbk.dto.HorarioPacienteForm;
import br.manaus.mysoft.acolherbk.dto.HorarioPsicologoForm;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.HorarioPacienteService;
import br.manaus.mysoft.acolherbk.services.HorarioService;
import br.manaus.mysoft.acolherbk.services.PacienteService;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static br.manaus.mysoft.acolherbk.utils.Constantes.ACESSO_NAO_AUTORIZADO;

@RestController
@RequestMapping(value = "/horariopaciente")
public class HorarioPacienteController {

    @Autowired
    HorarioPacienteService service;
    @Autowired
    HorarioService horarioService;
    @Autowired
    PacienteService pacienteService;

    Mapper mapper = new Mapper();

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> listar(@PathVariable Integer id) {
        try {
            List<HorarioDto> lista = mapper.converteParaHorarioDto(service.listar(id));
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(403, ACESSO_NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @PostMapping(value="/perfil")
    public ResponseEntity<Object> inserir(@RequestBody HorarioPacienteForm horarioForm, @PathVariable Perfil perfil) {
        try {
            Horario horario = horarioService.getByDescricao(horarioForm.getHorario());
            Paciente paciente = pacienteService.find(horarioForm.getPacienteId());
            HorarioPaciente obj = new HorarioPaciente(null, horario, paciente);
            HorarioPaciente horarioPaciente = service.insert(obj);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(horarioPaciente.getId()).toUri();
            return ResponseEntity.created(uri).body(horarioPaciente);
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
