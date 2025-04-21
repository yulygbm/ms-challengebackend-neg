package cl.desafio.challengebackend.exception;

import cl.desafio.challengebackend.exception.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de Excepciones.
 * Esta clase permite gestionar excepciones de solicitudes incorrectas de tipo 4xx y 5xx.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ValidationExceptionsHandler extends ResponseEntityExceptionHandler {

    private final String MENSAJE_BAD_REQUEST = "Parámetros de entrada incorrectos";
    private final String MENSAJE_GENERIC_REQUEST = "Problemas de comunicación";

    /**
     * Esta excepción se lanza a la solicitud le falta un parámetro.
     *
     * @param ex      excepción lanzada de tipo MissingServletRequestParameterException.
     * @param headers cabecera petición.
     * @param status  código del estado HTTP.
     * @param request petición.
     * @return información de la excepción controlada.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getParameterName() + " parámetro faltante";
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST, MENSAJE_BAD_REQUEST
                , List.of(error));
        log.error("[handleMissingServletRequestParameter] FINEX - Mensaje excepción:  [{}]", ex.getMessage());

        return new ResponseEntity<Object>(errorResponseDto, errorResponseDto.getStatus());
    }

    /**
     * Esta excepción se lanza cuando un argumento con anotación @Valid no pasa la validación.
     *
     * @param ex      excepción lanzada de tipo MethodArgumentNotValidException.
     * @param headers cabecera petición.
     * @param status  código del estado HTTP.
     * @param request petición.
     * @return información de la excepción controlada.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST, MENSAJE_BAD_REQUEST, errores);
        log.error("[handleMethodArgumentNotValid] FINEX - Mensaje excepción:  [{}]", ex.getMessage());
        return new ResponseEntity<Object>(errorResponseDto, errorResponseDto.getStatus());
    }

    /**
     * Esta excepción se lanza cuando no se encuentra el recurso consultado.
     *
     * @param ex      excepción lanzada de tipo NoResourceFoundException.
     * @param headers cabecera petición.
     * @param status  código del estado HTTP.
     * @param request petición.
     * @return información de la excepción controlada.
     */
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND
                , MENSAJE_GENERIC_REQUEST, List.of("No se encuentró el recurso solicitado"));
        log.error("[handleNoResourceFoundException] FINEX - Mensaje excepción:  [{}]", ex.getMessage());
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    /**
     * Esta excepción informa el resultado de las violaciones de restricciones.
     *
     * @param exception excepción de tipo ConstraintViolationException.
     * @return Información de la excepción controlada.
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException exception) {
        List<String> errores = exception.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST, MENSAJE_BAD_REQUEST, errores);
        log.error("[handleConstraintViolation] FINEX - Mensaje excepción:  [{}]", exception.getMessage());
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    /**
     * Esta excepción se lanza cuando hay problemas de comunicación con el servicio.
     *
     * @param exception excepción de tipo Exception.
     * @return Información de la excepción controlada.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR
                , MENSAJE_GENERIC_REQUEST, List.of("Servicio no disponible"));
        log.error("[handleGenericException] FINEX - Mensaje excepción:  [{}]", exception.getMessage());
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
