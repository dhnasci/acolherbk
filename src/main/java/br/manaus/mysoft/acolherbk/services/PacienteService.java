package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoDto;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoProjection;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository repository;

    public Paciente insert(Paciente obj) {
        return repository.save(obj);
    }

    public Paciente find(Integer id) throws ObjetoException {
        Optional<Paciente> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Paciente nao encontrada id: "
                + id + "!")
        );
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public List<Paciente> buscarPorNome(String nome) {
        return repository.findPacientesByNomeCompletoContainingIgnoreCase(nome);
    }

    public List<Paciente> buscarPacientesAgendadosNoIntervalo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findPacientesBySessoesDiaAgendadoBetween(inicio, fim);
    }

    public List<Paciente> buscarPacientesAlocadosNoIntervalo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findPacientesByTriagemsDiaAlocacaoBetween(inicio, fim);
    }
    public List<Paciente> buscarPacientesPendentesDeTriagem() {
        return repository.findPacientesByTriagemsIsPacienteAlocado(false);
    }

    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologo(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocados(id);
        List<PacienteAlocadoDto> dtos = projections.stream()
                .map(projection -> PacienteAlocadoDto.builder()
                        .nomeCompleto(projection.getNomeCompleto())
                        .celular1(projection.getCelular1())
                        .isWhatsapp1(projection.getIsWhatsapp1().toString())
                        .celular2(projection.getCelular2())
                        .registroGeral(projection.getRegistroGeral())
                        .build())
                .collect(Collectors.toList());
        return dtos;
    }

    public Paciente update(Paciente obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Paciente");
        }
    }
}
