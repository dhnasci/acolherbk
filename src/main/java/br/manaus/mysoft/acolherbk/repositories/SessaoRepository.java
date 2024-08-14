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

    @Transactional(readOnly = true)
    @Query(value = "SELECT\n" +
            "    numero_sessao as \"numeroSessao\"\n" +
            "     FROM sessao\n" +
            "WHERE paciente_id = ?1 and psicologo_id = ?2 " +
            "ORDER BY numero_sessao DESC\n" +
            "LIMIT 1", nativeQuery = true)
    Integer findLastSection(Integer idPaciente, Integer idPsicologo);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COUNT(*) AS TOTAL FROM sessao\n" +
            "WHERE paciente_id = ?1 AND psicologo_id = ?2 AND\n" +
            "      is_paciente_atendido = true"
    , nativeQuery = true)
    Integer getTotalSessoesConcluidas(Integer idPaciente, Integer idPsicologo);
}
