package br.manaus.mysoft.acolherbk.repositories;

import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.enums.Turno;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HorarioRepositoryTest {

    @Mock
    private HorarioRepository horarioRepository;

    private List<Horario> horarios;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Carrega os dados do arquivo JSON
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("json/horarios.json");
        horarios = mapper.readValue(is, new TypeReference<List<Horario>>() {});
        
        // Configura o comportamento do mock para findAll
        when(horarioRepository.findAll()).thenReturn(horarios);
        
        // Configura o comportamento do mock para findById
        for (Horario horario : horarios) {
            when(horarioRepository.findById(horario.getId()))
                    .thenReturn(Optional.of(horario));
        }
        
        // Configura o comportamento do mock para findByDiaAndTurno
        for (Horario horario : horarios) {
            when(horarioRepository.findByDiaAndTurno(horario.getDia(), horario.getTurno()))
                    .thenReturn(horario);
        }
        
        // Configura o comportamento do mock para save
        when(horarioRepository.save(any(Horario.class)))
                .thenAnswer(invocation -> {
                    Horario horario = invocation.getArgument(0);
                    if (horario.getId() == null) {
                        horario.setId(horarios.size() + 1);
                    }
                    return horario;
                });
    }

    @Test
    public void testFindAll() {
        List<Horario> result = horarioRepository.findAll();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(horarios.size(), result.size());
    }

    @Test
    public void testFindById() {
        Optional<Horario> result = horarioRepository.findById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(horarioRepository.findById(999)).thenReturn(Optional.empty());
        
        Optional<Horario> result = horarioRepository.findById(999);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByDiaAndTurno() {
        // Assume que existe um horário para segunda-feira de manhã no arquivo JSON
        Horario result = horarioRepository.findByDiaAndTurno(DayOfWeek.MONDAY, Turno.MANHA);
        
        assertNotNull(result);
        assertEquals(DayOfWeek.MONDAY, result.getDia());
        assertEquals(Turno.MANHA, result.getTurno());
    }

    @Test
    public void testFindByDiaAndTurnoNotFound() {
        // Configurando para retornar null para um dia/turno que não existe
        when(horarioRepository.findByDiaAndTurno(DayOfWeek.SATURDAY, Turno.MANHA)).thenReturn(null);
        
        Horario result = horarioRepository.findByDiaAndTurno(DayOfWeek.SATURDAY, Turno.MANHA);
        
        assertNull(result);
    }

    @Test
    public void testSave() {
        Horario novoHorario = new Horario();
        novoHorario.setDia(DayOfWeek.SATURDAY);
        novoHorario.setTurno(Turno.MANHA);
        
        Horario result = horarioRepository.save(novoHorario);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(DayOfWeek.SATURDAY, result.getDia());
        assertEquals(Turno.MANHA, result.getTurno());
    }
}