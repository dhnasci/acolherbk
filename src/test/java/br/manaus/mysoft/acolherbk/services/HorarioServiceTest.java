package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Horario;
import br.manaus.mysoft.acolherbk.enums.Turno;
import br.manaus.mysoft.acolherbk.repositories.HorarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

import static br.manaus.mysoft.acolherbk.services.HorarioService.PIPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class HorarioServiceTest {

    @InjectMocks
    HorarioService service;

    @Mock
    HorarioRepository repository;

    @Mock Horario horarioMock;

    String horarioStr;



    @Test
    void getByDescricao() {
        
        String[] lista = horarioStr.split(PIPE);
        Turno turno = Turno.valueOf(lista[1]);
        DayOfWeek dia = DayOfWeek.valueOf(lista[0]);
        System.out.println(turno.name());
        System.out.println(dia.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")));

    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        horarioStr = "THURSDAY|MANHA";

    }

    @Test
    void testaConversao() {
        DayOfWeek dia = DayOfWeek.THURSDAY;
        Turno turno = Turno.MANHA;
        when(repository.findByDiaAndTurno(dia, turno)).thenReturn(horarioMock);
        Horario horario = service.getByDescricao(horarioStr);
        Assertions.assertNotNull(horario);
    }
}