package br.manaus.mysoft.acolherbk.services;

import br.manaus.mysoft.acolherbk.domain.*;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.enums.Turno;
import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.exceptions.PersistenciaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    EscolaridadeService escolaridadeService;

    @Autowired
    EspecialidadeService especialidadeService;

    @Autowired
    GeneroService generoService;

    @Autowired
    ProfissaoService profissaoService;

    @Autowired
    HorarioService horarioService;

    @Autowired
    PsicologoService psicologoService;

    @Autowired
    HorarioPsiService horarioPsiService;

    private Logger log = LoggerFactory.getLogger(DBService.class);

    public void instantiateTestDatabase() throws PersistenciaException, ObjetoException {
        log.info("Iniciando dados");

        if (!isEscolaridadePopulado()) {
            Escolaridade escolaridade1 = new Escolaridade("Básico");
            Escolaridade escolaridade2 = new Escolaridade("Fundamental");
            Escolaridade escolaridade3 = new Escolaridade("Médio");
            Escolaridade escolaridade4 = new Escolaridade("Graduação");
            Escolaridade escolaridade5 = new Escolaridade("Pós-graduação");
            Escolaridade escolaridade6 = new Escolaridade("Mestrado");
            Escolaridade escolaridade7 = new Escolaridade("Doutorado");
            Escolaridade escolaridade8 = new Escolaridade("Pós-doutorado");

            try {
                escolaridadeService.salvar(Arrays.asList(escolaridade1, escolaridade2,
                        escolaridade3, escolaridade4, escolaridade5, escolaridade6,
                        escolaridade7, escolaridade8
                ));
            } catch (Exception e) {
                throw new PersistenciaException("Erro Escolaridade", e);
            }
        }
        if (!isGeneroPopulado()) {
            Genero genero1 = new Genero("Masculino");
            Genero genero2 = new Genero("Feminino");
            try {
                generoService.salvar(Arrays.asList(genero1, genero2));
            } catch (Exception e) {
                throw new PersistenciaException("Erro ao salvar generos", e);
            }
        }
        if (!isProfissaoPopulado()) {
            Profissao profissao1 = new Profissao("Medicina");
            Profissao profissao2 = new Profissao("Engenharia");
            Profissao profissao3 = new Profissao("Advocacia");
            Profissao profissao4 = new Profissao("Análise de Sistemas");
            Profissao profissao5 = new Profissao("Pedreiro");
            Profissao profissao6 = new Profissao("Auxiliar Doméstico");
            Profissao profissao7 = new Profissao("Técnico");
            Profissao profissao8 = new Profissao("Bombeiro");
            Profissao profissao9 = new Profissao("Atendente");
            Profissao profissao10 = new Profissao("Mestre de Obra");
            Profissao profissao11 = new Profissao("Vendedor");
            Profissao profissao12 = new Profissao("Caixa");
            Profissao profissao13 = new Profissao("Jornalismo");
            Profissao profissao14 = new Profissao("Letras");
            Profissao profissao15 = new Profissao("Psicologia");
            Profissao profissao16 = new Profissao("Teologia");
            Profissao profissao17 = new Profissao("Sociologia");
            Profissao profissao18 = new Profissao("Economia");
            Profissao profissao19 = new Profissao("Contabilidade");
            Profissao profissao20 = new Profissao("Fotografia");
            Profissao profissao21 = new Profissao("Analista de Marketing");
            Profissao profissao22 = new Profissao("Analista Financeiro");
            Profissao profissao23 = new Profissao("Cuidador(a) de Idosos");
            Profissao profissao24 = new Profissao("Manicure");
            Profissao profissao25 = new Profissao("Cabelereiro(a)");

            try {
                profissaoService.salvar(Arrays.asList(profissao1, profissao2, profissao3,
                        profissao4, profissao5, profissao6, profissao7, profissao8, profissao9,
                        profissao10, profissao11, profissao12, profissao13, profissao14, profissao15,
                        profissao16, profissao17, profissao18, profissao19, profissao20, profissao21,
                        profissao22, profissao23, profissao24, profissao25));
            } catch (Exception e) {
                throw new PersistenciaException("Erros ao salvar profissões", e);
            }

        }
        if (!isEspecialidadePopulado()){
            Especialidade especialidade1 = new Especialidade("Infantil");
            Especialidade especialidade2 = new Especialidade("Adictos");
            Especialidade especialidade3 = new Especialidade("Idosos");
            Especialidade especialidade4 = new Especialidade("Adultos");
            Especialidade especialidade5 = new Especialidade("Familiar");
            Especialidade especialidade6 = new Especialidade("Adolescentes");
            try {
                especialidadeService.salvar(Arrays.asList(especialidade1, especialidade2,
                        especialidade3, especialidade4, especialidade5, especialidade6));
            } catch (Exception e) {
                throw new PersistenciaException("Erro ao salvar especialidades", e);
            }
        }

        if (!isHorarioPopulado()) {
            Horario horario1 = new Horario(DayOfWeek.TUESDAY, Turno.MANHA);
            Horario horario2 = new Horario(DayOfWeek.TUESDAY, Turno.TARDE);
            Horario horario3 = new Horario(DayOfWeek.TUESDAY, Turno.NOITE);
            Horario horario4 = new Horario(DayOfWeek.WEDNESDAY, Turno.MANHA);
            Horario horario5 = new Horario(DayOfWeek.WEDNESDAY, Turno.TARDE);
            Horario horario6 = new Horario(DayOfWeek.WEDNESDAY, Turno.NOITE);
            Horario horario7 = new Horario(DayOfWeek.THURSDAY, Turno.MANHA);
            Horario horario8 = new Horario(DayOfWeek.THURSDAY, Turno.TARDE);
            Horario horario9 = new Horario(DayOfWeek.THURSDAY, Turno.NOITE);
            Horario horario13 = new Horario(DayOfWeek.SATURDAY, Turno.MANHA);
            Horario horario14 = new Horario(DayOfWeek.SATURDAY, Turno.TARDE);
            Horario horario15 = new Horario(DayOfWeek.SATURDAY, Turno.NOITE);
            try {
                horarioService.salvar(Arrays.asList(horario1, horario2, horario3, horario4,
                        horario5, horario6, horario7, horario8, horario9, horario13, horario14, horario15
                ));
            } catch (Exception e) {
                throw new PersistenciaException("Erro ao salvar horarios", e);
            }

        }

        if (!isPsicologoPopulado()) {
            Psicologo psicologo = new Psicologo();
            psicologo.setNomeCompleto("Edilce Menezes");
            psicologo.setLogin("edilce");
            psicologo.setPerfil(Perfil.ADMIN);
            psicologo.setCelular1("92 98109-1934");
            psicologo.setIsWhatsapp1(false);
            psicologo.setCadastro(LocalDateTime.now());
            psicologo.setCRP("20/08155");
            psicologo.setEmail("edilceangel@gmail.com");
            try {
                psicologoService.inserir(psicologo);
                String senha = "senha :: " + psicologoService.getNovaSenha();
                log.info(senha);
            } catch (Exception e) {
                throw new PersistenciaException("Erro ao salvar psicologo", e);
            }
        }
    }

    private boolean isPsicologoPopulado() {
        return psicologoService.listar().size() > 0;
    }

    public boolean isEscolaridadePopulado() {
        return escolaridadeService.listar().size() > 0;
    }

    public boolean isEspecialidadePopulado() {
        return especialidadeService.listar().size() > 0;
    }

    public boolean isHorarioPopulado() throws ObjetoException {
        return horarioService.find(1)!=null;
    }

    public boolean isGeneroPopulado() {
        return generoService.listar().size() > 0;
    }

    public boolean isProfissaoPopulado() {
        return profissaoService.listar().size() > 0;
    }
}
