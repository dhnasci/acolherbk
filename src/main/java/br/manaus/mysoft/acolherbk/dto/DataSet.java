package br.manaus.mysoft.acolherbk.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataSet {

    private List<Integer> data;
    private List<String> backgroundColor;
    private String label;
    private List<String> borderColor;
    private Integer borderWidth;
}
