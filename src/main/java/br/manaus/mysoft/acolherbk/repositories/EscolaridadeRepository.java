package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Escolaridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EscolaridadeRepository extends JpaRepository<Escolaridade, Integer> {
    @Transactional(readOnly = false)
    Escolaridade findByDescricao(String descricao);

}
