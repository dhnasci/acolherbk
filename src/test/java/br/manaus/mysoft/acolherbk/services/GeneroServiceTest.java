package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Genero;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.GeneroRepository;
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

class GeneroServiceTest {

    @InjectMocks
    GeneroService service;

    @Mock
    GeneroRepository repository;

    @Mock
    Genero genero1;

    @Mock
    Genero genero2;

    List<Genero> listaGeneros;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(genero1)).thenReturn(genero1);
        Optional<Genero> generoOpt = Optional.of(genero1);
        when(repository.findById(1)).thenReturn(generoOpt);
        listaGeneros = Arrays.asList(genero1, genero2);
        when(repository.findAll()).thenReturn(listaGeneros);
        when(repository.findByDescricao("Genero Teste")).thenReturn(genero1);
    }

    @Test
    void insert() {
        Genero resp = service.insert(genero1);
        assertEquals(genero1, resp);
    }

    @Test
    void find() throws ObjetoException {
        Genero resposta = service.find(1);
        assertEquals(genero1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("Genero nao encontrada"));
    }

    @Test
    void listar() {
        List<Genero> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void getByDescricao() {
        Genero resposta = service.getByDescricao("Genero Teste");
        assertEquals(genero1, resposta);
    }

    @Test
    void update() {
        Genero resposta = service.update(genero1);
        assertEquals(genero1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar Genero")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar Genero", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaGeneros);
        verify(repository).saveAll(listaGeneros);
    }
}