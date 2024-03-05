package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Genero;
import br.manaus.mysoft.acolherbk.domain.Triagem;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.GeneroRepository;
import br.manaus.mysoft.acolherbk.repositories.TriagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TriagemService {

    @Autowired
    TriagemRepository repository;

    public Triagem insert(Triagem obj) {
        return repository.save(obj);
    }

    public Triagem find(Integer id) throws ObjetoException {
        Optional<Triagem> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Triagem nao encontrada id: "
                + id + "!")
        );
    }

    public List<Triagem> listar() {
        return repository.findAll();
    }

    public Triagem update(Triagem obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Triagem");
        }
    }

    public void salvar(List<Triagem> lista) {
        repository.saveAll(lista);
    }
}
