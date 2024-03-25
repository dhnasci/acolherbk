package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.domain.HorarioPsi;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.dto.HorarioDto;
import br.manaus.mysoft.acolherbk.dto.HorarioPsicologoForm;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.HorarioPsiService;
import br.manaus.mysoft.acolherbk.services.HorarioService;
import br.manaus.mysoft.acolherbk.services.PsicologoService;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.manaus.mysoft.acolherbk.utils.Constantes.ACESSO_NAO_AUTORIZADO;

@RestController
@RequestMapping(value = "/horariopsicologo")
public class HorarioPsicologoController {

    @Autowired
    HorarioPsiService service;
    @Autowired
    HorarioService horarioService;
    @Autowired
    PsicologoService psicologoService;

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

    @PostMapping
    public ResponseEntity<Object> inserir(@RequestBody HorarioPsicologoForm horarioForm) {
        try {
            Horario horario = horarioService.getByDescricao(horarioForm.getHorario());
            Psicologo psicologo = psicologoService.find(horarioForm.getPsicologoId());
            HorarioPsi obj = new HorarioPsi(null, horario, psicologo);
            HorarioPsi horarioPsi = service.insert(obj);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(horarioPsi.getId()).toUri();
            return ResponseEntity.created(uri).body(horarioPsi);
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
