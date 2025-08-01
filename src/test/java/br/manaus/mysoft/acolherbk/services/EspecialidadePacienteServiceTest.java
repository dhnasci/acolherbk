package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.EspecialidadePaciente;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EspecialidadePacienteRepository;
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

class EspecialidadePacienteServiceTest {

    @InjectMocks
    EspecialidadePacienteService service;

    @Mock
    EspecialidadePacienteRepository repository;

    @Mock
    EspecialidadePaciente especialidadePaciente1;

    @Mock
    EspecialidadePaciente especialidadePaciente2;

    @Mock
    Paciente paciente;

    List<EspecialidadePaciente> listaEspecialidadesPaciente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(especialidadePaciente1)).thenReturn(especialidadePaciente1);
        Optional<EspecialidadePaciente> especialidadePacienteOpt = Optional.of(especialidadePaciente1);
        when(repository.findById(1)).thenReturn(especialidadePacienteOpt);
        listaEspecialidadesPaciente = Arrays.asList(especialidadePaciente1, especialidadePaciente2);
        when(repository.findAll()).thenReturn(listaEspecialidadesPaciente);
        when(repository.findAllByPaciente(paciente)).thenReturn(listaEspecialidadesPaciente);
    }

    @Test
    void insert() {
        EspecialidadePaciente resp = service.insert(especialidadePaciente1);
        assertEquals(especialidadePaciente1, resp);
    }

    @Test
    void find() throws ObjetoException {
        EspecialidadePaciente resposta = service.find(1);
        assertEquals(especialidadePaciente1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("EspecialidadePaciente nao encontrada"));
    }

    @Test
    void listar() {
        List<EspecialidadePaciente> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void obterEspecialidadesPorPaciente() {
        List<EspecialidadePaciente> listagem = service.obterEspecialidadesPorPaciente(paciente);
        assertEquals(2, listagem.size());
        verify(repository).findAllByPaciente(paciente);
    }

    @Test
    void update() {
        EspecialidadePaciente resposta = service.update(especialidadePaciente1);
        assertEquals(especialidadePaciente1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar EspecialidadePaciente")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar EspecialidadePaciente", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaEspecialidadesPaciente, paciente);
        verify(especialidadePaciente1).setPaciente(paciente);
        verify(especialidadePaciente2).setPaciente(paciente);
        verify(repository).saveAll(listaEspecialidadesPaciente);
    }
}