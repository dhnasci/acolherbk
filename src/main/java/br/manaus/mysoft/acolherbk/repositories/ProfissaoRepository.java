package br.manaus.mysoft.acolherbk.repositories;


import br.manaus.mysoft.acolherbk.domain.Profissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Integer> {
    @Transactional(readOnly = false)
    Profissao findByDescricao(String descricao);
}
