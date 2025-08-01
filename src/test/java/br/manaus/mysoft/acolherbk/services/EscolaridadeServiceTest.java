package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Escolaridade;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EscolaridadeRepository;
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

class EscolaridadeServiceTest {

    @InjectMocks
    EscolaridadeService service;

    @Mock
    EscolaridadeRepository repository;

    @Mock
    Escolaridade escolaridade1;

    @Mock
    Escolaridade escolaridade2;

    List<Escolaridade> listaEscolaridades;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(escolaridade1)).thenReturn(escolaridade1);
        Optional<Escolaridade> escolaridadeOpt = Optional.of(escolaridade1);
        when(repository.findById(1)).thenReturn(escolaridadeOpt);
        listaEscolaridades = Arrays.asList(escolaridade1, escolaridade2);
        when(repository.findAll()).thenReturn(listaEscolaridades);
        when(repository.findByDescricao("Escolaridade Teste")).thenReturn(escolaridade1);
    }

    @Test
    void insert() {
        Escolaridade resp = service.insert(escolaridade1);
        assertEquals(escolaridade1, resp);
    }

    @Test
    void find() throws ObjetoException {
        Escolaridade resposta = service.find(1);
        assertEquals(escolaridade1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("Escolaridade nao encontrada"));
    }

    @Test
    void listar() {
        List<Escolaridade> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void getByDescricao() {
        Escolaridade resposta = service.getByDescricao("Escolaridade Teste");
        assertEquals(escolaridade1, resposta);
    }

    @Test
    void update() {
        Escolaridade resposta = service.update(escolaridade1);
        assertEquals(escolaridade1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar Escolaridade")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar Escolaridade", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaEscolaridades);
        verify(repository).saveAll(listaEscolaridades);
    }
}