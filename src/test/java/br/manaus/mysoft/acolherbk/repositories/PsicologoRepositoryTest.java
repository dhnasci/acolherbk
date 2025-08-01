package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.enums.Perfil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PsicologoRepositoryTest {

    @Mock
    private PsicologoRepository psicologoRepository;

    private List<Psicologo> psicologos;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Para suportar LocalDateTime
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/psicologos.json");
        psicologos = mapper.readValue(is, new TypeReference<List<Psicologo>>() {});
        
        // Configura o comportamento do mock para findAll
        when(psicologoRepository.findAll()).thenReturn(psicologos);
        
        // Configura o comportamento do mock para findById
        for (Psicologo psicologo : psicologos) {
            when(psicologoRepository.findById(psicologo.getId()))
                    .thenReturn(Optional.of(psicologo));
        }
        
        // Configura o comportamento do mock para findPsicologoByLogin
        for (Psicologo psicologo : psicologos) {
            when(psicologoRepository.findPsicologoByLogin(psicologo.getLogin()))
                    .thenReturn(psicologo);
        }
        
        // Configura o comportamento do mock para findPsicologoByNomeCompleto
        for (Psicologo psicologo : psicologos) {
            when(psicologoRepository.findPsicologoByNomeCompleto(psicologo.getNomeCompleto()))
                    .thenReturn(psicologo);
        }
        
        // Configura o comportamento do mock para findPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto
        when(psicologoRepository.findPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto(any(String.class)))
                .thenAnswer(invocation -> {
                    String nome = invocation.getArgument(0);
                    return psicologos.stream()
                            .filter(p -> p.getNomeCompleto().toLowerCase().contains(nome.toLowerCase()))
                            .collect(Collectors.toList());
                });
        
        // Configura o comportamento do mock para findPsicologosBySessoesDiaAgendadoBetween
        when(psicologoRepository.findPsicologosBySessoesDiaAgendadoBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(psicologos));
        
        // Configura o comportamento do mock para save
        when(psicologoRepository.save(any(Psicologo.class)))
                .thenAnswer(invocation -> {
                    Psicologo psicologo = invocation.getArgument(0);
                    if (psicologo.getId() == null) {
                        psicologo.setId(psicologos.size() + 1);
                    }
                    return psicologo;
                });
    }

    @Test
    public void testFindAll() {
        List<Psicologo> result = psicologoRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(psicologos.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Psicologo> result = psicologoRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Psicologo Teste", result.get().getNomeCompleto());
    }

    @Test
    public void testFindByIdNotFound() {
        when(psicologoRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Psicologo> result = psicologoRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindPsicologoByLogin() {
        Psicologo result = psicologoRepository.findPsicologoByLogin("psicologo");
        
        assertNotNull(result);
        assertEquals("psicologo", result.getLogin());
        assertEquals(Perfil.PSICOLOGO, result.getPerfil());
    }

    @Test
    public void testFindPsicologoByLoginNotFound() {
        when(psicologoRepository.findPsicologoByLogin("inexistente")).thenReturn(null);
        
        Psicologo result = psicologoRepository.findPsicologoByLogin("inexistente");
        
        assertNull(result);
    }

    @Test
    public void testFindPsicologoByNomeCompleto() {
        Psicologo result = psicologoRepository.findPsicologoByNomeCompleto("Psicologo Teste");
        
        assertNotNull(result);
        assertEquals("Psicologo Teste", result.getNomeCompleto());
    }

    @Test
    public void testFindPsicologoByNomeCompletoNotFound() {
        when(psicologoRepository.findPsicologoByNomeCompleto("Inexistente")).thenReturn(null);
        
        Psicologo result = psicologoRepository.findPsicologoByNomeCompleto("Inexistente");
        
        assertNull(result);
    }

    @Test
    public void testFindPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto() {
        List<Psicologo> result = psicologoRepository.findPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto("Teste");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getNomeCompleto().contains("Teste")));
    }

    @Test
    public void testFindPsicologosBySessoesDiaAgendadoBetween() {
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 12, 31, 23, 59);
        
        List<Psicologo> result = psicologoRepository.findPsicologosBySessoesDiaAgendadoBetween(inicio, fim);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSave() {
        Psicologo novoPsicologo = new Psicologo();
        novoPsicologo.setNomeCompleto("Novo Psicologo");
        novoPsicologo.setLogin("novopsi");
        novoPsicologo.setSenha("senha");
        novoPsicologo.setPerfil(Perfil.PSICOLOGO);
        novoPsicologo.setCelular1("92999999999");
        novoPsicologo.setEmail("novo@psicologo.com");
        
        Psicologo result = psicologoRepository.save(novoPsicologo);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Novo Psicologo", result.getNomeCompleto());
        assertEquals("novopsi", result.getLogin());
    }
}