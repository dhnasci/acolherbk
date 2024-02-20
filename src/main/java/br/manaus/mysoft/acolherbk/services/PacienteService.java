package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return repository.findAll();
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
