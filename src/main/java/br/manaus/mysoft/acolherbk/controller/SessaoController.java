package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.dto.SessaoDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.exceptions.SessaoException;
import br.manaus.mysoft.acolherbk.services.PacienteService;
import br.manaus.mysoft.acolherbk.services.PsicologoService;
import br.manaus.mysoft.acolherbk.services.SessaoService;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/sessao")
public class SessaoController {

    private static final String SEM_PERMISSAO = "Não tem permisssão para agendar sessão";
    private static final String NAO_EXISTE_A_SESSAO = "Não existe a sessão selecionada!";

    SessaoService service;
    PsicologoService psicologoService;
    PacienteService pacienteService;

    @Autowired
    public SessaoController(SessaoService service, PsicologoService psicologoService, PacienteService pacienteService) {
        this.service = service;
        this.psicologoService = psicologoService;
        this.pacienteService = pacienteService;
    }

    Mapper mapper = new Mapper();

    @PostMapping(value = "/{perfil}")
    public ResponseEntity<Object> criarSessao(@RequestBody SessaoDto sessaoDto, @PathVariable Perfil perfil) {
        if (perfil.equals(Perfil.PSICOLOGO)) {

            try {
                Psicologo psicologo = psicologoService.find(sessaoDto.getIdPsicologo());
                Paciente paciente = pacienteService.find(sessaoDto.getIdPaciente());
                Sessao sessao1 = service.insert(mapper.dtoToSessao(sessaoDto, psicologo, paciente ));
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sessao1.getId()).toUri();
                return ResponseEntity.created(uri).body(sessao1);
            } catch (Exception e) {
                StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), SEM_PERMISSAO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/{idPaciente}/{idPsicologo}")
    public ResponseEntity<Object> listar(@PathVariable Integer idPaciente, @PathVariable Integer idPsicologo) {
        try {
            List<SessaoDto> lista = service.listarSessoes(idPaciente, idPsicologo);
            return ResponseEntity.ok().body(lista);
        } catch (SessaoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/obterUltima/{idPaciente}/{idPsicologo}")
    public ResponseEntity<Object> obterUltimaSessao(@PathVariable Integer idPaciente, @PathVariable Integer idPsicologo) {
        try {
            Integer nroUltimaSessao = service.obterUltimaSessao(idPaciente, idPsicologo);
            nroUltimaSessao = (nroUltimaSessao==null) ? 0: nroUltimaSessao;
            return ResponseEntity.ok().body(nroUltimaSessao);
        } catch (SessaoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/cancelar/{id}")
    public ResponseEntity<Object> cancelar(@PathVariable Integer id) {
        Optional<Sessao> optionalSessao = service.getById(id);
        if (optionalSessao.isPresent()) {
            Sessao sessao = optionalSessao.get();
            sessao.setIsCancelado(true);
            try {
                Sessao resposta = service.alterar(sessao);
                return ResponseEntity.ok().body(resposta);
            } catch (Exception e) {
                StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), NAO_EXISTE_A_SESSAO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping(value = "/concluir/{id}")
    public ResponseEntity<Object> concluir(@PathVariable Integer id) {
        Optional<Sessao> optionalSessao = service.getById(id);
        if (optionalSessao.isPresent()) {
            Sessao sessao = optionalSessao.get();
            sessao.setIsPacienteAtendido(true);
            try {
                Sessao resposta = service.alterar(sessao);
                return ResponseEntity.ok().body(resposta);
            } catch (Exception e) {
                StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), NAO_EXISTE_A_SESSAO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }
}
