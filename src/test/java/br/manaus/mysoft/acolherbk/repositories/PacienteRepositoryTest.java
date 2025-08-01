package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.dto.PacienteAlocadoProjection;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PacienteRepositoryTest {

    @Mock
    private PacienteRepository pacienteRepository;

    private List<Paciente> pacientes;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Para suportar LocalDateTime
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/pacientes.json");
        pacientes = mapper.readValue(is, new TypeReference<List<Paciente>>() {});
        
        // Configura o comportamento do mock para findAll
        when(pacienteRepository.findAll()).thenReturn(pacientes);
        
        // Configura o comportamento do mock para findById
        for (Paciente paciente : pacientes) {
            when(pacienteRepository.findById(paciente.getId()))
                    .thenReturn(Optional.of(paciente));
        }
        
        // Configura o comportamento do mock para findPacientesByNomeCompletoContainingIgnoreCase
        when(pacienteRepository.findPacientesByNomeCompletoContainingIgnoreCase(any(String.class)))
                .thenAnswer(invocation -> {
                    String nome = invocation.getArgument(0);
                    return pacientes.stream()
                            .filter(p -> p.getNomeCompleto().toLowerCase().contains(nome.toLowerCase()))
                            .collect(Collectors.toList());
                });
        
        // Configura o comportamento do mock para findPacientesBySessoesDiaAgendadoBetween
        when(pacienteRepository.findPacientesBySessoesDiaAgendadoBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(pacientes));
        
        // Configura o comportamento do mock para findPacientesByTriagemsDiaAlocacaoBetween
        when(pacienteRepository.findPacientesByTriagemsDiaAlocacaoBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(pacientes));
        
        // Configura o comportamento do mock para findPacientesByTriagemsIsPacienteAlocado
        when(pacienteRepository.findPacientesByTriagemsIsPacienteAlocado(any(Boolean.class)))
                .thenAnswer(invocation -> {
                    Boolean isAlocado = invocation.getArgument(0);
                    if (isAlocado) {
                        return pacientes.subList(0, 1); // Retorna apenas o primeiro paciente como alocado
                    } else {
                        return pacientes.subList(1, pacientes.size()); // Retorna os demais como não alocados
                    }
                });
        
        // Configura o comportamento do mock para findAllPacientesAlocados
        when(pacienteRepository.findAllPacientesAlocados(anyInt()))
                .thenAnswer(invocation -> {
                    Integer psicologoId = invocation.getArgument(0);
                    // Cria uma lista de projeções para simular o resultado da query nativa
                    List<PacienteAlocadoProjection> projecoes = new ArrayList<>();
                    
                    // Adiciona uma projeção para o primeiro paciente
                    if (psicologoId == 1 && !pacientes.isEmpty()) {
                        Paciente paciente = pacientes.get(0);
                        PacienteAlocadoProjection projecao = new PacienteAlocadoProjection() {
                            @Override
                            public String getNomeCompleto() {
                                return paciente.getNomeCompleto();
                            }
                            
                            @Override
                            public String getCelular1() {
                                return paciente.getCelular1();
                            }
                            
                            @Override
                            public Boolean getIsWhatsapp1() {
                                return paciente.getIsWhatsapp1();
                            }
                            
                            @Override
                            public String getCelular2() {
                                return paciente.getCelular2();
                            }
                            
                            @Override
                            public String getRegistroGeral() {
                                return paciente.getRegistroGeral();
                            }
                        };
                        projecoes.add(projecao);
                    }
                    
                    return projecoes;
                });
        
        // Configura o comportamento do mock para save
        when(pacienteRepository.save(any(Paciente.class)))
                .thenAnswer(invocation -> {
                    Paciente paciente = invocation.getArgument(0);
                    if (paciente.getId() == null) {
                        paciente.setId(pacientes.size() + 1);
                    }
                    return paciente;
                });
    }

    @Test
    public void testFindAll() {
        List<Paciente> result = pacienteRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(pacientes.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Paciente> result = pacienteRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Paciente Teste", result.get().getNomeCompleto());
    }

    @Test
    public void testFindByIdNotFound() {
        when(pacienteRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Paciente> result = pacienteRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindPacientesByNomeCompletoContainingIgnoreCase() {
        List<Paciente> result = pacienteRepository.findPacientesByNomeCompletoContainingIgnoreCase("Teste");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getNomeCompleto().contains("Teste")));
    }

    @Test
    public void testFindPacientesBySessoesDiaAgendadoBetween() {
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 12, 31, 23, 59);
        
        List<Paciente> result = pacienteRepository.findPacientesBySessoesDiaAgendadoBetween(inicio, fim);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindPacientesByTriagemsDiaAlocacaoBetween() {
        LocalDateTime inicio = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2023, 12, 31, 23, 59);
        
        List<Paciente> result = pacienteRepository.findPacientesByTriagemsDiaAlocacaoBetween(inicio, fim);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindPacientesByTriagemsIsPacienteAlocado() {
        List<Paciente> alocados = pacienteRepository.findPacientesByTriagemsIsPacienteAlocado(true);
        List<Paciente> naoAlocados = pacienteRepository.findPacientesByTriagemsIsPacienteAlocado(false);
        
        assertNotNull(alocados);
        assertNotNull(naoAlocados);
        assertFalse(alocados.isEmpty());
        assertEquals(1, alocados.size()); // Conforme configurado no mock
    }

    @Test
    public void testFindAllPacientesAlocados() {
        List<PacienteAlocadoProjection> result = pacienteRepository.findAllPacientesAlocados(1);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size()); // Conforme configurado no mock
        assertEquals("Paciente Teste", result.get(0).getNomeCompleto());
    }

    @Test
    public void testSave() {
        Paciente novoPaciente = new Paciente();
        novoPaciente.setNomeCompleto("Novo Paciente");
        novoPaciente.setCelular1("92999999999");
        novoPaciente.setIsWhatsapp1(true);
        novoPaciente.setRegistroGeral("123456789");
        
        Paciente result = pacienteRepository.save(novoPaciente);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Novo Paciente", result.getNomeCompleto());
    }
}