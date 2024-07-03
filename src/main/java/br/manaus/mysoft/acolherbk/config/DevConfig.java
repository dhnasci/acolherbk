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

@Configuration
@Profile("dev")
public class DevConfig {

    private Logger log = LoggerFactory.getLogger(DevConfig.class);

    DBService dbService;

    @Autowired
    public DevConfig(DBService dbService) {
        this.dbService = dbService;
    }

    @Bean
    public boolean instantiateDatabase() {
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
