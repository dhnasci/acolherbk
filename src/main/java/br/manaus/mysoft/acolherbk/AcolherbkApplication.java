package br.manaus.mysoft.acolherbk;

import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcolherbkApplication implements CommandLineRunner {

    private static boolean isMigration = true;
    private static final String MINHA_URL = System.getenv("SPRING_DATASOURCE_URL");
    private static final String MEU_LOGIN = System.getenv("SPRING_DATASOURCE_USERNAME");
    private static final String SENHA = System.getenv("SPRING_DATASOURCE_PASSWORD");
    private static final String FLYWAY_LOCATION = "classpath:db/migration" ;


    public static void main(String[] args) throws ObjetoException {
        if (isMigration) {
            Flyway flyway = null;
            try {
                    flyway = Flyway.configure()
                            .dataSource(MINHA_URL, MEU_LOGIN, SENHA)
                            .locations(FLYWAY_LOCATION)
                            .load();

            } catch (Exception e) {
                throw new ObjetoException("Problema no flyway");
            }

            // Primeira vez
            //flyway.baseline();
            //flyway.repair();
            // Executa as migrações
            flyway.migrate();
        }
        SpringApplication.run(AcolherbkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
