package cl.desafio.challengebackend.service;

import cl.desafio.challengebackend.client.PorcentajeDinamicoMockClient;
import cl.desafio.challengebackend.exception.HttpClientException;
import cl.desafio.challengebackend.model.SumaRequest;
import cl.desafio.challengebackend.service.domain.SumaConPorcentajeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Lógica asociada a los cálculos dinámicos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CalculoDinamicoService {

    private final PorcentajeDinamicoMockClient porcentajeDinamicoMockClient;

    /**
     * Calcular suma de dos números más un porcentaje dinámico
     *
     * @param sumaRequest contiene el primer número y segundo número a sumar.
     * @return suma de dos números + un porcentaje dinámico
     */
    public SumaConPorcentajeResponse calcularSumaConPorcentajeDinamico(SumaRequest sumaRequest) {

        log.info("[calcularSumaConPorcentajeDinamico] INICIO - suma de los números: [{}] y [{}]",
                sumaRequest.getNumero1(), sumaRequest.getNumero2());
        double porcentaje = 0;
        try {
            porcentaje = porcentajeDinamicoMockClient.getPorcentajeDinamico();
        } catch (HttpClientException e) {
            log.error("[calcularSumaConPorcentajeDinamico] FINEX - No se pudo consultar el porcentaje dinámico, " +
                    "excepción [{}]", e.getMensaje());
        }
        double suma = Double.parseDouble(sumaRequest.getNumero1()) + Double.parseDouble(sumaRequest.getNumero2())
                + porcentaje;

        log.info("[calcularSumaConPorcentajeDinamico] FINOK - suma de los números: [{}] + [{}] + [{}](porcentaje " +
                "dinámico) = [{}]", sumaRequest.getNumero1(), sumaRequest.getNumero2(), porcentaje, suma);
        return SumaConPorcentajeResponse.builder().numero1(Double.parseDouble(sumaRequest.getNumero1()))
                .numero2(Double.parseDouble(sumaRequest.getNumero2())).suma(suma).porcentaje(porcentaje).build();
    }
}
