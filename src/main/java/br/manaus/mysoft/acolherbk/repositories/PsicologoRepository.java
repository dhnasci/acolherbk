package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PsicologoRepository extends JpaRepository<Psicologo, Integer> {

    @Transactional(readOnly = true)
    Psicologo findPsicologoByLogin(String login);

    @Transactional(readOnly = true)
    List<Psicologo> findPsicologosBySessoesDiaAgendadoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Transactional(readOnly = true)
    List<Psicologo> findPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto(String nome);
}

