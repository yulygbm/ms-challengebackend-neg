package cl.desafio.challengebackend.util;

import cl.desafio.challengebackend.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

/**
 * Clase Utilitaria para realizar peticiones a servicios externos.
 */
@Slf4j
public class HttpClientUtil {

    private HttpClientUtil() {
    }

    /**
     * Llamada a peticiones GET.
     *
     * @param url ruta/endpoint a consultar.
     * @return respuesta de la petición GET.
     * @throws HttpClientException si hay problemas al ejecutar la petición GET.
     */
    public static String executeGetCall(HttpUrl url) throws HttpClientException {

        log.info("[executeGetCall] INICIO - Consulta a la ruta: [{}] ", url);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String respuesta = response.body().string();
                log.info("[executeGetCall] FINOK - Consulta a la ruta con respuesta: [{}] ", respuesta);
                return respuesta;
            } else {
                log.info("[executeGetCall] FINEK - Respuesta no obtenida desde servicio mock");
                throw new HttpClientException("No hay respuesta desde la api externa", response.code());
            }
        } catch (Exception e) {
            log.info("[executeGetCall] FINEK - Servicio no disponible, excepción: [{}]", e.getMessage());
            throw new HttpClientException(e.getMessage(), 500);
        }
    }
}
