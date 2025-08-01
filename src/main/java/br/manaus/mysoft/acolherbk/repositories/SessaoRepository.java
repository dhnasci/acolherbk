package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
    
    @Transactional(readOnly = true)
    List<Sessao> findSessoesByDiaAgendadoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Transactional(readOnly = true)
    List<Sessao> findSessoesByLoginPsicologo(String loginPsicologo);
    
    @Transactional(readOnly = true)
    List<Sessao> findSessoesByIsCancelado(Boolean isCancelado);
    
    @Transactional(readOnly = true)
    List<Sessao> findSessoesByIsPacienteAtendido(Boolean isPacienteAtendido);
}