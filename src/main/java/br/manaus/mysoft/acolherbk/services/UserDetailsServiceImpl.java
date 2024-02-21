package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import br.manaus.mysoft.acolherbk.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.manaus.mysoft.acolherbk.utils.Constantes.PSICOLOGO_NAO_ENCONTRADO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PsicologoRepository repository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Psicologo psicologo = repository.findPsicologoByLogin(login);
        if (psicologo.equals(null)) {
            throw new UsernameNotFoundException(PSICOLOGO_NAO_ENCONTRADO);
        }
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(psicologo.getPerfil());
        return new UserSS(psicologo.getId(), psicologo.getLogin(), psicologo.getSenha(), perfis);
    }
}
