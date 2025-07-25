package br.manaus.mysoft.acolherbk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cnpjcpf;
    private String usuario;
    private String email;
    private String endereco;
    private Boolean pago;
    private LocalDateTime cadastro;
    private Integer vencimento;
    private String token;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa")
    private List<Paciente> pacientes;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa")
    private List<Psicologo> psicologos;
}
