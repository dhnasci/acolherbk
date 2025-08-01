package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import br.manaus.mysoft.acolherbk.security.UserSS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static br.manaus.mysoft.acolherbk.utils.Constantes.PSICOLOGO_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl service;

    @Mock
    PsicologoRepository repository;

    @Mock
    Psicologo psicologo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(psicologo.getId()).thenReturn(1);
        when(psicologo.getLogin()).thenReturn("usuario_teste");
        when(psicologo.getSenha()).thenReturn("senha_teste");
        when(psicologo.getPerfil()).thenReturn(Perfil.ADMIN);
        when(repository.findPsicologoByLogin("usuario_teste")).thenReturn(psicologo);
    }

    @Test
    void loadUserByUsername() {
        UserDetails userDetails = service.loadUserByUsername("usuario_teste");
        
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof UserSS);
        UserSS userSS = (UserSS) userDetails;
        
        assertEquals(1, userSS.getId());
        assertEquals("usuario_teste", userSS.getUsername());
        assertEquals("senha_teste", userSS.getPassword());
        assertTrue(userSS.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN")));
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(repository.findPsicologoByLogin("usuario_inexistente")).thenReturn(null);
        
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("usuario_inexistente");
        });
        
        assertEquals(PSICOLOGO_NAO_ENCONTRADO, exception.getMessage());
    }
}