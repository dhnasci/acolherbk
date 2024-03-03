package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.HorarioPaciente;
import br.manaus.mysoft.acolherbk.domain.HorarioPsi;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HorarioPacienteRepository extends JpaRepository<HorarioPaciente, Integer> {
    @Transactional(readOnly = false)
    List<HorarioPaciente> findAllByPaciente(Paciente paciente);
}
