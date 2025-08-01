package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.HorarioPaciente;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.HorarioPacienteRepository;
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

class HorarioPacienteServiceTest {

    @InjectMocks
    HorarioPacienteService service;

    @Mock
    HorarioPacienteRepository repository;

    @Mock
    HorarioPaciente horarioPaciente1;

    @Mock
    HorarioPaciente horarioPaciente2;

    @Mock
    Paciente paciente;

    List<HorarioPaciente> listaHorariosPaciente;
    List<Object[]> listaInfoHorarios;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(horarioPaciente1)).thenReturn(horarioPaciente1);
        Optional<HorarioPaciente> horarioPacienteOpt = Optional.of(horarioPaciente1);
        when(repository.findById(1)).thenReturn(horarioPacienteOpt);
        listaHorariosPaciente = Arrays.asList(horarioPaciente1, horarioPaciente2);
        listaInfoHorarios = Arrays.asList(new Object[]{"info1"}, new Object[]{"info2"});
        when(repository.findAll()).thenReturn(listaHorariosPaciente);
        when(repository.findAllByPaciente(paciente)).thenReturn(listaHorariosPaciente);
        when(repository.obterInfoPacienteHorario(1)).thenReturn(listaInfoHorarios);
    }

    @Test
    void insert() {
        HorarioPaciente resp = service.insert(horarioPaciente1);
        assertEquals(horarioPaciente1, resp);
    }

    @Test
    void find() throws ObjetoException {
        HorarioPaciente resposta = service.find(1);
        assertEquals(horarioPaciente1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("HorarioPaciente nao encontrada"));
    }

    @Test
    void listar() {
        List<Object[]> listagem = service.listar(1);
        assertEquals(2, listagem.size());
        verify(repository).obterInfoPacienteHorario(1);
    }

    @Test
    void obterHorariosPaciente() {
        List<HorarioPaciente> listagem = service.obterHorariosPaciente(paciente);
        assertEquals(2, listagem.size());
        verify(repository).findAllByPaciente(paciente);
    }

    @Test
    void update() {
        HorarioPaciente resposta = service.update(horarioPaciente1);
        assertEquals(horarioPaciente1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar HorarioPaciente")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar HorarioPaciente", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaHorariosPaciente);
        verify(repository).saveAll(listaHorariosPaciente);
    }
}