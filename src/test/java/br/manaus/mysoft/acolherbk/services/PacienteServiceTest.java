package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoDto;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoProjection;
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

import static br.manaus.mysoft.acolherbk.utils.Constantes.ERRO_APAGAR_PACIENTE;
import static br.manaus.mysoft.acolherbk.utils.Constantes.ERRO_APAGAR_PSICOLOGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @InjectMocks
    PacienteService service;
    @Mock
    Paciente paciente1;
    @Mock
    Paciente paciente2;
    @Mock
    PacienteRepository repository;
    @Mock
    PacienteAlocadoProjection projecao1;
    @Mock
    PacienteAlocadoProjection projecao2;
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
        when(repository.findPacientesByTriagemsDiaAlocacaoBetween(inicio, fim)).thenReturn(lista_de_pacientes);
        List<Paciente> lista_4 = service.buscarPacientesAlocadosNoIntervalo(inicio, fim);
        assertEquals(2, lista_4.size());
    }

    @Test
    void buscarPacientesPendentesDeTriagem() {
        when(repository.findPacientesByTriagemsIsPacienteAlocado(false)).thenReturn(lista_de_pacientes);
        List<Paciente> lista_5 = service.buscarPacientesPendentesDeTriagem();
        assertEquals(2, lista_5.size());
    }

    @Test
    void buscarPacientesAlocadosAoPsicologo() {
        List<PacienteAlocadoProjection> projecoes = Arrays.asList(projecao1, projecao2);
        when(repository.findAllPacientesAlocados(1)).thenReturn(projecoes);
        List<PacienteAlocadoDto> lista_alocados = service.buscarPacientesAlocadosAoPsicologo(1);
        assertEquals(2, lista_alocados.size());
    }

    @Test
    void update() {
        Paciente resposta = service.update(paciente1);
        assertEquals(paciente1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException(ERRO_APAGAR_PACIENTE)).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals(ERRO_APAGAR_PACIENTE, ex.getMessage());
    }
}