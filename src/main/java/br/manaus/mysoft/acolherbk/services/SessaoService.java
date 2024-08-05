package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.exceptions.SessaoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import br.manaus.mysoft.acolherbk.repositories.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessaoService {

    SessaoRepository repository;
    PacienteRepository pacienteRepository;
    PsicologoRepository psicologoRepository;

    @Autowired
    public SessaoService(SessaoRepository repository, PacienteRepository pacienteRepository, PsicologoRepository psicologoRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.psicologoRepository = psicologoRepository;
    }

    public Sessao insert(Sessao sessao) {
        return repository.save(sessao);

    }

    public List<Sessao> listarSessoes(Integer idPaciente, Integer idPsicologo) throws SessaoException {
        Optional<Psicologo> psicologo = psicologoRepository.findById(idPsicologo);
        Optional<Paciente> paciente = pacienteRepository.findById(idPaciente);
        if (psicologo.isEmpty()){
            throw new SessaoException("Este psicologo não tem sessão!");
        }
        if (paciente.isEmpty()){
            throw new SessaoException("Este paciente não tem sessão!");
        }
        return repository.findAllByPsicologoAndByPaciente(psicologo.get(), paciente.get());
    }

    public Sessao alterar(Sessao sessao) {
        return repository.save(sessao);
    }

    public Optional<Sessao> getById(Integer id) {
        return repository.findById(id);
    }

}
