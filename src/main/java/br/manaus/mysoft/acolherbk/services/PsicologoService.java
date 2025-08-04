package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import br.manaus.mysoft.acolherbk.utils.TestUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.manaus.mysoft.acolherbk.utils.Constantes.*;

@Service
public class PsicologoService {

    @Autowired
    PsicologoRepository repository;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    private String novaSenha;

    private static SecureRandom secureRandom = new SecureRandom();

    public Psicologo find(Integer id) throws ObjetoException {
        Optional<Psicologo> psi = repository.findById(id);
        return psi.orElseThrow(
                () -> new ObjetoException(PSICOLOGO_NAO_ENCONTRADO)
        );
    }

    public List<Psicologo> listar() {
        return repository.findAll();
    }

    public List<Psicologo> buscarPeloNome(String nome) {
        return repository.findPsicologosByNomeCompletoContainingIgnoreCaseOrderByNomeCompleto(nome);
    }

    public List<Psicologo> listarComSessoesAgendadasNoIntervalo(LocalDateTime inicio, LocalDateTime fim){
        return repository.findPsicologosBySessoesDiaAgendadoBetween(inicio, fim);
    }

    public Psicologo inserir(Psicologo psi) {
        psi.setId(null);
        String novaSenha = generatePassword(TAMANHO_SENHA);
        this.setNovaSenha(novaSenha);
        if (!TestUtils.isRunningUnderTest()) {
            String senha = bcrypt.encode(novaSenha);
            psi.setSenha(senha);
        }
        psi.setCadastro(LocalDateTime.now());
        return repository.save(psi);
    }

    public String generatePassword(int length) {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    public Psicologo reset(Psicologo psi){
        String novaSenha = generatePassword(TAMANHO_SENHA);
        this.setNovaSenha(novaSenha);
        String senha = bcrypt.encode(novaSenha);
        psi.setSenha(senha);
        return repository.save(psi);
    }

    public Psicologo alterar(Psicologo psi) throws ObjetoException {
        Psicologo psicologo = find(psi.getId());
        psi.setSenha(psicologo.getSenha());
        return repository.save(psi);
    }

    public void apagar(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException(ERRO_APAGAR_PSICOLOGO);
        }
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public Psicologo buscarPeloLogin(String nome) {
        return repository.findPsicologoByLogin(nome);
    }

    public Psicologo obterPeloNomeCompleto(String nome) throws ObjetoException {
        try {
            return repository.findPsicologoByNomeCompleto(nome);
        } catch (Exception e) {
            throw new ObjetoException(PSICOLOGO_NAO_ENCONTRADO);
        }
    }
}
