package cl.desafio.challengebackend.model;


import java.time.LocalDateTime;

/**
 * Clase DTO para transferir data entre el servidor y el cliente.
 *
 * @param id            identificador.
 * @param endpoint      petición realizada.
 * @param parametros    argumentos de entrada.
 * @param respuesta     respuesta de la petición realizada.
 * @param fechaCreacion fecha y hora en que se realiza la petición.
 */
public record HistorialPeticionDto(Integer id, String endpoint, String parametros, String respuesta
        , LocalDateTime fechaCreacion) {
}
