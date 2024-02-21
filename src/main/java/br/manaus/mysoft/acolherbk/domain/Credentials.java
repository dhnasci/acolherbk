package br.manaus.mysoft.acolherbk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credentials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private String senha;

}
