package br.manaus.mysoft.acolherbk.config;

import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import br.manaus.mysoft.acolherbk.exceptions.PersistenciaException;
import br.manaus.mysoft.acolherbk.services.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Profile("test")
@Configuration
public class TestConfig {

    private Logger log = LoggerFactory.getLogger(TestConfig.class);

    DBService dbService;

    @Autowired
    public TestConfig(DBService dbService) {
        this.dbService = dbService;
    }

    // liberando da autenticação para os testes de integração
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();

        http.authorizeRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    //@Bean
    //public BCryptPasswordEncoder bcryptPasswordEncoder() {
      //  return new BCryptPasswordEncoder();
    //}

    @Bean
    public boolean populaBancoH2() {
        try {
            dbService.instantiateTestDatabase();
        } catch (PersistenciaException e) {
            log.error(e.getMessage());
        } catch (ObjetoException e1) {
            log.error(e1.getMessage());
        }
        return true;
    }
}
