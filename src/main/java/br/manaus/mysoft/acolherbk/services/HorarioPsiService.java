package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.domain.HorarioPsi;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.HorarioPsiRepository;
import br.manaus.mysoft.acolherbk.repositories.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioPsiService {

    @Autowired
    HorarioPsiRepository repository;

    public HorarioPsi insert(HorarioPsi obj) {
        return repository.save(obj);
    }

    public HorarioPsi find(Integer id) throws ObjetoException {
        Optional<HorarioPsi> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("HorarioPsi nao encontrada id: "
                + id + "!")
        );
    }

    public List<HorarioPsi> listar() {
        return repository.findAll();
    }

    public List<HorarioPsi> obterHorariosPsicologo(Psicologo psicologo) {
        return repository.findAllByPsicologo(psicologo);
    }

    public HorarioPsi update(HorarioPsi obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar HorarioPsi");
        }
    }

    public void salvar(List<HorarioPsi> lista) {
        repository.saveAll(lista);
    }
}
