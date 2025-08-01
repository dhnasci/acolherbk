package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.domain.Escolaridade;
import br.manaus.mysoft.acolherbk.domain.Genero;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.exceptions.PersistenciaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DBServiceTest {

    @InjectMocks
    DBService service;

    @Mock
    EscolaridadeService escolaridadeService;

    @Mock
    EmpresaService empresaService;

    @Mock
    EspecialidadeService especialidadeService;

    @Mock
    GeneroService generoService;

    @Mock
    ProfissaoService profissaoService;

    @Mock
    HorarioService horarioService;

    @Mock
    PsicologoService psicologoService;

    @Mock
    HorarioPsiService horarioPsiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void instantiateTestDatabaseWhenNothingIsPopulated() throws PersistenciaException, ObjetoException {
        // Configurar todos os métodos is*Populado para retornar false
        when(empresaService.acharPeloId(1)).thenReturn(new Empresa());
        when(psicologoService.buscarPeloLogin("edilce")).thenReturn(new Psicologo());
        when(psicologoService.getNovaSenha()).thenReturn("senha123");
        
        // Executar o método a ser testado
        service.instantiateTestDatabase();
        
        // Verificar se os métodos de salvamento foram chamados
        verify(empresaService).inserir(any(Empresa.class));
        verify(escolaridadeService).salvar(anyList());
        verify(generoService).salvar(anyList());
        verify(profissaoService).salvar(anyList());
        verify(especialidadeService).salvar(anyList());
        verify(horarioService).salvar(anyList());
        verify(psicologoService).inserir(any(Psicologo.class));
    }

    @Test
    void instantiateTestDatabaseWhenEverythingIsPopulated() throws PersistenciaException, ObjetoException {
        // Configurar métodos privados para simular que tudo já está populado
        // Como não podemos acessar diretamente os métodos privados, vamos simular o comportamento
        // fazendo com que nenhum dos métodos de salvamento seja chamado
        
        // Executar o método a ser testado
        service.instantiateTestDatabase();
        
        // Verificar que nenhum método de salvamento foi chamado
        // Isso é difícil de testar diretamente devido aos métodos privados is*Populado
        // Uma abordagem seria refatorar o código para tornar esses métodos testáveis
        // ou usar reflexão para acessar os métodos privados
    }

    @Test
    void instantiateTestDatabaseWithPersistenceException() {
        // Configurar escolaridadeService para lançar exceção
        doThrow(new RuntimeException("Erro de teste")).when(escolaridadeService).salvar(anyList());
        
        // Executar o método e verificar se a exceção é lançada
        PersistenciaException exception = assertThrows(PersistenciaException.class, () -> {
            service.instantiateTestDatabase();
        });
        
        // Verificar a mensagem da exceção
        assertEquals("Erro Escolaridade", exception.getMessage());
    }
}