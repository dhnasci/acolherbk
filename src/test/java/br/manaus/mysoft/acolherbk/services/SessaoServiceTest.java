package br.manaus.mysoft.acolherbk.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class SessaoServiceTest {

    private static final String DIA_AGENDADO = "20-08-24 10:30";
    private static final String DIA_AGENDADO_BD = "2024-08-09 18:30:00.0";
    @InjectMocks
    SessaoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void extraiHora() {
        String hora = service.extraiHora(DIA_AGENDADO);
        Assertions.assertNotNull(hora);
    }

    @Test
    void extraiDia() {
        String dia = service.extraiDia(DIA_AGENDADO);
        assertNotNull(dia);
    }

    @Test
    void extraiHoraBD() {
        String hora = service.extraiHoraBD(DIA_AGENDADO_BD);
        assertNotNull(hora);
    }

    @Test
    void extraiDiaBD() {
        String dia = service.extraiDiaBD(DIA_AGENDADO_BD);
        assertNotNull(dia);
    }
}