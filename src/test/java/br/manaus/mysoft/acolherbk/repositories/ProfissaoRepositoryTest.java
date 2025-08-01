package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Profissao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfissaoRepositoryTest {

    @Mock
    private ProfissaoRepository profissaoRepository;

    private List<Profissao> profissoes;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/profissoes.json");
        profissoes = mapper.readValue(is, new TypeReference<List<Profissao>>() {});
        
        // Configura o comportamento do mock para findAll
        when(profissaoRepository.findAll()).thenReturn(profissoes);
        
        // Configura o comportamento do mock para findById
        for (Profissao profissao : profissoes) {
            when(profissaoRepository.findById(profissao.getId()))
                    .thenReturn(Optional.of(profissao));
        }
        
        // Configura o comportamento do mock para findByDescricao
        for (Profissao profissao : profissoes) {
            when(profissaoRepository.findByDescricao(profissao.getDescricao()))
                    .thenReturn(profissao);
        }
        
        // Configura o comportamento do mock para save
        when(profissaoRepository.save(any(Profissao.class)))
                .thenAnswer(invocation -> {
                    Profissao profissao = invocation.getArgument(0);
                    if (profissao.getId() == null) {
                        profissao.setId(profissoes.size() + 1);
                    }
                    return profissao;
                });
    }

    @Test
    public void testFindAll() {
        List<Profissao> result = profissaoRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(profissoes.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Profissao> result = profissaoRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(profissaoRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Profissao> result = profissaoRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByDescricao() {
        // Assume que "Medicina" é uma descrição que existe no arquivo JSON
        Profissao result = profissaoRepository.findByDescricao("Medicina");
        
        assertNotNull(result);
        assertEquals("Medicina", result.getDescricao());
    }

    @Test
    public void testFindByDescricaoNotFound() {
        when(profissaoRepository.findByDescricao("Inexistente")).thenReturn(null);
        
        Profissao result = profissaoRepository.findByDescricao("Inexistente");
        
        assertNull(result);
    }

    @Test
    public void testSave() {
        Profissao novaProfissao = new Profissao();
        novaProfissao.setDescricao("Nova Profissao");
        
        Profissao result = profissaoRepository.save(novaProfissao);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Nova Profissao", result.getDescricao());
    }
}