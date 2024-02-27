package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Profissao;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.ProfissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissaoService {

    @Autowired
    ProfissaoRepository repository;

    public Profissao insert(Profissao obj) {
        return repository.save(obj);
    }

    public Profissao find(Integer id) throws ObjetoException {
        Optional<Profissao> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Profissao nao encontrada id: "
                + id + "!")
        );
    }

    public List<Profissao> listar() {
        return repository.findAll();
    }
    public Profissao getByDescricao(String descricao) {
        return repository.findByDescricao(descricao);
    }

    public Profissao update(Profissao obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Profissao");
        }
    }

    public void salvar(List<Profissao> lista) {
        repository.saveAll(lista);
    }
}
