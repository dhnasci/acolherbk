package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.EspecialidadePaciente;
import br.manaus.mysoft.acolherbk.domain.EspecialidadePsicologo;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EspecialidadePsicologoRepository extends JpaRepository<EspecialidadePsicologo, Integer> {
    @Transactional(readOnly = false)
    List<EspecialidadePsicologo> findAllByPsicologo(Psicologo psicologo);
}
