package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Genero;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.services.GeneroService;
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
@RequestMapping(value = "/genero")
public class GeneroController {

    @Autowired
    GeneroService service;

    @GetMapping(value = "/{token}")
    public ResponseEntity<Object> listar(@PathVariable String token) {
        if (token.equals(TOKEN)) {
            List<Genero> lista = service.listar();
            return ResponseEntity.ok().body(lista);
        } else {
            StandardError error = new StandardError(403, ACESSO_NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
