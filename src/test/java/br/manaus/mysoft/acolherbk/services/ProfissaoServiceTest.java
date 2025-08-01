package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Profissao;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.ProfissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfissaoServiceTest {

    @InjectMocks
    ProfissaoService service;

    @Mock
    ProfissaoRepository repository;

    @Mock
    Profissao profissao1;

    @Mock
    Profissao profissao2;

    List<Profissao> listaProfissoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(profissao1)).thenReturn(profissao1);
        Optional<Profissao> profissaoOpt = Optional.of(profissao1);
        when(repository.findById(1)).thenReturn(profissaoOpt);
        listaProfissoes = Arrays.asList(profissao1, profissao2);
        when(repository.findAll()).thenReturn(listaProfissoes);
        when(repository.findByDescricao("Profissao Teste")).thenReturn(profissao1);
    }

    @Test
    void insert() {
        Profissao resp = service.insert(profissao1);
        assertEquals(profissao1, resp);
    }

    @Test
    void find() throws ObjetoException {
        Profissao resposta = service.find(1);
        assertEquals(profissao1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("Profissao nao encontrada"));
    }

    @Test
    void listar() {
        List<Profissao> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void getByDescricao() {
        Profissao resposta = service.getByDescricao("Profissao Teste");
        assertEquals(profissao1, resposta);
    }

    @Test
    void update() {
        Profissao resposta = service.update(profissao1);
        assertEquals(profissao1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar Profissao")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar Profissao", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaProfissoes);
        verify(repository).saveAll(listaProfissoes);
    }
}