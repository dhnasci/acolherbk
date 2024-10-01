package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoProjection;
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
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\",\n" +
            "    p.id as \"idPaciente\"\n" +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    t.psicologo_id = ?1 \n" +
            "  AND\n" +
            "    t.is_paciente_alocado = TRUE AND \n" +
            "    s.motivo_cancelamento IS NULL and s.feedback IS NULL ", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesAlocados(Integer psicologoId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    CASE\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NULL THEN 'EM ATENDIMENTO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NULL THEN 'EM CANCELAMENTO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and s.id IS NULL THEN 'TRIAGEM'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\", \n" +
            "    p.id as \"idPaciente\" " +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    s.id IS NULL OR (s.feedback IS NOT NULL OR s.motivo_cancelamento IS NOT NULL)\n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientes();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    CASE\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NULL THEN 'EM ATENDIMENTO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NULL THEN 'EM CANCELAMENTO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and s.id IS NULL THEN 'TRIAGEM'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\", \n" +
            "    p.id as \"idPaciente\" " +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    s.is_paciente_atendido = true AND s.feedback IS NOT NULL \n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesAtendidos();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    CASE\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NULL THEN 'EM ATENDIMENTO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NULL THEN 'EM CANCELAMENTO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and s.id IS NULL THEN 'TRIAGEM'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\", \n" +
            "    p.id as \"idPaciente\" " +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL \n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesCancelados();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    CASE\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NULL THEN 'EM ATENDIMENTO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NULL THEN 'EM CANCELAMENTO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and s.id IS NULL THEN 'TRIAGEM'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\", \n" +
            "    p.id as \"idPaciente\" " +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    s.is_paciente_atendido = true AND s.feedback IS NULL \n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesEmAtendimento();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    CASE\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NULL THEN 'EM ATENDIMENTO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NULL THEN 'EM CANCELAMENTO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and s.id IS NULL THEN 'TRIAGEM'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\", \n" +
            "    p.id as \"idPaciente\" " +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    s.id IS NULL AND t.id IS NULL  \n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesPendentes();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT ON (p.id)\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    CASE\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NOT NULL THEN 'ATENDIDO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NOT NULL THEN 'CANCELADO'\n" +
            "        WHEN s.is_paciente_atendido = true AND s.feedback IS NULL THEN 'EM ATENDIMENTO'\n" +
            "        WHEN s.is_cancelado = true AND s.motivo_cancelamento IS NULL THEN 'EM CANCELAMENTO'\n" +
            "        WHEN s.id IS NULL AND t.id IS NULL THEN 'PENDENTE' -- Quando não há sessão, o status é \"Pendente\"\n" +
            "        WHEN t.id IS NOT NULL and s.id IS NULL THEN 'TRIAGEM'\n" +
            "        END AS \"status\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\", \n" +
            "    p.id as \"idPaciente\" " +
            "FROM paciente p\n" +
            "         LEFT JOIN sessao s on p.id = s.paciente_id\n" +
            "         LEFT JOIN triagem t on p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    t.id IS NOT NULL and s.id IS NULL  \n" +
            "ORDER BY p.id, s.id DESC;", nativeQuery = true)
    List<PacienteAlocadoProjection> findAllPacientesEmTriagem();



}
