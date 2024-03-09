package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.EspecialidadePsicologo;
import br.manaus.mysoft.acolherbk.domain.HorarioPsi;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HorarioPsiRepository extends JpaRepository<HorarioPsi, Integer> {
    @Transactional(readOnly = false)
    List<HorarioPsi> findAllByPsicologo(Psicologo psicologo);

    @Transactional(readOnly = true)
    @Query("SELECT hp.id, p.nomeCompleto AS nomePaciente, h.dia, h.turno " +
            "FROM HorarioPsi hp " +
            "JOIN hp.psicologo p " +
            "JOIN hp.horario h")
    List<Object[]> obterInfoPsicologoHorario();
}
