package cl.desafio.challengebackend.exception;

import cl.desafio.challengebackend.exception.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas asociadas a la gestión de excepciones.
 */
@ExtendWith(MockitoExtension.class)
class ValidationExceptionsHandlerTest {

    @InjectMocks
    private ValidationExceptionsHandler validationExceptionsHandler;

    @Mock
    private WebRequest webRequest;

    private final String MENSAJE_BAD_REQUEST = "Parámetros de entrada incorrectos";
    private final String MENSAJE_GENERIC_REQUEST = "Problemas de comunicación";

    @BeforeEach
    public void setUp() {
        validationExceptionsHandler = new ValidationExceptionsHandler();
    }

    @Test
    void validarParametroFaltantePeticion() {
        //given
        MissingServletRequestParameterException missingServletRequestParameterException =
                mock(MissingServletRequestParameterException.class);
        when(missingServletRequestParameterException.getParameterName()).thenReturn("num1");

        //when
        ResponseEntity<Object> objectResult = validationExceptionsHandler.handleMissingServletRequestParameter(
                missingServletRequestParameterException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        //then
        assertNotNull(objectResult);
        assertEquals(HttpStatus.BAD_REQUEST, objectResult.getStatusCode());
        assertNotNull(objectResult.getBody());
        assertEquals(MENSAJE_BAD_REQUEST, ((ErrorResponseDto) objectResult.getBody()).getMessage());
        assertEquals(1, ((ErrorResponseDto) objectResult.getBody()).getErrores().size());
        assertEquals("num1 parámetro faltante", ((ErrorResponseDto) objectResult.getBody()).getErrores()
                .getFirst());
    }

    @Test
    void validarArgumentoNoValidoPeticion() {
        //given
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("num1", "num1"
                , "num1 no puede ser vacío")));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        //when
        ResponseEntity<Object> objectResult = validationExceptionsHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        //then
        assertNotNull(objectResult);
        assertEquals(HttpStatus.BAD_REQUEST, objectResult.getStatusCode());
        assertNotNull(objectResult.getBody());
        assertEquals(MENSAJE_BAD_REQUEST, ((ErrorResponseDto) objectResult.getBody()).getMessage());
        assertEquals(1, ((ErrorResponseDto) objectResult.getBody()).getErrores().size());
        assertEquals("num1 no puede ser vacío", ((ErrorResponseDto) objectResult.getBody()).getErrores()
                .getFirst());
    }

    @Test
    void validarViolacionRestriccionesParametrosPeticion() {
        //given
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> constraintViolationSet = new HashSet<>();
        var constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getMessage()).thenReturn("num2 no es de tipo numérico");
        constraintViolationSet.add(constraintViolation);
        when(constraintViolationException.getConstraintViolations()).thenReturn(constraintViolationSet);

        //when
        ResponseEntity<ErrorResponseDto> objectResult = validationExceptionsHandler
                .handleConstraintViolation(constraintViolationException);

        //then
        assertNotNull(objectResult);
        assertEquals(HttpStatus.BAD_REQUEST, objectResult.getStatusCode());
        assertNotNull(objectResult.getBody());
        assertEquals(MENSAJE_BAD_REQUEST, objectResult.getBody().getMessage());
        assertEquals(1, objectResult.getBody().getErrores().size());
        assertEquals("num2 no es de tipo numérico", objectResult.getBody().getErrores().getFirst());
    }


    @Test
    void validarRecursoNoEncontrado() {
        //given
        NoResourceFoundException noResourceFoundException =
                mock(NoResourceFoundException.class);

        //when
        ResponseEntity<Object> objectResult = validationExceptionsHandler
                .handleNoResourceFoundException(noResourceFoundException, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                        webRequest);

        //then
        assertNotNull(objectResult);
        assertEquals(HttpStatus.NOT_FOUND, objectResult.getStatusCode());
        assertNotNull(objectResult.getBody());
        assertEquals(MENSAJE_GENERIC_REQUEST, ((ErrorResponseDto) objectResult.getBody()).getMessage());
        assertEquals(1, ((ErrorResponseDto) objectResult.getBody()).getErrores().size());
        assertEquals("No se encuentró el recurso solicitado", ((ErrorResponseDto) objectResult.getBody())
                .getErrores().getFirst());
    }

    @Test
    void validarServicioNoDisponible() {
        //given
        ServerErrorException serverErrorException = mock(ServerErrorException.class);

        //when
        ResponseEntity<ErrorResponseDto> objectResult = validationExceptionsHandler
                .handleGenericException(serverErrorException);

        //then
        assertNotNull(objectResult);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, objectResult.getStatusCode());
        assertNotNull(objectResult.getBody());
        assertEquals(MENSAJE_GENERIC_REQUEST, objectResult.getBody().getMessage());
        assertEquals(1, objectResult.getBody().getErrores().size());
        assertEquals("Servicio no disponible", objectResult.getBody().getErrores().getFirst());
    }

}