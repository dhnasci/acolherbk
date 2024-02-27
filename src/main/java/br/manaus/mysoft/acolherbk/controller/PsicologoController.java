package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.PsicologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.manaus.mysoft.acolherbk.utils.Mapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.manaus.mysoft.acolherbk.utils.Constantes.NAO_AUTORIZADO;
import static br.manaus.mysoft.acolherbk.utils.Constantes.PSICOLOGO_NAO_ENCONTRADO;

@RestController
@RequestMapping(value = "/psicologo")
public class PsicologoController {

    @Autowired
    PsicologoService service;

    @PostMapping
    public ResponseEntity<Object> inserir(@RequestBody Psicologo registro) {

        try {
            Psicologo reg = service.inserir(registro);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(registro.getId()).toUri();
            return ResponseEntity.created(uri).body(reg);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        List<Psicologo> lista = service.listar();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/buscarNome/{nome}")
    public ResponseEntity<Object> buscarPeloNome(@PathVariable String nome) {
        List<Psicologo> lista = service.buscarPeloNome(nome);
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/login/{nome}")
    public ResponseEntity<Object> buscarPeloLogin(@PathVariable String nome) {
        try {
            Psicologo psicologo = service.buscarPeloLogin(nome);
            return ResponseEntity.ok().body(psicologo);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), PSICOLOGO_NAO_ENCONTRADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Integer id) {
        try {
            Psicologo psicologo = service.find(id);
            return ResponseEntity.ok().body(psicologo);
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping(value = "/{inicio}/{fim}")
    public ResponseEntity<Object> buscarPorSessaoIntervalo(@PathVariable String inicio, @PathVariable String fim) {
        List<Psicologo> lista = service.listarComSessoesAgendadasNoIntervalo(Mapper.converteParaData(inicio), Mapper.converteParaData(fim));
        return ResponseEntity.ok().body(lista);
    }

    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody Psicologo obj) {
        service.alterar(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id)  {
        try {
            service.apagar(id);
            return ResponseEntity.noContent().build();
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/reset/{id}")
    public ResponseEntity<Object> reset(@PathVariable Integer id) {
        try {
            Psicologo psicologo = service.find(id);
            Map<String, Object> response = new HashMap<>();
            service.reset(psicologo);
            response.put("senha", service.getNovaSenha());
            return ResponseEntity.ok(response);
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), PSICOLOGO_NAO_ENCONTRADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
