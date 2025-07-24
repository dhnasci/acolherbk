package br.manaus.mysoft.acolherbk;

import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcolherbkApplication implements CommandLineRunner {

    private static boolean isMigration = true;
    private static boolean desenvolvimento = true;
    private static final String MINHA_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String MINHA_URL_PROD = "jdbc:postgresql://c97r84s7psuajm.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/d4jgr2qoeh16lc";
    private static final String MEU_LOGIN = "postgres";
    private static final String MEU_LOGIN_PROD = "u5fmceib9av9a3";
    private static final String SENHA = "Selenio17!";
    private static final String SENHA_PROD = "p7ea5c6562070b4d4f6739169290d9f9465486085c76d1ca967d78762a4542f73";
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
