package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Genero;
import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.enums.Turno;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.GeneroRepository;
import br.manaus.mysoft.acolherbk.repositories.HorarioRepository;
import br.manaus.mysoft.acolherbk.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static br.manaus.mysoft.acolherbk.utils.Constantes.PIPE;

@Service
public class HorarioService {


    @Autowired
    HorarioRepository repository;

    Mapper mapper = new Mapper();

    public Horario insert(Horario obj) {
        return repository.save(obj);
    }

    public Horario find(Integer id) throws ObjetoException {
        Optional<Horario> obj = repository.findById(id);
        return obj.orElseThrow( () -> new ObjetoException("Horario nao encontrada id: "
                + id + "!")
        );
    }

    public List<Horario> listar() {
        return repository.findAll();
    }

    public Horario update(Horario obj) {
        return repository.save(obj);
    }

    public void delete(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException("Erro ao apagar Horario");
        }
    }

    public void salvar(List<Horario> lista) {
        repository.saveAll(lista);
    }

    public Horario getByDescricao(String horario) {
        String[] lista = horario.split(PIPE);
        DayOfWeek dia = mapper.diaDaSemana.get(lista[0]);
        Turno turno = Turno.valueOf(lista[1].trim());
        return repository.findByDiaAndTurno(dia, turno);
    }
}
