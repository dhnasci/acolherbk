package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.dto.*;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.enums.Status;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository repository;

    @Autowired
    PsicologoService psicologoService;

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

    public List<Paciente> listar(Empresa empresa) {
        return repository.findPacientesByEmpresa(empresa);
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

    public List<PacienteAlocadoDto> listarTodosPacientes(Integer idEmpresa) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientes(idEmpresa);
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesPendentes(Integer idEmpresa) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesPorStatus(idEmpresa, Status.PENDENTE.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesAtendidos(Integer idEmpresa) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesPorStatus(idEmpresa, Status.ATENDIDO.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesEmAtendimento(Integer idEmpresa) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesPorStatus(idEmpresa, Status.EM_ATENDIMENTO.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesCancelados(Integer idEmpresa) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesPorStatus(idEmpresa, Status.CANCELADO.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> listarTodosPacientesEmTriagem(Integer idEmpresa) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesPorStatus(idEmpresa, Status.TRIAGEM.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public ChartDto obterStatusAtendimentoParaGrafico(Integer ano, Empresa empresa ) {
        List<StatusAtendimentoProjection> projections = repository.getAllStatusAtendimento(ano, empresa.getId() );
        return mapper.fromStatusAtendimentoToGrafico(projections);
    }

    public ChartDto obterDistribuicaoFaixaEtariaParaGrafico(Integer ano, Empresa empresa) {
        List<FaixaEtariaDistribuicaoProjection> projections = repository.getAllDistribuicaoFaixaEtaria(ano, empresa.getId());
        return mapper.fromDistribuicaoFaixaEtariaToGrafico(projections);
    }


    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologo(Integer id) {
        List<PacienteAlocadoProjection> triagem = repository.findAllPacientesAlocadosPorStatus(id, Status.TRIAGEM.getNome());
        List<PacienteAlocadoProjection> emAtendimento = repository.findAllPacientesAlocadosPorStatus(id, Status.EM_ATENDIMENTO.getNome());
        List<PacienteAlocadoProjection> projections = new ArrayList<>();
        adiciona(triagem, projections);
        adiciona(emAtendimento, projections);
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    private static void adiciona(List<PacienteAlocadoProjection> triagem, List<PacienteAlocadoProjection> projections) {
        for (PacienteAlocadoProjection proj : triagem) {
            projections.add(proj);
        }
    }

    public List<PacienteAlocadoDto> buscarTodosPacientesAlocadosAoPsicologo(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocados(id);
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologoEmTriagem(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocadosPorStatus(id, Status.TRIAGEM.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologoEmAtendimento(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocadosPorStatus(id, Status.EM_ATENDIMENTO.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologoAtendidos(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocadosPorStatus(id, Status.ATENDIDO.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    public List<PacienteAlocadoDto> buscarPacientesAlocadosAoPsicologoCancelados(Integer id) {
        List<PacienteAlocadoProjection> projections = repository.findAllPacientesAlocadosPorStatus(id, Status.CANCELADO.getNome());
        List<PacienteAlocadoDto> dtos = getPacienteAlocadoDtos(projections);
        return dtos;
    }

    private static List<PacienteAlocadoDto> getPacienteAlocadoDtos(List<PacienteAlocadoProjection> projections) {
        return projections.stream()
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

    public TotalDto obterTotais(Integer ano, Empresa empresa) {
        String numPacientes = String.valueOf(filtrarPacientesPorAno(listar(empresa),ano).size());
        String numPsis = String.valueOf(filtrarPsicologosPorPerfil(psicologoService.listar(empresa)).size());
        return new TotalDto(  numPacientes, numPsis);
    }

    public List<Paciente> filtrarPacientesPorAno(List<Paciente> pacientes, Integer ano) {
        return pacientes.stream()
                .filter(paciente -> paciente.getCadastro() != null && paciente.getCadastro().getYear() == ano)
                .collect(Collectors.toList());
    }

    public List<Psicologo> filtrarPsicologosPorPerfil(List<Psicologo> psicologos) {
        return psicologos.stream()
                .filter(psicologo -> psicologo.getPerfil() == Perfil.PSICOLOGO)
                .collect(Collectors.toList());
    }

}
