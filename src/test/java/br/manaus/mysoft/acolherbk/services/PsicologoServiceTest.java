package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.manaus.mysoft.acolherbk.utils.Constantes.ERRO_APAGAR_PSICOLOGO;
import static br.manaus.mysoft.acolherbk.utils.Constantes.PSICOLOGO_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PsicologoServiceTest {

    @InjectMocks
    PsicologoService service;

    @Mock
    PsicologoRepository repository;
    @Mock
    Psicologo psicologo1;
    @Mock
    Psicologo psicologo2;
    List<Psicologo> listagem;
    LocalDateTime inicio;
    LocalDateTime fim;
    @Mock
    BCryptPasswordEncoder bcrypt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listagem = new ArrayList<>();
        listagem.add(psicologo1);
        listagem.add(psicologo2);
        inicio = LocalDateTime.now();
        fim = inicio.plusDays(2);
        when(repository.save(psicologo1)).thenReturn(psicologo1);
        when(repository.save(psicologo2)).thenReturn(psicologo2);
        when(bcrypt.encode(service.getNovaSenha())).thenReturn("ueiufidj3");
    }

    @Test
    void find() throws ObjetoException {
        Psicologo psi = new Psicologo();
        Optional<Psicologo> psiOpt = Optional.of(psi);
        when(repository.findById(1)).thenReturn(psiOpt);

        Psicologo resposta = service.find(1);

        assertNotNull(resposta);
    }

    @Test
    void naoAchei() {
        Optional<Psicologo> psiOpt = Optional.empty();
        when(repository.findById(1)).thenReturn(psiOpt);
        ObjetoException ex = assertThrows(ObjetoException.class, () -> service.find(1));
        assertEquals(PSICOLOGO_NAO_ENCONTRADO, ex.getMessage());
    }

    @Test
    void listar() {
        when(repository.findAll()).thenReturn(listagem);
        List<Psicologo> lista = service.listar();
        assertNotNull(lista);
    }

    @Test
    void buscarPeloNome() {
        when(repository.findPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto("Fulano")).thenReturn(listagem);
        List<Psicologo> resposta = service.buscarPeloNome("Fulano");
        assertEquals(2, resposta.size());
    }

    @Test
    void listarComSessoesAgendadasNoIntervalo() {
        when(repository.findPsicologosBySessoesDiaAgendadoBetween(inicio, fim)).thenReturn(listagem);
        List<Psicologo> lista = service.listarComSessoesAgendadasNoIntervalo(inicio, fim);
        assertEquals(2, lista.size());
    }

    @Test
    void inserir() {

        Psicologo resp = service.inserir(psicologo1);
        assertNotNull(resp);
    }

    @Test
    void generatePassword() {
        String resp = service.generatePassword(10);
        assertEquals(10, resp.length());
    }

    @Test
    void reset() {
        service.reset(psicologo1);
        verify(repository).save(psicologo1);
    }

    @Test
    void alterar() throws ObjetoException {
        when(psicologo2.getId()).thenReturn(1);
        Optional<Psicologo> psicologoOpt = Optional.of(psicologo2);
        when(repository.findById(1)).thenReturn(psicologoOpt);
        Psicologo resp = service.alterar(psicologo2);
        assertNotNull(resp);
    }

    @Test
    void apagar() throws ObjetoException {
        service.apagar(1);
        verify(repository).deleteById(1);
    }

    @Test
    void naoApagar() {
        doThrow(new RuntimeException(ERRO_APAGAR_PSICOLOGO)).when(repository).deleteById(2);

        ObjetoException ex = assertThrows(ObjetoException.class, () -> service.apagar(2));
        assertEquals(ERRO_APAGAR_PSICOLOGO, ex.getMessage());
    }

    @Test
    void getNovaSenha() {
        String senha = service.getNovaSenha();
        assertNull(senha);
    }

    @Test
    void setNovaSenha() {
        service.setNovaSenha("senhaNova");
        assertNotNull(service.getNovaSenha());
    }

    @Test
    void buscarPeloLogin() {
        when(repository.findPsicologoByLogin("fulano")).thenReturn(psicologo1);
        Psicologo psi = service.buscarPeloLogin("fulano");
        assertEquals(psicologo1, psi);
    }

    @Test
    void obterPeloNomeCompleto() throws ObjetoException {
        when(repository.findPsicologoByNomeCompleto("beltrano")).thenReturn(psicologo2);
        Psicologo resp = service.obterPeloNomeCompleto("beltrano");
        assertEquals(psicologo2, resp);

    }
}