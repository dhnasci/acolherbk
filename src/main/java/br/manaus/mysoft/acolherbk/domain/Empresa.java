package br.manaus.mysoft.acolherbk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String endereco;
    private String cnpjcpf;
    private Boolean pago;
    private LocalDate cadastro;
    private Integer vencimento;
    private String token;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa")
    private List<Psicologo> psicologos;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa")
    private List<Paciente> pacientes;
}
