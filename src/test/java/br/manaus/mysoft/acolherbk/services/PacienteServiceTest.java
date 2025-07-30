package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PacienteServiceTest {

    @InjectMocks
    PacienteService service;
    @Mock
    Paciente paciente1;
    @Mock
    Paciente paciente2;
    @Mock
    PacienteRepository repository;
    List<Paciente> lista_de_pacientes;
    LocalDateTime inicio, fim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(paciente1)).thenReturn(paciente1);
        Optional<Paciente> pacienteOpt = Optional.of(paciente1);
        when(repository.findById(1)).thenReturn(pacienteOpt);
        lista_de_pacientes = Arrays.asList(paciente1, paciente2);
        when(repository.findAll()).thenReturn(lista_de_pacientes);
        inicio = LocalDateTime.now();
        fim = inicio.plusDays(2);
    }

    @Test
    void insert() {
        Paciente resp = service.insert(paciente1);
        assertEquals(paciente1, resp);
    }

    @Test
    void find() throws ObjetoException {
        Paciente resposta = service.find(1);
        assertEquals(paciente1, resposta);
    }

    @Test
    void listar() {
        List<Paciente> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void buscarPorNome() {
        when(repository.findPacientesByNomeCompletoContainingIgnoreCase("Fulana")).thenReturn(lista_de_pacientes);
        List<Paciente> lista_2 = service.buscarPorNome("Fulana");
        assertEquals(2, lista_2.size());
    }

    @Test
    void buscarPacientesAgendadosNoIntervalo() {
        when(repository.findPacientesBySessoesDiaAgendadoBetween(inicio, fim)).thenReturn(lista_de_pacientes);
        List<Paciente> lista_3 = service.buscarPacientesAgendadosNoIntervalo(inicio, fim);
        assertEquals(2, lista_3.size());
    }

    @Test
    void buscarPacientesAlocadosNoIntervalo() {
    }

    @Test
    void buscarPacientesPendentesDeTriagem() {
    }

    @Test
    void buscarPacientesAlocadosAoPsicologo() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}