package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.dto.SessaoDto;
import br.manaus.mysoft.acolherbk.dto.SessaoProjection;
import br.manaus.mysoft.acolherbk.exceptions.SessaoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import br.manaus.mysoft.acolherbk.repositories.SessaoRepository;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessaoService {

    private static final String STATUS_PENDENTE = "PENDENTE";
    private static final String STATUS_CONCLUIDA = "CONCLUIDA";
    private static final String STATUS_CANCELADA = "CANCELADA";

    SessaoRepository repository;
    PacienteRepository pacienteRepository;
    PsicologoRepository psicologoRepository;
    Mapper mapper;

    @Autowired
    public SessaoService(SessaoRepository repository, PacienteRepository pacienteRepository, PsicologoRepository psicologoRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.psicologoRepository = psicologoRepository;
    }

    public Sessao insert(Sessao sessao) {
        return repository.save(sessao);
    }

    public List<SessaoDto> listarSessoes(Integer idPaciente, Integer idPsicologo) throws SessaoException {
        List<SessaoProjection> projections = repository.findAllSessoesAlocadas(idPaciente, idPsicologo);
        return projections.stream()
                .map(
                        proj -> SessaoDto.builder()
                                .id( proj.getId())
                                .dia( extraiDiaBD(proj.getDiaAgendado()) )
                                .hora( extraiHoraBD(proj.getDiaAgendado()))
                                .numeroSessao( proj.getNumeroSessao())
                                .status( defineStatusSessao(proj.getIsAtendido(), proj.getIsCancelado()))
                                .idPaciente(idPaciente)
                                .idPsicologo(idPsicologo)
                                .build()
                ).collect(Collectors.toList());
    }

    private String defineStatusSessao(Boolean isAtendido, Boolean isCancelado) {
        if (isCancelado == null) {
            if (isAtendido == null) {
                return STATUS_PENDENTE;
            } else {
                return STATUS_CONCLUIDA;
            }
        } else {
            return STATUS_CANCELADA;
        }
    }

    protected String extraiHora(String diaAgendado) {
        LocalDateTime data = mapper.converteParaData(diaAgendado);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        return data.toLocalTime().format(formatoHora);
    }

    protected String extraiDia(String diaAgendado) {
        LocalDateTime data = mapper.converteParaData(diaAgendado);
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd-MM-yy");
        return data.toLocalDate().format(formatoDia);
    }

    protected String extraiHoraBD(String diaAgendado) {
        LocalDateTime data = mapper.converteParaDataBD(diaAgendado);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        return data.toLocalTime().format(formatoHora);
    }

    protected String extraiDiaBD(String diaAgendado) {
        LocalDateTime data = mapper.converteParaDataBD(diaAgendado);
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd-MM-yy");
        return data.toLocalDate().format(formatoDia);
    }

    public Sessao alterar(Sessao sessao) {
        return repository.save(sessao);
    }

    public Optional<Sessao> getById(Integer id) {
        return repository.findById(id);
    }

    public Integer obterUltimaSessao(Integer idPaciente, Integer idPsicologo) throws SessaoException {
        return repository.findLastSection(idPaciente, idPsicologo);
    }

    public Integer obterTotalConcluidas(Integer idPaciente, Integer idPsicologo) throws SessaoException {
        return repository.getTotalSessoesConcluidas(idPaciente, idPsicologo);
    }
}
