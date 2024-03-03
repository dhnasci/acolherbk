package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.EspecialidadePsicologo;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EspecialidadePsicologoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadePsicologoService {

    @Autowired
    EspecialidadePsicologoRepository repository;

    public EspecialidadePsicologo insert(EspecialidadePsicologo obj) {
        return repository.save(obj);
    }

    public EspecialidadePsicologo find(Integer id) throws ObjetoException {
        Optional<EspecialidadePsicologo> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("EspecialidadePsicologo nao encontrada id: "
                + id + "!")
        );
    }

    public List<EspecialidadePsicologo> listar() {
        return repository.findAll();
    }

    public List<EspecialidadePsicologo> getByPsicologo(Psicologo psicologo) {
        return repository.findAllByPsicologo(psicologo);
    }

    public EspecialidadePsicologo update(EspecialidadePsicologo obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar EspecialidadePsicologo");
        }
    }

    public void salvar(List<EspecialidadePsicologo> lista) {
        repository.saveAll(lista);
    }
}
