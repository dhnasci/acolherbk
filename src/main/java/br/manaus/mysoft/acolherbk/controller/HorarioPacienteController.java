package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.HorarioPaciente;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.dto.HorarioDto;
import br.manaus.mysoft.acolherbk.services.HorarioPacienteService;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static br.manaus.mysoft.acolherbk.utils.Constantes.ACESSO_NAO_AUTORIZADO;

@RestController
@RequestMapping(value = "/horariopaciente")
public class HorarioPacienteController {

    @Autowired
    HorarioPacienteService service;

    Mapper mapper = new Mapper();

    @GetMapping
    public ResponseEntity<Object> listar() {
        try {
            List<HorarioDto> lista = mapper.converteParaHorarioDto(service.listar());
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(403, ACESSO_NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

}
