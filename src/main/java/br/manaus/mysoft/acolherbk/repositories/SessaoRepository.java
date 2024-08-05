package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
    @Transactional(readOnly = true)
    List<Sessao> findAllByPsicologoAndByPaciente(Psicologo psicologo, Paciente paciente);
}
