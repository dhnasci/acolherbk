package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
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

    // todo verificar expressão - testar
    @Transactional(readOnly = true)   // para ver pacientes alocados ao psicologo
    List<Paciente> findPacientesByTriagemsIsPsicologoId(Integer psicologoId);

}
