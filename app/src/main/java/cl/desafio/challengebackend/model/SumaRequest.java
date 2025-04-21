package cl.desafio.challengebackend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

/**
 * Clase para el manejo de los números a sumar.
 */
@Getter
public class SumaRequest {

    private static final String REG_EXP_NUMEROS_DOBLES = "^[-+]?\\d*[.]?\\d+|^[-+]?\\d+[.]?\\d*";

    @NotNull(message = "El primer número no puede ser nulo")
    @NotBlank(message = "El primer número no puede ser vacío")
    @Pattern(regexp = REG_EXP_NUMEROS_DOBLES, message = "El primer número no es de tipo numérico")
    private String numero1;

    @NotNull(message = "El segundo número no puede ser nulo")
    @NotBlank(message = "El segundo número no puede ser vacío")
    @Pattern(regexp = REG_EXP_NUMEROS_DOBLES, message = "El segundo número no es de tipo numérico")
    private String numero2;

}
