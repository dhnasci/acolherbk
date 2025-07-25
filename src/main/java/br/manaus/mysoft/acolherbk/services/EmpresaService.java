package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService implements EmpresaServices {

    @Autowired
    EmpresaRepository repository;


    @Override
    public Empresa inserir(Empresa empresa) {
        return repository.save(empresa);
    }

    @Override
    public Empresa acharPeloId(Integer id) throws ObjetoException {
        Optional<Empresa> emp = repository.findById(id);
        return emp.orElseThrow( () -> new ObjetoException(String.format("Empresa id %s não encontrada!",id)));
    }

    @Override
    public List<Empresa> listarEmpresas() {
        return repository.findAll();
    }

    @Override
    public Empresa atualizar(Empresa empresa) {
        return repository.save(empresa);
    }

    @Override
    public void apagarPeloId(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Empresa");
        }
    }
}
