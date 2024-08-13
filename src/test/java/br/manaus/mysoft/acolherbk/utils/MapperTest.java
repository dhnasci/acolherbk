package br.manaus.mysoft.acolherbk.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void converteParaData() {
        LocalDateTime resposta = Mapper.converteParaData("20-08-24 10:50");
        Assertions.assertNotNull(resposta);
    }
}