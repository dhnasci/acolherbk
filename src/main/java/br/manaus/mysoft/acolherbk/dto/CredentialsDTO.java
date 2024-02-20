package br.manaus.mysoft.acolherbk.dto;

import java.io.Serializable;

public class CredentialsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private String senha;

    public CredentialsDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
