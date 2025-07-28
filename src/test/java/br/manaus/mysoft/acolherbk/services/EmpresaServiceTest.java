package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpresaServiceTest {

    @InjectMocks
    EmpresaService service;
    @Mock
    Empresa empresa;
    @Mock
    EmpresaRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(empresa)).thenReturn(new Empresa());
    }

    @Test
    void inserir() {
        Empresa resposta = service.inserir(empresa);
        assertNotNull(resposta);
    }

    @Test
    void acharPeloId() throws ObjetoException {
        Empresa empresa = new Empresa(); // você pode setar atributos se necessário
        Optional<Empresa> empresaOpt = Optional.of(empresa); // cria o Optional corretamente
        when(repository.findById(1)).thenReturn(empresaOpt);
        Empresa resp = service.acharPeloId(1);
        assertNotNull(resp);
    }

    @Test
    void acharPeloIdNaoEncontrado() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjetoException.class, () -> service.acharPeloId(1));
    }

    @Test
    void listarEmpresas() {
        List<Empresa> listaDeEmpresas = new ArrayList<>();
        listaDeEmpresas.add(empresa);
        when(repository.findAll()).thenReturn(listaDeEmpresas);
        List<Empresa> lista = service.listarEmpresas();
        assertNotNull(lista);
    }

    @Test
    void atualizar() {
        Empresa resposta = service.atualizar(empresa);
        assertNotNull(resposta);
    }

    @Test
    void apagarPeloId() throws ObjetoException {
        service.apagarPeloId(1);
        verify(repository).deleteById(1);
    }

    @Test
    void apagarPeloId_DeveLancarObjetoExceptionQuandoRepositoryFalhar() {
        // Arrange
        Integer id = 1;
        doThrow(new RuntimeException("Erro interno")).when(repository).deleteById(id);

        // Act & Assert
        ObjetoException exception = assertThrows(ObjetoException.class, () -> {
            service.apagarPeloId(id);
        });

        assertEquals("Erro ao apagar Empresa", exception.getMessage());
    }
}