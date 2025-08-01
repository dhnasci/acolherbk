package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

public class EmpresaRepositoryTest {

    @Mock
    private EmpresaRepository empresaRepository;

    private List<Empresa> empresas;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Para suportar LocalDateTime
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/empresas.json");
        empresas = mapper.readValue(is, new TypeReference<List<Empresa>>() {});
        
        // Configura o comportamento do mock para findAll
        when(empresaRepository.findAll()).thenReturn(empresas);
        
        // Configura o comportamento do mock para findById
        for (Empresa empresa : empresas) {
            when(empresaRepository.findById(empresa.getId()))
                    .thenReturn(Optional.of(empresa));
        }
        
        // Configura o comportamento do mock para save
        when(empresaRepository.save(any(Empresa.class)))
                .thenAnswer(invocation -> {
                    Empresa empresa = invocation.getArgument(0);
                    if (empresa.getId() == null) {
                        empresa.setId(empresas.size() + 1);
                    }
                    return empresa;
                });
    }

    @Test
    public void testFindAll() {
        List<Empresa> result = empresaRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(empresas.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Empresa> result = empresaRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Sistema Acolher", result.get().getNome());
    }

    @Test
    public void testFindByIdNotFound() {
        when(empresaRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Empresa> result = empresaRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testSave() {
        Empresa novaEmpresa = new Empresa();
        novaEmpresa.setNome("Nova Empresa");
        novaEmpresa.setCnpjcpf("11111111111111");
        novaEmpresa.setUsuario("novousuario");
        novaEmpresa.setEmail("novo@empresa.com");
        
        Empresa result = empresaRepository.save(novaEmpresa);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Nova Empresa", result.getNome());
        assertEquals("11111111111111", result.getCnpjcpf());
    }
}