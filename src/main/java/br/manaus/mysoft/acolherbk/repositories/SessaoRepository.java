package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.dto.RelatorioAnaliticoSessao;
import br.manaus.mysoft.acolherbk.dto.RelatorioAnaliticoSessaoProjection;
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

    @Transactional(readOnly = true)
    @Query(value = "SELECT  p2.login as psicologo, p.nome_completo as paciente, s.id as sessao, s.dia_agendado as \"atendimento\", s.feedback\n" +
            "FROM sessao s\n" +
            "         INNER JOIN paciente p on s.paciente_id = p.id\n" +
            "         INNER JOIN psicologo p2 on p2.id = s.psicologo_id\n" +
            "WHERE p2.perfil = 2\n" +
            "  AND EXTRACT(YEAR FROM s.dia_agendado) = ?1 \n" +
            "ORDER BY paciente, dia_agendado" , nativeQuery = true)
    List<RelatorioAnaliticoSessaoProjection> getRelatorioAnalitico(Integer ano);
}
