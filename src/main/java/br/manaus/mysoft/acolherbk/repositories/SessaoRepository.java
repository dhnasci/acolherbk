package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.dto.SessaoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT\n" +
            "    numero_sessao as \"numeroSessao\",\n" +
            "    id,\n" +
            "    is_cancelado as \"isCancelado\",\n" +
            "    is_paciente_atendido as \"isAtendido\",\n" +
            "    dia_agendado as \"diaAgendado\"\n" +
            "     FROM sessao\n" +
            "WHERE paciente_id = ?1 and psicologo_id = ?2", nativeQuery = true)
    List<SessaoProjection> findAllSessoesAlocadas(Integer idPaciente, Integer idPsicologo );
}
