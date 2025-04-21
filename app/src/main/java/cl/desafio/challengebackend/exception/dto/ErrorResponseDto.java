package cl.desafio.challengebackend.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase para el envío de errores en las peticiones.
 */
@Getter
public class ErrorResponseDto {
    private final LocalDateTime date;
    private final HttpStatus status;
    private final String message;
    private final List<String> errores;


    /**
     * Constructor con parámetros.
     *
     * @param status:  código del estado HTTP.
     * @param message: Mensaje genérico de error asociado a la excepción.
     * @param errores: Lista con el detalle de los errores.
     */
    public ErrorResponseDto(HttpStatus status, String message, List<String> errores) {
        this.date = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errores = errores;
    }
}