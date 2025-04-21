package cl.desafio.challengebackend.util;

import cl.desafio.challengebackend.exception.HttpClientException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria del servicio externo que entrega el porcentaje dinámico.
 */
@ExtendWith(MockitoExtension.class)
class HttpClientUtilTest {

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * Realiza una llamada al servicio externo de manera correcta.
     *
     * @throws IOException              error durante la petición.
     * @throws NoResourceFoundException no puede conectarse con el servicio.
     */
    @Test
    void executeGetCallOK() throws IOException, NoResourceFoundException {
        String respuesta = mockExecuteCall(200, "{\"porcentaje\": \"45\"}");
        assertEquals("{\"porcentaje\": \"45\"}", respuesta);
    }

    /**
     * Realiza una llamada al servicio externo de manera correcta.
     *
     * @throws IOException error durante la petición.
     */
    @Test()
    void executeGetCallNOK() {
        Throwable exception = assertThrows(HttpClientException.class, () -> mockExecuteCall(400, Strings.EMPTY));
        assertNull(exception.getMessage());
    }

    /**
     * Mockear llamada a petición api externa.
     *
     * @param statusCode código http.
     * @param body       mock respuesta petición externa.
     * @return respuesta servicio.
     */
    private String mockExecuteCall(int statusCode, String body) {
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(statusCode)
                .setBody(body);
        mockWebServer.enqueue(mockResponse);
        HttpUrl baseUrl = mockWebServer.url("/porcentaje-dinamico");
        return HttpClientUtil.executeGetCall(baseUrl);
    }
}