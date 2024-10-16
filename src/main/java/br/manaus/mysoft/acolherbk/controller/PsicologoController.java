package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.StandardError;
import br.manaus.mysoft.acolherbk.dto.NomePsicologoDto;
import br.manaus.mysoft.acolherbk.dto.PsicologoDto;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.EspecialidadePsicologoService;
import br.manaus.mysoft.acolherbk.services.HorarioPsiService;
import br.manaus.mysoft.acolherbk.services.PsicologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.manaus.mysoft.acolherbk.utils.Mapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.manaus.mysoft.acolherbk.utils.Constantes.NAO_AUTORIZADO;
import static br.manaus.mysoft.acolherbk.utils.Constantes.PSICOLOGO_NAO_ENCONTRADO;

@RestController
@RequestMapping(value = "/psicologo")
public class PsicologoController {

    PsicologoService service;
    HorarioPsiService horarioPsicologoService;
    EspecialidadePsicologoService especialidadePsicologoService;

    @Autowired
    public PsicologoController(PsicologoService service, HorarioPsiService horarioPsicologoService, EspecialidadePsicologoService especialidadePsicologoService) {
        this.service = service;
        this.horarioPsicologoService = horarioPsicologoService;
        this.especialidadePsicologoService = especialidadePsicologoService;
    }

    @PostMapping(value = "/{perfil}")
    public ResponseEntity<Object> inserir(@RequestBody PsicologoDto registro, @PathVariable Perfil perfil) {
        Map<String, Object> response = new HashMap<>();
        if (!perfil.equals(Perfil.PSICOLOGO)) {
            try {
                Psicologo psi = service.inserir(Mapper.toPsicologo(registro));
                response.put("psicologo", psi);
                response.put("senha", service.getNovaSenha());
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(psi.getId()).toUri();
                return ResponseEntity.created(uri).body(response);
            } catch (Exception e) {
                StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), NAO_AUTORIZADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        try {
            List<PsicologoDto> lista = toListaDto(service.listar());
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(value = "/nomes")
    public ResponseEntity<Object> listarNomes() {
        try {
            List<NomePsicologoDto> lista = Mapper.preparaPsicologosNomes(service.listar());
            return ResponseEntity.ok().body(lista);
        } catch (Exception e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private List<PsicologoDto> toListaDto(List<Psicologo> lista) {
        List<PsicologoDto> listaDto = new ArrayList<>();
        for (Psicologo psicologo : lista) {
            PsicologoDto dto = toDto(psicologo);
            listaDto.add(dto);
        }
        return listaDto;
    }

    private PsicologoDto toDto(Psicologo psicologo) {
        PsicologoDto dto = new PsicologoDto();
        dto.setId(psicologo.getId());
        dto.setNomeCompleto(psicologo.getNomeCompleto());
        dto.setCelular1(psicologo.getCelular1());
        dto.setCelular2(psicologo.getCelular2());
        if (psicologo.getIsWhatsapp1() != null) {
            dto.setIsWhatsapp1(psicologo.getIsWhatsapp1().toString());
        } else {
            dto.setIsWhatsapp1("");
        }
        dto.setCrp(psicologo.getCrp());
        if (psicologo.getIsWhatsapp2() != null) {
            dto.setIsWhatsapp2(psicologo.getIsWhatsapp2().toString());
        } else {
            dto.setIsWhatsapp2("");
        }
        dto.setLogin(psicologo.getLogin());
        dto.setEmail(psicologo.getEmail());
        dto.setPerfil(psicologo.getPerfil().name());
        dto.setHorarios(Mapper.preparaHorariosPsicologo(horarioPsicologoService.obterHorariosPsicologo(psicologo)));
        dto.setEspecialidades(Mapper.preparaEspecialidadePsicologo(especialidadePsicologoService.getByPsicologo(psicologo)));
        return dto;
    }

    @GetMapping(value = "/buscarNome/{nome}")
    public ResponseEntity<Object> buscarPeloNome(@PathVariable String nome) {
        List<PsicologoDto> lista = toListaDto(service.buscarPeloNome(nome));
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
            PsicologoDto psicologo = toDto(service.find(id));
            return ResponseEntity.ok().body(psicologo);
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping(value = "/pelonomecompleto/{nome}")
    public ResponseEntity<Object> buscarPeloNomeCompleto(@PathVariable String nome) {
        try {
            PsicologoDto psicologo = toDto(service.obterPeloNomeCompleto(nome));
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
    public ResponseEntity<Object> atualizar(@RequestBody PsicologoDto dto) {
        Psicologo psicologo = Mapper.toPsicologo(dto);
        try {
            service.alterar(psicologo);
            return ResponseEntity.noContent().build();
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
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
            response.put("login", psicologo.getLogin());
            response.put("senha", service.getNovaSenha());
            return ResponseEntity.ok(response);
        } catch (ObjetoException e) {
            StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), PSICOLOGO_NAO_ENCONTRADO, System.currentTimeMillis());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
