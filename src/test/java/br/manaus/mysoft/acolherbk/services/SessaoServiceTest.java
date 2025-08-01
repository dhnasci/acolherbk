package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SessaoServiceTest {

    @InjectMocks
    SessaoService service;

    @Mock
    SessaoRepository repository;

    @Mock
    Sessao sessao1;

    @Mock
    Sessao sessao2;

    List<Sessao> listaSessoes;
    LocalDateTime inicio, fim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listaSessoes = new ArrayList<>();
        listaSessoes.add(sessao1);
        listaSessoes.add(sessao2);
        inicio = LocalDateTime.now();
        fim = inicio.plusDays(2);
        
        when(repository.save(sessao1)).thenReturn(sessao1);
        when(repository.save(sessao2)).thenReturn(sessao2);
        when(repository.findById(1)).thenReturn(Optional.of(sessao1));
        when(repository.findById(2)).thenReturn(Optional.of(sessao2));
        when(repository.findAll()).thenReturn(listaSessoes);
        when(repository.findSessoesByDiaAgendadoBetween(inicio, fim)).thenReturn(listaSessoes);
        when(repository.findSessoesByLoginPsicologo("psicologo")).thenReturn(listaSessoes);
        when(repository.findSessoesByIsCancelado(true)).thenReturn(listaSessoes);
        when(repository.findSessoesByIsPacienteAtendido(true)).thenReturn(listaSessoes);
    }

    @Test
    void inserir() {
        Sessao resposta = service.inserir(sessao1);
        assertEquals(sessao1, resposta);
    }

    @Test
    void buscarPeloId() throws ObjetoException {
        Sessao resposta = service.buscarPeloId(1);
        assertEquals(sessao1, resposta);
    }

    @Test
    void buscarPeloIdNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.buscarPeloId(99));
        assertTrue(exception.getMessage().contains("Sessão não encontrada"));
    }

    @Test
    void listar() {
        List<Sessao> lista = service.listar();
        assertEquals(2, lista.size());
    }

    @Test
    void buscarSessoesNoIntervalo() {
        List<Sessao> lista = service.buscarSessoesNoIntervalo(inicio, fim);
        assertEquals(2, lista.size());
    }

    @Test
    void buscarSessoesPorLoginPsicologo() {
        List<Sessao> lista = service.buscarSessoesPorLoginPsicologo("psicologo");
        assertEquals(2, lista.size());
    }

    @Test
    void buscarSessoesCanceladas() {
        List<Sessao> lista = service.buscarSessoesCanceladas(true);
        assertEquals(2, lista.size());
    }

    @Test
    void buscarSessoesAtendidas() {
        List<Sessao> lista = service.buscarSessoesAtendidas(true);
        assertEquals(2, lista.size());
    }

    @Test
    void atualizar() {
        Sessao resposta = service.atualizar(sessao1);
        assertEquals(sessao1, resposta);
    }

    @Test
    void cancelarSessao() throws ObjetoException {
        when(sessao1.getIsCancelado()).thenReturn(false);
        
        service.cancelarSessao(1, "Motivo de teste");
        
        verify(sessao1).setIsCancelado(true);
        verify(sessao1).setMotivoCancelamento("Motivo de teste");
        verify(repository).save(sessao1);
    }

    @Test
    void registrarAtendimento() throws ObjetoException {
        when(sessao1.getIsPacienteAtendido()).thenReturn(false);
        
        service.registrarAtendimento(1, "Feedback de teste");
        
        verify(sessao1).setIsPacienteAtendido(true);
        verify(sessao1).setFeedback("Feedback de teste");
        verify(repository).save(sessao1);
    }

    @Test
    void apagar() throws ObjetoException {
        doNothing().when(repository).deleteById(1);
        
        service.apagar(1);
        
        verify(repository).deleteById(1);
    }

    @Test
    void apagarWithException() {
        doThrow(new RuntimeException()).when(repository).deleteById(99);
        
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.apagar(99));
        assertEquals("Erro ao apagar Sessão", exception.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaSessoes);
        verify(repository).saveAll(listaSessoes);
    }
}