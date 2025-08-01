package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Triagem;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.TriagemRepository;
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

class TriagemServiceTest {

    @InjectMocks
    TriagemService service;

    @Mock
    TriagemRepository repository;

    @Mock
    Triagem triagem1;

    @Mock
    Triagem triagem2;

    List<Triagem> listaTriagens;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(triagem1)).thenReturn(triagem1);
        Optional<Triagem> triagemOpt = Optional.of(triagem1);
        when(repository.findById(1)).thenReturn(triagemOpt);
        listaTriagens = Arrays.asList(triagem1, triagem2);
        when(repository.findAll()).thenReturn(listaTriagens);
    }

    @Test
    void insert() {
        Triagem resp = service.insert(triagem1);
        verify(triagem1).setIsPacienteAlocado(true);
        assertEquals(triagem1, resp);
    }

    @Test
    void cancel() {
        Triagem resp = service.cancel(triagem1);
        verify(triagem1).setIsPacienteAlocado(false);
        verify(triagem1).setIsCancelado(true);
        assertEquals(triagem1, resp);
    }

    @Test
    void find() throws ObjetoException {
        Triagem resposta = service.find(1);
        assertEquals(triagem1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("Triagem nao encontrada"));
    }

    @Test
    void listar() {
        List<Triagem> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void update() {
        Triagem resposta = service.update(triagem1);
        assertEquals(triagem1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar Triagem")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar Triagem", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaTriagens);
        verify(repository).saveAll(listaTriagens);
    }
}