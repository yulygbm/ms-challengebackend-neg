package cl.desafio.challengebackend.client;

import cl.desafio.challengebackend.exception.HttpClientException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba la consulta hacia un servicio externo.
 */
@EnableRetry
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"external.api-porcentaje.retry.maxAttempts=1",
        "external.api-porcentaje.retry.maxDelay=10"})
class PorcentajeDinamicoMockClientTest {

    @InjectMocks
    private PorcentajeDinamicoMockClient porcentajeClient;

    @BeforeEach
    void setUp() {
        porcentajeClient = new PorcentajeDinamicoMockClient();
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Se consulta el servicio externo del porcentaje de manera exitosa.
     */
    @Test
    void getPorcentajeDinamicoOK() {
        //given
        ReflectionTestUtils.setField(porcentajeClient, "urlPorcentaje", "/porcentaje-dinamico");
        //when
        int respuesta = porcentajeClient.getPorcentajeDinamico();
        //then
        assertTrue(respuesta > 0);
    }

    /**
     * El servicio externo no funciona, se lee de la caché.
     */
    @Test
    void getPorcentajeDinamicoNOK() {
        //given
        ReflectionTestUtils.setField(porcentajeClient, "urlPorcentaje", "http://external-api/porcentaje-dinamico");
        //when
        HttpClientException exception = assertThrows(HttpClientException.class, () -> porcentajeClient.getPorcentajeDinamico());
        //then
        assertEquals("Host desconocido (external-api)", exception.getMensaje());
    }
}