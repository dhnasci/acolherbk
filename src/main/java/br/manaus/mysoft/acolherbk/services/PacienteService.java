package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return repository.findPacientesByTriagemsIsPacienteAlocado(true);
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
