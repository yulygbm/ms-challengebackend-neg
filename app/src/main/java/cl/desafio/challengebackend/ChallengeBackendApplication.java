package cl.desafio.challengebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Clase principal.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableRetry
@EnableCaching
public class ChallengeBackendApplication {

    /**
     * Clase principal.
     *
     * @param args si se ingresa parámetros por consola.
     */
    public static void main(String[] args) {
        SpringApplication.run(ChallengeBackendApplication.class, args);
    }
}
