package br.manaus.mysoft.acolherbk.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {
    public static LocalDateTime converteParaData(String inicio) {
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(inicio, formatoDia);
    }
}
