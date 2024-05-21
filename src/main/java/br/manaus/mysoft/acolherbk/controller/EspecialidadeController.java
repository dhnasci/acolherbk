package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Especialidade;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.services.EspecialidadeService;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static br.manaus.mysoft.acolherbk.utils.Constantes.ACESSO_NAO_AUTORIZADO;
import static br.manaus.mysoft.acolherbk.utils.Constantes.TOKEN;

@RestController
@RequestMapping(value = "/especialidade")
public class EspecialidadeController {

    EspecialidadeService service;

    @Autowired
    public EspecialidadeController(EspecialidadeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        try {
            List<Especialidade> lista = service.listar();
            return ResponseEntity.ok().body(Mapper.preparaEspecialidade(lista));
        } catch (Exception e) {
            StandardError error = new StandardError(403, ACESSO_NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
