package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;

import java.util.List;

public interface EmpresaServices {

    Empresa inserir(Empresa empresa);
    Empresa acharPeloId(Integer id) throws ObjetoException;
    List<Empresa> listarEmpresas();
    Empresa atualizar(Empresa empresa);
    void apagarPeloId(Integer id) throws ObjetoException;
}
