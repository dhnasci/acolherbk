package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.HorarioPaciente;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.HorarioPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioPacienteService {

    @Autowired
    HorarioPacienteRepository repository;

    public HorarioPaciente insert(HorarioPaciente obj) {
        return repository.save(obj);
    }

    public HorarioPaciente find(Integer id) throws ObjetoException {
        Optional<HorarioPaciente> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("HorarioPaciente nao encontrada id: "
                + id + "!")
        );
    }

    public List<Object[]> listar() {
        return repository.obterInfoPacienteHorario();
    }

    public List<HorarioPaciente> obterHorariosPaciente(Paciente paciente) {
        return repository.findAllByPaciente(paciente);
    }

    public HorarioPaciente update(HorarioPaciente obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar HorarioPaciente");
        }
    }

    public void salvar(List<HorarioPaciente> lista) {
        repository.saveAll(lista);
    }


}
