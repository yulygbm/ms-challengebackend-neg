package cl.desafio.challengebackend.client;

import cl.desafio.challengebackend.exception.HttpClientException;
import cl.desafio.challengebackend.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Esta clase permite consultar el porcentaje dinámico de un servicio externo.
 */
@Slf4j
@Component
public class PorcentajeDinamicoMockClient {

    @Value("${external.api-porcentaje.url}")
    private String urlPorcentaje;

    /**
     * Consulta el porcentaje dinámico de un servicio externo y lo guarda en caché por 10 minutos.
     * Si el servcio externo no puede ser consultado, se obtiene el porcentaje de la caché.
     * Si no funciona la caché se entrega un mensaje de error.
     *
     * @return porcentaje dinámico.
     */
    @Cacheable(value = "api-externa", key = "porcentaje")
    @Retryable(retryFor = {HttpClientException.class}, maxAttemptsExpression = "${external.api-porcentaje.retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${external.api-porcentaje.retry.maxDelay}"))
    public int getPorcentajeDinamico() throws HttpClientException {
        Random random = new Random();
        int porcentajeMock = random.nextInt(100) + 1;
        int resultPorcentaje = 0;

        log.info("[getPorcentajeDinamico] INICIO - Consulta del porcentaje dinámico");

        try (MockWebServer mockWebServer = new MockWebServer()) {
            //Se mockea la respuesta del servicio externo, indicando un porcentaje.
            MockResponse mockResponse = new MockResponse()
                    .addHeader("Content-Type", "application/json")
                    .setBody(String.valueOf(porcentajeMock));
            mockWebServer.enqueue(mockResponse);
            mockWebServer.start();
            HttpUrl baseUrl = mockWebServer.url(urlPorcentaje);
            String porcentajeApi = HttpClientUtil.executeGetCall(baseUrl);
            resultPorcentaje = Integer.parseInt(porcentajeApi);
            log.info("[getPorcentajeDinamico] FINOK - Se obtuvo el porcentaje dinámico [{}] desde la ruta [{}]",
                    porcentajeApi, urlPorcentaje);
            mockWebServer.shutdown();
        } catch (Exception e) {
            assert e instanceof HttpClientException;
            log.error("[getPorcentajeDinamico] FINEX - Excepción en el proceso de consulta del porcentaje: [{}]",
                    ((HttpClientException) e).getMensaje());
            throw new HttpClientException(((HttpClientException) e).getMensaje());
        }
        return resultPorcentaje;
    }

}
