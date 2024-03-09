package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.enums.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    @Transactional(readOnly = false)
    Horario findByDiaAndTurno(DayOfWeek dia, Turno turno);


}
