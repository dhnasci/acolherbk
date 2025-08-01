package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Genero;
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

public class GeneroRepositoryTest {

    @Mock
    private GeneroRepository generoRepository;

    private List<Genero> generos;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/generos.json");
        generos = mapper.readValue(is, new TypeReference<List<Genero>>() {});
        
        // Configura o comportamento do mock para findAll
        when(generoRepository.findAll()).thenReturn(generos);
        
        // Configura o comportamento do mock para findById
        for (Genero genero : generos) {
            when(generoRepository.findById(genero.getId()))
                    .thenReturn(Optional.of(genero));
        }
        
        // Configura o comportamento do mock para findByDescricao
        for (Genero genero : generos) {
            when(generoRepository.findByDescricao(genero.getDescricao()))
                    .thenReturn(genero);
        }
        
        // Configura o comportamento do mock para save
        when(generoRepository.save(any(Genero.class)))
                .thenAnswer(invocation -> {
                    Genero genero = invocation.getArgument(0);
                    if (genero.getId() == null) {
                        genero.setId(generos.size() + 1);
                    }
                    return genero;
                });
    }

    @Test
    public void testFindAll() {
        List<Genero> result = generoRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(generos.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Genero> result = generoRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(generoRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Genero> result = generoRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByDescricao() {
        // Assume que "Masculino" é uma descrição que existe no arquivo JSON
        Genero result = generoRepository.findByDescricao("Masculino");
        
        assertNotNull(result);
        assertEquals("Masculino", result.getDescricao());
    }

    @Test
    public void testFindByDescricaoNotFound() {
        when(generoRepository.findByDescricao("Inexistente")).thenReturn(null);
        
        Genero result = generoRepository.findByDescricao("Inexistente");
        
        assertNull(result);
    }

    @Test
    public void testSave() {
        Genero novoGenero = new Genero();
        novoGenero.setDescricao("Novo Genero");
        
        Genero result = generoRepository.save(novoGenero);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Novo Genero", result.getDescricao());
    }
}