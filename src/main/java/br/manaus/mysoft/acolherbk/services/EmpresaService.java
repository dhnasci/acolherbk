package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {
    
    @Autowired
    EmpresaRepository repository;

    public Empresa insert(Empresa obj) {
        return repository.save(obj);
    }

    public Empresa find(Integer id) throws ObjetoException {
        Optional<Empresa> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Empresa nao encontrada id: "
                + id + "!")
        );
    }

    public List<Empresa> listar() {
        return repository.findAll();
    }

    public Empresa update(Empresa obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Empresa");
        }
    }

    public void salvar(List<Empresa> lista) {
        repository.saveAll(lista);
    }
}
