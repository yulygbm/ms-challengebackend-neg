package cl.desafio.challengebackend.service.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Clase que gestiona el valor de la suma, indicando el porcentaje adicional.
 */
@Getter
@Builder
public class SumaConPorcentajeResponse {
    private final double numero1;
    private final double numero2;
    private final double suma;
    private final double porcentaje;
}
