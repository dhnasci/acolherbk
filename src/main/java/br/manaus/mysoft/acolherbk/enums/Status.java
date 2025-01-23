package br.manaus.mysoft.acolherbk.enums;

public enum Status {
    EM_ATENDIMENTO(1, "EM ATENDIMENTO"),
    ATENDIDO(2,  "ATENDIDO"),
    CANCELADO(3, "CANCELADO"),
    PENDENTE(4, "PENDENTE"),
    TRIAGEM(5, "TRIAGEM");

    private final Integer codigo;
    private final String nome;

    // Construtor do enum
    Status(Integer codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    // Métodos getter
    public Integer getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    // Método para buscar um Status pelo código
    public static Status fromCodigo(Integer codigo) {
        for (Status s : Status.values()) {
            if (s.getCodigo().equals(codigo)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    // Método para buscar um Status pelo nome
    public static Status fromNome(String nome) {
        for (Status s : Status.values()) {
            if (s.getNome().equalsIgnoreCase(nome)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Nome inválido: " + nome);
    }
}
