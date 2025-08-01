package br.manaus.mysoft.acolherbk.controller;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.services.SessaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessaoControllerTest {

    @InjectMocks
    private SessaoController controller;

    @Mock
    private SessaoService service;

    private Sessao sessao;
    private List<Sessao> sessoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        iniciarSessao();
    }

    private void iniciarSessao() {
        sessao = new Sessao();
        sessao.setId(1);
        sessao.setLoginPsicologo("psicologo1");
        sessao.setDiaAgendado(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        sessao.setIsCancelado(false);
        sessao.setIsPacienteAtendido(false);

        sessoes = new ArrayList<>();
        sessoes.add(sessao);
    }

    @Test
    void quandoListarTodos_retornaListaDeSessoes() {
        when(service.listar()).thenReturn(sessoes);

        ResponseEntity<List<Sessao>> response = controller.listar();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(Sessao.class, response.getBody().get(0).getClass());
        assertEquals(1, response.getBody().get(0).getId());
        verify(service, times(1)).listar();
    }

    @Test
    void quandoBuscarPorId_retornaSessao() throws ObjetoException {
        when(service.buscarPeloId(anyInt())).thenReturn(sessao);

        ResponseEntity<Object> response = controller.buscarPorId(1);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Sessao.class, response.getBody().getClass());
        assertEquals(1, ((Sessao) response.getBody()).getId());
        verify(service, times(1)).buscarPeloId(anyInt());
    }

    @Test
    void quandoBuscarPorIntervalo_retornaListaDeSessoes() {
        String dataInicio = "2023-01-01";
        String dataFim = "2023-01-08";
        
        LocalDateTime ldtInicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime ldtFim = LocalDateTime.of(2023, 1, 8, 0, 0);

        when(service.buscarSessoesNoIntervalo(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(sessoes);

        ResponseEntity<List<Sessao>> response = controller.buscarPorIntervalo(dataInicio, dataFim);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(Sessao.class, response.getBody().get(0).getClass());
        verify(service, times(1)).buscarSessoesNoIntervalo(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void quandoBuscarPorLoginPsicologo_retornaListaDeSessoes() {
        when(service.buscarSessoesPorLoginPsicologo(anyString())).thenReturn(sessoes);

        ResponseEntity<List<Sessao>> response = controller.buscarPorLoginPsicologo("psicologo1");

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(Sessao.class, response.getBody().get(0).getClass());
        verify(service, times(1)).buscarSessoesPorLoginPsicologo(anyString());
    }

    @Test
    void quandoBuscarSessoesCanceladas_retornaListaDeSessoes() {
        when(service.buscarSessoesCanceladas(true)).thenReturn(sessoes);

        ResponseEntity<List<Sessao>> response = controller.buscarSessoesCanceladas(true);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(Sessao.class, response.getBody().get(0).getClass());
        verify(service, times(1)).buscarSessoesCanceladas(true);
    }

    @Test
    void quandoBuscarSessoesAtendidas_retornaListaDeSessoes() {
        when(service.buscarSessoesAtendidas(true)).thenReturn(sessoes);

        ResponseEntity<List<Sessao>> response = controller.buscarSessoesAtendidas(true);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(Sessao.class, response.getBody().get(0).getClass());
        verify(service, times(1)).buscarSessoesAtendidas(true);
    }

    @Test
    void quandoInserir_retornaSessaoComStatusCreated() {
        when(service.inserir(any(Sessao.class))).thenReturn(sessao);

        ResponseEntity<Object> response = controller.inserir(sessao);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Sessao.class, response.getBody().getClass());
        assertEquals(1, ((Sessao) response.getBody()).getId());
        verify(service, times(1)).inserir(any(Sessao.class));
    }

    @Test
    void quandoAtualizar_retornaSessaoAtualizada() {
        when(service.atualizar(any(Sessao.class))).thenReturn(sessao);

        ResponseEntity<Object> response = controller.atualizar(1, sessao);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Sessao.class, response.getBody().getClass());
        assertEquals(1, ((Sessao) response.getBody()).getId());
        verify(service, times(1)).atualizar(any(Sessao.class));
    }

    @Test
    void quandoCancelarSessao_retornaOk() throws ObjetoException {
        doNothing().when(service).cancelarSessao(anyInt(), anyString());

        ResponseEntity<Object> response = controller.cancelarSessao(1, "Motivo de teste");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).cancelarSessao(anyInt(), anyString());
    }

    @Test
    void quandoRegistrarAtendimento_retornaOk() throws ObjetoException {
        doNothing().when(service).registrarAtendimento(anyInt(), anyString());

        ResponseEntity<Object> response = controller.registrarAtendimento(1, "Feedback de teste");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).registrarAtendimento(anyInt(), anyString());
    }

    @Test
    void quandoApagar_retornaOk() throws ObjetoException {
        doNothing().when(service).apagar(anyInt());

        ResponseEntity<Object> response = controller.apagar(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).apagar(anyInt());
    }
}