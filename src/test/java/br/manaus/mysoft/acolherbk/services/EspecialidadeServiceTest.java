package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Especialidade;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EspecialidadeRepository;
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

class EspecialidadeServiceTest {

    @InjectMocks
    EspecialidadeService service;

    @Mock
    EspecialidadeRepository repository;

    @Mock
    Especialidade especialidade1;

    @Mock
    Especialidade especialidade2;

    List<Especialidade> listaEspecialidades;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(especialidade1)).thenReturn(especialidade1);
        Optional<Especialidade> especialidadeOpt = Optional.of(especialidade1);
        when(repository.findById(1)).thenReturn(especialidadeOpt);
        listaEspecialidades = Arrays.asList(especialidade1, especialidade2);
        when(repository.findAll()).thenReturn(listaEspecialidades);
        when(repository.findByDescricao("Especialidade Teste")).thenReturn(especialidade1);
    }

    @Test
    void insert() {
        Especialidade resp = service.insert(especialidade1);
        assertEquals(especialidade1, resp);
    }

    @Test
    void find() throws ObjetoException {
        Especialidade resposta = service.find(1);
        assertEquals(especialidade1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("Especialidade nao encontrada"));
    }

    @Test
    void listar() {
        List<Especialidade> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void getByDescricao() {
        Especialidade resposta = service.getByDescricao("Especialidade Teste");
        assertEquals(especialidade1, resposta);
    }

    @Test
    void update() {
        Especialidade resposta = service.update(especialidade1);
        assertEquals(especialidade1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar Especialidade")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar Especialidade", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaEspecialidades);
        verify(repository).saveAll(listaEspecialidades);
    }
}