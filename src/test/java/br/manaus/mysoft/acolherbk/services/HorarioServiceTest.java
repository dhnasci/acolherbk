package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.enums.Turno;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.junit.Assert.*;

public class HorarioServiceTest {

    @Test
    public void getByDescricao() {
        String horario = "terça-feira - MANHA";
        String[] lista = horario.split("-");
        Turno turno = Turno.valueOf(lista[1]);
        DayOfWeek dia = DayOfWeek.valueOf(lista[0]);
        System.out.println(turno.name());
        System.out.println(dia.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-br")));

    }
}