package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.HorarioPaciente;
import br.manaus.mysoft.acolherbk.domain.HorarioPsi;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.dto.HorarioDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HorarioPacienteRepository extends JpaRepository<HorarioPaciente, Integer> {
    @Transactional(readOnly = false)
    List<HorarioPaciente> findAllByPaciente(Paciente paciente);

    @Transactional(readOnly = true)
    @Query("SELECT hp.id, p.nomeCompleto AS nomePaciente, h.dia, h.turno " +
            "FROM HorarioPaciente hp " +
            "JOIN hp.paciente p " +
            "JOIN hp.horario h")
    List<Object[]> obterInfoPacienteHorario();
}
