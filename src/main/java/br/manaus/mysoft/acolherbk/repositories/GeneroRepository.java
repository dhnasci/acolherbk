package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    @Transactional(readOnly = false)
    Genero findByDescricao(String descricao);
}
