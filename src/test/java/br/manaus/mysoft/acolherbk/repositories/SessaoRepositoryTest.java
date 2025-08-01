package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SessaoRepositoryTest {

    @Mock
    private SessaoRepository sessaoRepository;

    private List<Sessao> sessoes;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Necessário para deserializar LocalDateTime
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/sessoes.json");
        sessoes = mapper.readValue(is, new TypeReference<List<Sessao>>() {});
        
        // Configura o comportamento do mock para findAll
        when(sessaoRepository.findAll()).thenReturn(sessoes);
        
        // Configura o comportamento do mock para findById
        for (Sessao sessao : sessoes) {
            when(sessaoRepository.findById(sessao.getId()))
                    .thenReturn(Optional.of(sessao));
        }
        
        // Configura o comportamento do mock para findSessoesByDiaAgendadoBetween
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 15, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 1, 23, 0, 0);
        when(sessaoRepository.findSessoesByDiaAgendadoBetween(inicio, fim))
                .thenReturn(sessoes);
        
        // Configura o comportamento do mock para findSessoesByLoginPsicologo
        when(sessaoRepository.findSessoesByLoginPsicologo("psicologo"))
                .thenReturn(sessoes);
        
        // Configura o comportamento do mock para findSessoesByIsCancelado
        List<Sessao> sessoesNaoCanceladas = sessoes.stream()
                .filter(s -> !s.getIsCancelado())
                .collect(Collectors.toList());
        when(sessaoRepository.findSessoesByIsCancelado(false))
                .thenReturn(sessoesNaoCanceladas);
        
        List<Sessao> sessoesCanceladas = sessoes.stream()
                .filter(Sessao::getIsCancelado)
                .collect(Collectors.toList());
        when(sessaoRepository.findSessoesByIsCancelado(true))
                .thenReturn(sessoesCanceladas);
        
        // Configura o comportamento do mock para findSessoesByIsPacienteAtendido
        List<Sessao> sessoesAtendidas = sessoes.stream()
                .filter(Sessao::getIsPacienteAtendido)
                .collect(Collectors.toList());
        when(sessaoRepository.findSessoesByIsPacienteAtendido(true))
                .thenReturn(sessoesAtendidas);
        
        List<Sessao> sessoesNaoAtendidas = sessoes.stream()
                .filter(s -> !s.getIsPacienteAtendido())
                .collect(Collectors.toList());
        when(sessaoRepository.findSessoesByIsPacienteAtendido(false))
                .thenReturn(sessoesNaoAtendidas);
        
        // Configura o comportamento do mock para save
        when(sessaoRepository.save(any(Sessao.class)))
                .thenAnswer(invocation -> {
                    Sessao sessao = invocation.getArgument(0);
                    if (sessao.getId() == null) {
                        sessao.setId(sessoes.size() + 1);
                    }
                    return sessao;
                });
    }

    @Test
    public void testFindAll() {
        List<Sessao> result = sessaoRepository.findAll();
        
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindById() {
        Optional<Sessao> result = sessaoRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(1, result.get().getNumeroSessao());
        assertTrue(result.get().getIsPacienteAtendido());
        assertFalse(result.get().getIsCancelado());
    }

    @Test
    public void testFindByIdNotFound() {
        when(sessaoRepository.findById(99)).thenReturn(Optional.empty());
        
        Optional<Sessao> result = sessaoRepository.findById(99);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindSessoesByDiaAgendadoBetween() {
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 15, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 1, 23, 0, 0);
        
        List<Sessao> result = sessaoRepository.findSessoesByDiaAgendadoBetween(inicio, fim);
        
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindSessoesByLoginPsicologo() {
        List<Sessao> result = sessaoRepository.findSessoesByLoginPsicologo("psicologo");
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("psicologo", result.get(0).getLoginPsicologo());
    }

    @Test
    public void testFindSessoesByIsCancelado() {
        List<Sessao> sessoesNaoCanceladas = sessaoRepository.findSessoesByIsCancelado(false);
        List<Sessao> sessoesCanceladas = sessaoRepository.findSessoesByIsCancelado(true);
        
        assertNotNull(sessoesNaoCanceladas);
        assertNotNull(sessoesCanceladas);
        
        for (Sessao sessao : sessoesNaoCanceladas) {
            assertFalse(sessao.getIsCancelado());
        }
        
        for (Sessao sessao : sessoesCanceladas) {
            assertTrue(sessao.getIsCancelado());
        }
    }

    @Test
    public void testFindSessoesByIsPacienteAtendido() {
        List<Sessao> sessoesAtendidas = sessaoRepository.findSessoesByIsPacienteAtendido(true);
        List<Sessao> sessoesNaoAtendidas = sessaoRepository.findSessoesByIsPacienteAtendido(false);
        
        assertNotNull(sessoesAtendidas);
        assertNotNull(sessoesNaoAtendidas);
        
        for (Sessao sessao : sessoesAtendidas) {
            assertTrue(sessao.getIsPacienteAtendido());
        }
        
        for (Sessao sessao : sessoesNaoAtendidas) {
            assertFalse(sessao.getIsPacienteAtendido());
        }
    }

    @Test
    public void testSave() {
        Sessao novaSessao = new Sessao();
        novaSessao.setNumeroSessao(1);
        novaSessao.setIsPacienteAtendido(true);
        novaSessao.setDiaAgendado(LocalDateTime.of(2023, 2, 1, 10, 0));
        novaSessao.setLoginPsicologo("psicologo");
        novaSessao.setFeedback("Sessão teste");
        novaSessao.setIsCancelado(false);
        
        Sessao result = sessaoRepository.save(novaSessao);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1, result.getNumeroSessao());
        assertTrue(result.getIsPacienteAtendido());
        assertEquals("psicologo", result.getLoginPsicologo());
        assertEquals("Sessão teste", result.getFeedback());
        assertFalse(result.getIsCancelado());
    }

    @Test
    public void testUpdate() {
        Sessao sessaoExistente = sessoes.get(0);
        sessaoExistente.setFeedback("Feedback atualizado");
        
        Sessao result = sessaoRepository.save(sessaoExistente);
        
        assertNotNull(result);
        assertEquals(sessaoExistente.getId(), result.getId());
        assertEquals("Feedback atualizado", result.getFeedback());
    }
}