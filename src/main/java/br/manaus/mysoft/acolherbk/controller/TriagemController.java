package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.domain.Triagem;
import br.manaus.mysoft.acolherbk.dto.TriagemDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.services.PacienteService;
import br.manaus.mysoft.acolherbk.services.PsicologoService;
import br.manaus.mysoft.acolherbk.services.TriagemService;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static br.manaus.mysoft.acolherbk.utils.Constantes.INFORMACOES_INSUFICIENTES;
import static br.manaus.mysoft.acolherbk.utils.Constantes.NAO_AUTORIZADO;

@RestController
@RequestMapping(value = "/triagem")
public class TriagemController {
    @Autowired
    TriagemService triagemService;
    @Autowired
    PsicologoService psicologoService;
    @Autowired
    PacienteService pacienteService;

    @PostMapping(value = "/{perfil}")
    public ResponseEntity<Object> inserir(@RequestBody TriagemDto triagemDto, @PathVariable Perfil perfil) {

        if (perfil.equals(Perfil.TRIAGEM) || perfil.equals(Perfil.ADMIN)) {
            try {
                Triagem original = Mapper.toTriagem(triagemDto);
                if (triagemDto.getLogin().equals("") || triagemDto.getPacienteId().equals("")) {
                    StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), INFORMACOES_INSUFICIENTES, System.currentTimeMillis());
                    return ResponseEntity.badRequest().body(error);
                }
                original.setPaciente(pacienteService.find(Integer.parseInt(triagemDto.getPacienteId())));
                original.setPsicologo(psicologoService.buscarPeloLogin(triagemDto.getLogin()));
                Triagem triagem = triagemService.insert(original);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(triagem.getId()).toUri();
                return ResponseEntity.created(uri).body(triagem);
            } catch (Exception e) {
                StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            StandardError error = new StandardError(HttpStatus.UNAUTHORIZED.value(), NAO_AUTORIZADO , System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }
}
