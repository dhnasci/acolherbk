package br.manaus.mysoft.acolherbk;

import br.manaus.mysoft.acolherbk.exceptions.ObjetoException;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcolherbkApplication implements CommandLineRunner {

    private static boolean isMigration = false;
    private static boolean desenvolvimento = true;
    private static final String MINHA_URL = "jdbc:sqlserver://localhost;databaseName=acolher";
    private static final String MINHA_URL_PROD = "jdbc:sqlserver://eu-az-sql-serv1.database.windows.net:1433;database=d3yn0bi94s8zvey";
    private static final String MEU_LOGIN = "sa";
    private static final String MEU_LOGIN_PROD = "umg0nch6d8p4brs";
    private static final String SENHA = "MeuChapa15";
    private static final String SENHA_PROD = "J9rr7TkGpaTfrL4XjTZhqsMbf";
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
            // flyway.baseline();
            // flyway.repair();
            // Executa as migrações
            flyway.migrate();
        }
        SpringApplication.run(AcolherbkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
