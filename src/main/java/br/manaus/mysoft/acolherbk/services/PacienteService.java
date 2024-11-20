package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.*;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import br.manaus.mysoft.acolherbk.utils.Mapper;
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

    Mapper mapper = new Mapper();

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

    public List<PacienteAlocadoDto> listarTodosPacientes() {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientes();
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesPendentes() {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesPendentes();
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesAtendidos() {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAtendidos();
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesEmAtendimento() {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesEmAtendimento();
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesCancelados() {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesCancelados();
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesEmTriagem() {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesEmTriagem();
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public ChartDto obterStatusAtendimentoParaGrafico() {
        List<StatusAtendimentoProjection> projections = repository.getAllStatusAtendimento();
        return mapper.fromStatusAtendimentoToGrafico(projections);
    }

    public ChartDto obterDistribuicaoFaixaEtariaParaGrafico() {
        List<FaixaEtariaDistribuicaoProjection> projections = repository.getAllDistribuicaoFaixaEtaria();
        return mapper.fromDistribuicaoFaixaEtariaToGrafico(projections);
    }


    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologo(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocados(id);
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    private static List<PacienteAlocadoDto> getPacienteAlocadoDtos(List<PacienteAlocadoProjection> projections) {
        List<PacienteAlocadoDto> dtos = projections.stream()
                .map(projection -> PacienteAlocadoDto.builder()
                        .nomeCompleto(projection.getNomeCompleto())
                        .status(projection.getStatus())
                        .celular1(projection.getCelular1())
                        .isWhatsapp1(projection.getIsWhatsapp1().toString())
                        .celular2(projection.getCelular2())
                        .registroGeral(projection.getRegistroGeral())
                        .idPaciente(projection.getIdPaciente())
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
