package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static br.manaus.mysoft.acolherbk.utils.Constantes.NAO_AUTORIZADO;
import static br.manaus.mysoft.acolherbk.utils.Constantes.TOKEN;

@RestController
@RequestMapping(value = "/pacientes")
public class PacienteController {

    @Autowired
    PacienteService service;

    @PostMapping(value = "/{token}")
    public ResponseEntity<Object> inserir(@RequestBody Paciente registro, @PathVariable String token) {
        if (token.equals(TOKEN)) {
            Paciente reg = service.insert(registro);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registro.getId()).toUri();
            return ResponseEntity.created(uri).body(reg);
        } else {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }


}
