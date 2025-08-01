package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Especialidade;
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

public class EspecialidadeRepositoryTest {

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    private List<Especialidade> especialidades;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/especialidades.json");
        especialidades = mapper.readValue(is, new TypeReference<List<Especialidade>>() {});
        
        // Configura o comportamento do mock para findAll
        when(especialidadeRepository.findAll()).thenReturn(especialidades);
        
        // Configura o comportamento do mock para findById
        for (Especialidade especialidade : especialidades) {
            when(especialidadeRepository.findById(especialidade.getId()))
                    .thenReturn(Optional.of(especialidade));
        }
        
        // Configura o comportamento do mock para findByDescricao
        for (Especialidade especialidade : especialidades) {
            when(especialidadeRepository.findByDescricao(especialidade.getDescricao()))
                    .thenReturn(especialidade);
        }
        
        // Configura o comportamento do mock para save
        when(especialidadeRepository.save(any(Especialidade.class)))
                .thenAnswer(invocation -> {
                    Especialidade especialidade = invocation.getArgument(0);
                    if (especialidade.getId() == null) {
                        especialidade.setId(especialidades.size() + 1);
                    }
                    return especialidade;
                });
    }

    @Test
    public void testFindAll() {
        List<Especialidade> result = especialidadeRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(especialidades.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Especialidade> result = especialidadeRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(especialidadeRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Especialidade> result = especialidadeRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByDescricao() {
        // Assume que "Infantil" é uma descrição que existe no arquivo JSON
        Especialidade result = especialidadeRepository.findByDescricao("Infantil");
        
        assertNotNull(result);
        assertEquals("Infantil", result.getDescricao());
    }

    @Test
    public void testFindByDescricaoNotFound() {
        when(especialidadeRepository.findByDescricao("Inexistente")).thenReturn(null);
        
        Especialidade result = especialidadeRepository.findByDescricao("Inexistente");
        
        assertNull(result);
    }

    @Test
    public void testSave() {
        Especialidade novaEspecialidade = new Especialidade();
        novaEspecialidade.setDescricao("Nova Especialidade");
        
        Especialidade result = especialidadeRepository.save(novaEspecialidade);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Nova Especialidade", result.getDescricao());
    }
}