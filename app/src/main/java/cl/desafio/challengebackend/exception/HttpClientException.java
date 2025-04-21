package cl.desafio.challengebackend.exception;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class HttpClientException extends RuntimeException {

    private String mensaje;
    private int statusCode;

    public HttpClientException(){}

    public HttpClientException(@NotBlank String mensaje) {
        super(mensaje);
        this.mensaje = mensaje;
    }

    public HttpClientException(@NotBlank String mensaje, int statusCode){
        this.mensaje = mensaje;
        this.statusCode = statusCode;
    }
}
