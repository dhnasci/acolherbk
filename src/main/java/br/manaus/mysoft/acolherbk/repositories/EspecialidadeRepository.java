package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {
    @Transactional(readOnly = false)
    Especialidade findByDescricao(String descricao);
}
