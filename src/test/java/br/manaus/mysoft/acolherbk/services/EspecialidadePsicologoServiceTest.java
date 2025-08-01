package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.EspecialidadePsicologo;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.EspecialidadePsicologoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EspecialidadePsicologoServiceTest {

    @InjectMocks
    EspecialidadePsicologoService service;

    @Mock
    EspecialidadePsicologoRepository repository;

    @Mock
    EspecialidadePsicologo especialidadePsicologo1;

    @Mock
    EspecialidadePsicologo especialidadePsicologo2;

    @Mock
    Psicologo psicologo;

    List<EspecialidadePsicologo> listaEspecialidadesPsicologo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repository.save(especialidadePsicologo1)).thenReturn(especialidadePsicologo1);
        Optional<EspecialidadePsicologo> especialidadePsicologoOpt = Optional.of(especialidadePsicologo1);
        when(repository.findById(1)).thenReturn(especialidadePsicologoOpt);
        listaEspecialidadesPsicologo = Arrays.asList(especialidadePsicologo1, especialidadePsicologo2);
        when(repository.findAll()).thenReturn(listaEspecialidadesPsicologo);
        when(repository.findAllByPsicologo(psicologo)).thenReturn(listaEspecialidadesPsicologo);
    }

    @Test
    void insert() {
        EspecialidadePsicologo resp = service.insert(especialidadePsicologo1);
        assertEquals(especialidadePsicologo1, resp);
    }

    @Test
    void find() throws ObjetoException {
        EspecialidadePsicologo resposta = service.find(1);
        assertEquals(especialidadePsicologo1, resposta);
    }

    @Test
    void findNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        ObjetoException exception = assertThrows(ObjetoException.class, () -> service.find(2));
        assertTrue(exception.getMessage().contains("EspecialidadePsicologo nao encontrada"));
    }

    @Test
    void listar() {
        List<EspecialidadePsicologo> listagem = service.listar();
        assertEquals(2, listagem.size());
    }

    @Test
    void getByPsicologo() {
        List<EspecialidadePsicologo> listagem = service.getByPsicologo(psicologo);
        assertEquals(2, listagem.size());
        verify(repository).findAllByPsicologo(psicologo);
    }

    @Test
    void update() {
        EspecialidadePsicologo resposta = service.update(especialidadePsicologo1);
        assertEquals(especialidadePsicologo1, resposta);
    }

    @Test
    void delete() throws ObjetoException {
        service.delete(1);
        verify(repository).deleteById(1);
    }

    @Test
    void erroNoDelete() {
        doThrow(new RuntimeException("Erro ao apagar EspecialidadePsicologo")).when(repository).deleteById(1);

        ObjetoException ex = assertThrows(ObjetoException.class,
                () -> service.delete(1)
        );

        assertEquals("Erro ao apagar EspecialidadePsicologo", ex.getMessage());
    }

    @Test
    void salvar() {
        service.salvar(listaEspecialidadesPsicologo);
        verify(repository).saveAll(listaEspecialidadesPsicologo);
    }
}