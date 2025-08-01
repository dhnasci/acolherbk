package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.repositories.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.manaus.mysoft.acolherbk.utils.Constantes.ERRO_APAGAR_SESSAO;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository repository;

    public Sessao inserir(Sessao sessao) {
        return repository.save(sessao);
    }

    public Sessao buscarPeloId(Integer id) throws ObjetoException {
        Optional<Sessao> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjetoException("Sessão não encontrada com id: " + id));
    }

    public List<Sessao> listar() {
        return repository.findAll();
    }

    public List<Sessao> buscarSessoesNoIntervalo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findSessoesByDiaAgendadoBetween(inicio, fim);
    }

    public List<Sessao> buscarSessoesPorLoginPsicologo(String loginPsicologo) {
        return repository.findSessoesByLoginPsicologo(loginPsicologo);
    }

    public List<Sessao> buscarSessoesCanceladas(Boolean isCancelado) {
        return repository.findSessoesByIsCancelado(isCancelado);
    }

    public List<Sessao> buscarSessoesAtendidas(Boolean isPacienteAtendido) {
        return repository.findSessoesByIsPacienteAtendido(isPacienteAtendido);
    }

    public Sessao atualizar(Sessao sessao) {
        return repository.save(sessao);
    }

    public void cancelarSessao(Integer id, String motivoCancelamento) throws ObjetoException {
        Sessao sessao = buscarPeloId(id);
        sessao.setIsCancelado(true);
        sessao.setMotivoCancelamento(motivoCancelamento);
        repository.save(sessao);
    }

    public void registrarAtendimento(Integer id, String feedback) throws ObjetoException {
        Sessao sessao = buscarPeloId(id);
        sessao.setIsPacienteAtendido(true);
        sessao.setFeedback(feedback);
        repository.save(sessao);
    }

    public void apagar(Integer id) throws ObjetoException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ObjetoException(ERRO_APAGAR_SESSAO);
        }
    }

    public void salvar(List<Sessao> lista) {
        repository.saveAll(lista);
    }
}