package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TriagemRepository extends JpaRepository<Triagem, Integer> {
}
