package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Triagem;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TriagemRepositoryTest {

    @Mock
    private TriagemRepository triagemRepository;

    private List<Triagem> triagens;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Para suportar LocalDateTime
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/triagens.json");
        triagens = mapper.readValue(is, new TypeReference<List<Triagem>>() {});
        
        // Configura o comportamento do mock para findAll
        when(triagemRepository.findAll()).thenReturn(triagens);
        
        // Configura o comportamento do mock para findById
        for (Triagem triagem : triagens) {
            when(triagemRepository.findById(triagem.getId()))
                    .thenReturn(Optional.of(triagem));
        }
        
        // Configura o comportamento do mock para save
        when(triagemRepository.save(any(Triagem.class)))
                .thenAnswer(invocation -> {
                    Triagem triagem = invocation.getArgument(0);
                    if (triagem.getId() == null) {
                        triagem.setId(triagens.size() + 1);
                    }
                    return triagem;
                });
    }

    @Test
    public void testFindAll() {
        List<Triagem> result = triagemRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(triagens.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Triagem> result = triagemRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertTrue(result.get().getIsPacienteAlocado());
    }

    @Test
    public void testFindByIdNotFound() {
        when(triagemRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Triagem> result = triagemRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testSave() {
        Triagem novaTriagem = new Triagem();
        novaTriagem.setIsPacienteAlocado(false);
        novaTriagem.setIsCancelado(false);
        
        Triagem result = triagemRepository.save(novaTriagem);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertFalse(result.getIsPacienteAlocado());
        assertFalse(result.getIsCancelado());
    }

    @Test
    public void testUpdate() {
        // Obtém uma triagem existente
        Optional<Triagem> triagemOpt = triagemRepository.findById(1);
        assertTrue(triagemOpt.isPresent());
        
        Triagem triagem = triagemOpt.get();
        
        // Atualiza os dados
        triagem.setIsPacienteAlocado(true);
        triagem.setDiaAlocacao(LocalDateTime.now());
        triagem.setLoginAlocador("admin");
        
        // Salva a atualização
        Triagem result = triagemRepository.save(triagem);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertTrue(result.getIsPacienteAlocado());
        assertNotNull(result.getDiaAlocacao());
        assertEquals("admin", result.getLoginAlocador());
    }
}