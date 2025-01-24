package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.ChartDto;
import br.manaus.mysoft.acolherbk.dto.FaixaEtariaDistribuicaoProjection;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoProjection;
import br.manaus.mysoft.acolherbk.dto.StatusAtendimentoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    @Transactional(readOnly = true)
    List<Paciente> findPacientesByNomeCompletoContainingIgnoreCase(String nome);

    @Transactional(readOnly = true)  // ver pacientes agendados para sessão no intervalo
    List<Paciente> findPacientesBySessoesDiaAgendadoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Transactional(readOnly = true) // ver pacientes alocados no intervalo
    List<Paciente> findPacientesByTriagemsDiaAlocacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Transactional(readOnly = true)   // para ver pacientes pendentes de triagem
    List<Paciente> findPacientesByTriagemsIsPacienteAlocado(Boolean isPacienteAlocado);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    p.id as \"idPaciente\",\n" +
            "    CASE\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento is not null THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback is not null THEN 'ATENDIDO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and t.is_paciente_alocado = false THEN 'TRIAGEM'\n" +
            "        WHEN t.id IS NOT NULL and t.is_paciente_alocado = true THEN 'EM ATENDIMENTO'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\"\n" +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE t.psicologo_id = ?1 AND t.is_paciente_alocado = TRUE\n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesAlocados(Integer psicologoId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT A.\"idPaciente\",\n" +
            "       A.\"nomeCompleto\",\n" +
            "        A.celular1,\n" +
            "        A.\"isWhatsapp1\",\n" +
            "        A.celular2,\n" +
            "        A.\"registroGeral\",\n" +
            "        A.status\n" +
            "FROM (SELECT DISTINCT ON (p.id) p.nome_completo  AS \"nomeCompleto\",\n" +
            "                                         p.id             as \"idPaciente\",\n" +
            "                                         CASE\n" +
            "                                             WHEN s.is_cancelado = true AND s.motivo_cancelamento is not null\n" +
            "                                                 THEN 'CANCELADO'\n" +
            "                                             WHEN s.is_paciente_atendido = true AND s.feedback is not null\n" +
            "                                                 THEN 'ATENDIDO'\n" +
            "                                             WHEN s.id IS NULL AND t.id IS NULL\n" +
            "                                                 THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "                                             WHEN t.id IS NOT NULL and t.is_paciente_alocado = false THEN 'TRIAGEM'\n" +
            "                                             WHEN t.id IS NOT NULL and t.is_paciente_alocado = true\n" +
            "                                                 THEN 'EM ATENDIMENTO'\n" +
            "                                             END          AS \"status\",\n" +
            "                                         p.celular1,\n" +
            "                                         p.is_whatsapp1   as \"isWhatsapp1\",\n" +
            "                                         p.celular2,\n" +
            "                                         p.registro_geral as \"registroGeral\"\n" +
            "               FROM paciente p\n" +
            "                        LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "                        LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "               WHERE t.psicologo_id = ?1\n" +
            "                 AND t.is_paciente_alocado = TRUE\n" +
            "               ORDER BY p.id, s.id DESC\n" +
            "               ) A\n" +
            "    WHERE status = ?2 ;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesAlocadosPorStatus(Integer psicologoId, String status);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    p.id as \"idPaciente\",\n" +
            "    CASE\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento is not null THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback is not null THEN 'ATENDIDO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and t.is_paciente_alocado = false THEN 'TRIAGEM'\n" +
            "        WHEN t.id IS NOT NULL and t.is_paciente_alocado = true THEN 'EM ATENDIMENTO'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\"\n" +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientes();


    @Transactional(readOnly = true)
    @Query(value = "SELECT A.\"idPaciente\",\n" +
            "       A.\"nomeCompleto\",\n" +
            "       A.celular1,\n" +
            "       A.\"isWhatsapp1\",\n" +
            "       A.celular2,\n" +
            "       A.\"registroGeral\",\n" +
            "       A.status \n" +
            "FROM (SELECT DISTINCT ON (p.id) p.nome_completo  AS \"nomeCompleto\",\n" +
            "                                p.id             as \"idPaciente\",\n" +
            "                                CASE\n" +
            "                                    WHEN s.is_cancelado = true AND s.motivo_cancelamento is not null THEN 'CANCELADO'\n" +
            "                                    WHEN s.is_paciente_atendido = true AND s.feedback is not null THEN 'ATENDIDO'\n" +
            "                                    WHEN s.id IS NULL AND t.id IS NULL\n" +
            "                                        THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "                                    WHEN t.id IS NOT NULL and t.is_paciente_alocado = false THEN 'TRIAGEM'\n" +
            "                                    WHEN t.id IS NOT NULL and t.is_paciente_alocado = true THEN 'EM ATENDIMENTO'\n" +
            "                                    END          AS \"status\",\n" +
            "                                p.celular1,\n" +
            "                                p.is_whatsapp1   as \"isWhatsapp1\",\n" +
            "                                p.celular2,\n" +
            "                                p.registro_geral as \"registroGeral\"\n" +
            "      FROM paciente p\n" +
            "               LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "               LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "      ORDER BY p.id, s.id DESC\n" +
            "      ) A\n" +
            "WHERE status = ?1 ;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesPorStatus(String status);


    @Transactional(readOnly = true)
    @Query(value = "SELECT A.status, count(*) as total\n" +
            "    FROM (\n" +
            "        SELECT DISTINCT ON (p.id)\n" +
            "            p.nome_completo,\n" +
            "            CASE\n" +
            "                WHEN s.is_cancelado = true AND s.motivo_cancelamento is not null THEN 'CANCELADO'\n" +
            "                WHEN s.is_paciente_atendido = true AND s.feedback is not null THEN 'ATENDIDO'\n" +
            "                WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' \n" +
            "                WHEN t.id IS NOT NULL and t.is_paciente_alocado = false THEN 'TRIAGEM'\n" +
            "                WHEN t.id IS NOT NULL and t.is_paciente_alocado = true THEN 'EM ATENDIMENTO'WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "                END AS \"status\",\n" +
            "            p.celular1,\n" +
            "            p.registro_geral\n" +
            "        FROM paciente p\n" +
            "                 LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "                 LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "        ORDER BY p.id, s.id DESC\n" +
            "         ) A\n" +
            "GROUP BY status\n" +
            "ORDER BY status;", nativeQuery = true)
    List<StatusAtendimentoProjection> getAllStatusAtendimento();

    @Transactional(readOnly = true)
    @Query(value = "SELECT\n" +
            "    CASE\n" +
            "        WHEN idade <= 12 THEN 'Criancas'\n" +
            "        WHEN idade BETWEEN 13 AND 18 THEN 'Adolescentes'\n" +
            "        WHEN idade BETWEEN 19 AND 25 THEN 'Jovens'\n" +
            "        WHEN idade BETWEEN 26 AND 60 THEN 'Adultos'\n" +
            "        WHEN idade > 60 THEN 'Idosos'\n" +
            "        --ELSE 'Idade inválida'\n" +
            "        END AS faixaetaria,\n" +
            "    COUNT(*) AS quantidade\n" +
            "FROM paciente\n" +
            "GROUP BY faixaetaria\n" +
            "ORDER BY quantidade DESC;", nativeQuery = true)
    List<FaixaEtariaDistribuicaoProjection> getAllDistribuicaoFaixaEtaria();
}
