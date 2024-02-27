package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Escolaridade;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EscolaridadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EscolaridadeService {

    @Autowired
    EscolaridadeRepository repository;

    public Escolaridade insert(Escolaridade obj) {
        return repository.save(obj);
    }

    public Escolaridade find(Integer id) throws ObjetoException {
        Optional<Escolaridade> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Escolaridade nao encontrada id: "
                + id + "!")
        );
    }

    public List<Escolaridade> listar() {
        return repository.findAll();
    }
    public Escolaridade getByDescricao(String descricao) {
        return repository.findByDescricao(descricao);
    }

    public Escolaridade update(Escolaridade obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Escolaridade");
        }
    }


    public void salvar(List<Escolaridade> lista) {
        repository.saveAll(lista);
    }
}
