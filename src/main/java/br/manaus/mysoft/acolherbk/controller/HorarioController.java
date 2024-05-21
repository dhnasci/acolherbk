package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.services.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import static br.manaus.mysoft.acolherbk.utils.Constantes.*;

import java.util.List;


@RestController
@RequestMapping(value = "/horario")
public class HorarioController {

    HorarioService service;

    @Autowired
    public HorarioController(HorarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        try {
            List<Horario> lista = service.listar();
            return ResponseEntity.ok().body(Mapper.preparaHorarios(lista));
        } catch (Exception e) {
            StandardError error = new StandardError(403, ACESSO_NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }


}
