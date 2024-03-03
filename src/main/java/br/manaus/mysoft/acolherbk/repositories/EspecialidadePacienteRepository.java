package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.EspecialidadePaciente;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EspecialidadePacienteRepository extends JpaRepository<EspecialidadePaciente, Integer> {
    @Transactional(readOnly = false)
    List<EspecialidadePaciente> findAllByPaciente(Paciente paciente);
}
