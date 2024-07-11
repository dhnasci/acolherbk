package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoDto;
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
    @Query(value = "SELECT\n" +
            "    p.nome_completo AS \"nomeCompleto\",\n" +
            "    p.celular1,\n" +
            "    p.is_whatsapp1 as \"isWhatsapp1\",\n" +
            "    p.celular2,\n" +
            "    p.registro_geral as \"registroGeral\"\n" +
            "FROM\n" +
            "    paciente p\n" +
            "        JOIN\n" +
            "    triagem t ON p.id = t.paciente_id\n" +
            "WHERE\n" +
            "    t.psicologo_id = $1 \n" +
            "  AND\n" +
            "    t.is_paciente_alocado = TRUE", nativeQuery = true)
    List<PacienteAlocadoDto> findAllPacientesAlocados(Integer psicologoId);
}
