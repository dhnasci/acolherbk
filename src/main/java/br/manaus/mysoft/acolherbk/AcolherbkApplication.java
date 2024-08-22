package br.manaus.mysoft.acolherbk;

import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcolherbkApplication implements CommandLineRunner {

    private static boolean isMigration = true;
    private static boolean desenvolvimento = false;
    private static final String MINHA_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String MINHA_URL_PROD = "jdbc:postgresql://cbib4a865d7s88.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com";
    private static final String MEU_LOGIN = "postgres";
    private static final String MEU_LOGIN_PROD = "u5fmceib9av9a3";
    private static final String SENHA = "Selenio17!";
    private static final String SENHA_PROD = "pd1b1fbf744b0fccf9ee624e593fbee87d7631ba767615c997c661bb2476b79b8";
    private static final String FLYWAY_LOCATION = "classpath:db/migration" ;


    public static void main(String[] args) throws ObjetoException {
        if (isMigration) {
            Flyway flyway = null;
            try {
                if (desenvolvimento) {
                    flyway = Flyway.configure()
                            .dataSource(MINHA_URL, MEU_LOGIN, SENHA)
                            .locations(FLYWAY_LOCATION)
                            .load();
                } else {
                    flyway = Flyway.configure()
                            .dataSource(MINHA_URL_PROD, MEU_LOGIN_PROD, SENHA_PROD)
                            .locations(FLYWAY_LOCATION)
                            .load();
                }

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
