package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Especialidade;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadeService {

    @Autowired
    EspecialidadeRepository repository;

    public Especialidade insert(Especialidade obj) {
        return repository.save(obj);
    }

    public Especialidade find(Integer id) throws ObjetoException {
        Optional<Especialidade> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Especialidade nao encontrada id: "
                + id + "!")
        );
    }

    public List<Especialidade> listar() {
        return repository.findAll();
    }

    public Especialidade getByDescricao(String descricao) {
        return repository.findByDescricao(descricao);
    }

    public Especialidade update(Especialidade obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Especialidade");
        }
    }

    public void salvar(List<Especialidade> lista) {
        repository.saveAll(lista);
    }
}
