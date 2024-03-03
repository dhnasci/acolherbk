package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.EspecialidadePaciente;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EspecialidadePacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadePacienteService {

    @Autowired
    EspecialidadePacienteRepository repository;

    public EspecialidadePaciente insert(EspecialidadePaciente obj) {
        return repository.save(obj);
    }

    public EspecialidadePaciente find(Integer id) throws ObjetoException {
        Optional<EspecialidadePaciente> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("EspecialidadePaciente nao encontrada id: "
                + id + "!")
        );
    }

    public List<EspecialidadePaciente> listar() {
        return repository.findAll();
    }

    public List<EspecialidadePaciente> obterEspecialidadesPorPaciente(Paciente paciente) {
        return repository.findAllByPaciente(paciente);
    }

    public EspecialidadePaciente update(EspecialidadePaciente obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar EspecialidadePaciente");
        }
    }

    public void salvar(List<EspecialidadePaciente> lista) {
        repository.saveAll(lista);
    }
}
