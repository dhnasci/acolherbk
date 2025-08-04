package br.manaus.mysoft.acolherbk.integration;

import br.manaus.mysoft.acolherbk.domain.Empresa;
import br.manaus.mysoft.acolherbk.domain.Paciente;
import br.manaus.mysoft.acolherbk.domain.Psicologo;
import br.manaus.mysoft.acolherbk.domain.Sessao;
import br.manaus.mysoft.acolherbk.enums.Perfil;
import br.manaus.mysoft.acolherbk.repositories.EmpresaRepository;
import br.manaus.mysoft.acolherbk.repositories.PacienteRepository;
import br.manaus.mysoft.acolherbk.repositories.PsicologoRepository;
import br.manaus.mysoft.acolherbk.repositories.SessaoRepository;
import br.manaus.mysoft.acolherbk.services.DBService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.id.GUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SessaoControllerIntegrationTest {

    private Logger log = LoggerFactory.getLogger(SessaoControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PsicologoRepository psicologoRepository;

    @Autowired
    private DBService serviceDB;

    private ObjectMapper objectMapper;
    private Sessao sessao;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sessao = new Sessao();
        sessao.setLoginPsicologo("psicologo1");
        sessao.setDiaAgendado(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        sessao.setIsCancelado(false);
        sessao.setIsPacienteAtendido(false);
        Empresa empresa = Empresa.builder()
                .cadastro(LocalDateTime.now())
                .cnpjcpf("000000000000")
                .nome("Empresa 1")
                .token("bbf335d2-f0c6-4dc2-8b30-8d0058073030")
                .email("teste@email.com")
                .pago(true)
                .usuario("usuario1")
                .vencimento(2)
                .endereco("Rua daqui, 10 - Em algum bairro")
                .build();
        try {
            Empresa resposta = empresaRepository.save(empresa);
            log.info("salvou empresa :: " + resposta.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        Paciente paciente = Paciente.builder()
                .cadastro(LocalDateTime.now())
                .id(1)
                .nomeCompleto("Sicrano")
                .generoid(1)
                .idade(30)
                .empresa(empresa)
                .build();
        pacienteRepository.save(paciente);
        sessao.setPaciente(paciente);
        Psicologo psicologo = Psicologo.builder()
                .id(1)
                .cadastro(LocalDateTime.now())
                .nomeCompleto("fulano")
                .login("usuario1")
                .crp("11111-25")
                .perfil(Perfil.ADMIN)
                .senha("minhaSenha")
                .celular1("(92) 9999-8888")
                .empresa(empresa)
                .build();
        psicologoRepository.save(psicologo);
        sessao.setPsicologo(psicologo);
    }

    @Test
    void deveListarTodasSessoes() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.get("/sessao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists());
    }

    @Test
    void deveBuscarSessaoPorId() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.get("/sessao/{id}", sessaoSalva.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sessaoSalva.getId()));
    }

    @Test
    void deveBuscarSessoesPorIntervalo() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        LocalDate dataInicio = LocalDate.now().minusDays(1);
        LocalDate dataFim = LocalDate.now().plusDays(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/sessao/intervalo")
                .param("dataInicio", dataInicio.format(DateTimeFormatter.ISO_DATE))
                .param("dataFim", dataFim.format(DateTimeFormatter.ISO_DATE))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void deveBuscarSessoesPorLoginPsicologo() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.get("/sessao/psicologo/{login}", "psicologo1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void deveBuscarSessoesCanceladas() throws Exception {
        sessao.setIsCancelado(true);
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.get("/sessao/canceladas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void deveBuscarSessoesAtendidas() throws Exception {


        sessao.setIsPacienteAtendido(true);
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.get("/sessao/atendidas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void deveInserirSessao() throws Exception {
        String sessaoJson = objectMapper.writeValueAsString(sessao);

        mockMvc.perform(MockMvcRequestBuilders.post("/sessao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessaoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.loginPsicologo").value("psicologo1"));
    }

    @Test
    void deveAtualizarSessao() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);
        sessaoSalva.setLoginPsicologo("psicologo2");

        String sessaoJson = objectMapper.writeValueAsString(sessaoSalva);

        mockMvc.perform(MockMvcRequestBuilders.put("/sessao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessaoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sessaoSalva.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loginPsicologo").value("psicologo2"));
    }

    @Test
    void deveCancelarSessao() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.put("/sessao/{id}/cancelar", sessaoSalva.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sessaoSalva.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isCancelado").value(true));
    }

    @Test
    void deveRegistrarAtendimento() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.put("/sessao/{id}/atender", sessaoSalva.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sessaoSalva.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isPacienteAtendido").value(true));
    }

    @Test
    void deveApagarSessao() throws Exception {
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        mockMvc.perform(MockMvcRequestBuilders.delete("/sessao/{id}", sessaoSalva.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}