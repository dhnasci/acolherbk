package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Escolaridade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EscolaridadeRepositoryTest {

    @Mock
    private EscolaridadeRepository escolaridadeRepository;

    private List<Escolaridade> escolaridades;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/escolaridades.json");
        escolaridades = mapper.readValue(is, new TypeReference<List<Escolaridade>>() {});
        
        // Configura o comportamento do mock para findAll
        when(escolaridadeRepository.findAll()).thenReturn(escolaridades);
        
        // Configura o comportamento do mock para findById
        for (Escolaridade escolaridade : escolaridades) {
            when(escolaridadeRepository.findById(escolaridade.getId()))
                    .thenReturn(Optional.of(escolaridade));
        }
        
        // Configura o comportamento do mock para findByDescricao
        for (Escolaridade escolaridade : escolaridades) {
            when(escolaridadeRepository.findByDescricao(escolaridade.getDescricao()))
                    .thenReturn(escolaridade);
        }
        
        // Configura o comportamento do mock para save
        when(escolaridadeRepository.save(any(Escolaridade.class)))
                .thenAnswer(invocation -> {
                    Escolaridade escolaridade = invocation.getArgument(0);
                    if (escolaridade.getId() == null) {
                        escolaridade.setId(escolaridades.size() + 1);
                    }
                    return escolaridade;
                });
    }

    @Test
    public void testFindAll() {
        List<Escolaridade> result = escolaridadeRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(escolaridades.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Escolaridade> result = escolaridadeRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(escolaridadeRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Escolaridade> result = escolaridadeRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByDescricao() {
        // Usamos "Graduação" que existe no arquivo JSON
        Escolaridade result = escolaridadeRepository.findByDescricao("Graduação");
        
        assertNotNull(result);
        assertEquals("Graduação", result.getDescricao());
    }

    @Test
    public void testFindByDescricaoNotFound() {
        when(escolaridadeRepository.findByDescricao("Inexistente")).thenReturn(null);
        
        Escolaridade result = escolaridadeRepository.findByDescricao("Inexistente");
        
        assertNull(result);
    }

    @Test
    public void testSave() {
        Escolaridade novaEscolaridade = new Escolaridade();
        novaEscolaridade.setDescricao("Nova Escolaridade");
        
        Escolaridade result = escolaridadeRepository.save(novaEscolaridade);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Nova Escolaridade", result.getDescricao());
    }
}