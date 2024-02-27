package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Genero;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroService {

    @Autowired
    GeneroRepository repository;

    public Genero insert(Genero obj) {
        return repository.save(obj);
    }

    public Genero find(Integer id) throws ObjetoException {
        Optional<Genero> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Genero nao encontrada id: "
                + id + "!")
        );
    }

    public List<Genero> listar() {
        return repository.findAll();
    }
    public Genero getByDescricao(String descricao) {
        return repository.findByDescricao(descricao);
    }

    public Genero update(Genero obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Genero");
        }
    }

    public void salvar(List<Genero> lista) {
        repository.saveAll(lista);
    }
}
