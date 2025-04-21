package cl.desafio.challengebackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

/**
 * Personalizar la configuración de la caché para establecer el tiempo de expiración a 30 minutos
 */
@Configuration
public class RedisCacheConfig {

    @Value("${spring.redis.time-to-live}")
    private long timeToLive;

    @Bean
    public RedisCacheConfiguration cacheConfiguration(){
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(timeToLive)).disableCachingNullValues();
    }
}
