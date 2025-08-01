package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.HorarioPsi;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.HorarioPsiRepository;
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

class HorarioPsiServiceTest {

    @InjectMocks
    HorarioPsiService service;

    @Mock
    HorarioPsiRepository repository;

    @Mock
    HorarioPsi horarioPsi1;

    @Mock
    HorarioPsi horarioPsi2;

    @Mock
    Psicologo psicologo;

    List<HorarioPsi> listaHorariosPsi;
    List<Object[]> listaInfoHorarios;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(horarioPsi1)).thenReturn(horarioPsi1);
        Optional<HorarioPsi> horarioPsiOpt = Optional.of(horarioPsi1);
        when(repository.findById(1)).thenReturn(horarioPsiOpt);
        listaHorariosPsi = Arrays.asList(horarioPsi1, horarioPsi2);
        listaInfoHorarios = Arrays.asList(new Object[]{"info1"}, new Object[]{"info2"});
        when(repository.findAll()).thenReturn(listaHorariosPsi);
        when(repository.findAllByPsicologo(psicologo)).thenReturn(listaHorariosPsi);
        when(repository.obterInfoPsicologoHorario(1)).thenReturn(listaInfoHorarios);
    }

    @Test
    void insert() {
        HorarioPsi resp = service.insert(horarioPsi1);
        assertEquals(horarioPsi1, resp);
    }

    @Test
    void find() throws ObjetoException {
        HorarioPsi resposta = service.find(1);
        assertEquals(horarioPsi1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("HorarioPsi nao encontrada"));
    }

    @Test
    void listar() {
        List<Object[]> listagem = service.listar(1);
        assertEquals(2, listagem.size());
        verify(repository).obterInfoPsicologoHorario(1);
    }

    @Test
    void obterHorariosPsicologo() {
        List<HorarioPsi> listagem = service.obterHorariosPsicologo(psicologo);
        assertEquals(2, listagem.size());
        verify(repository).findAllByPsicologo(psicologo);
    }

    @Test
    void update() {
        HorarioPsi resposta = service.update(horarioPsi1);
        assertEquals(horarioPsi1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar HorarioPsi")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar HorarioPsi", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaHorariosPsi);
        verify(repository).saveAll(listaHorariosPsi);
    }
}