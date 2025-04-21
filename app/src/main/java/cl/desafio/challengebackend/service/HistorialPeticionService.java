package cl.desafio.challengebackend.service;

import cl.desafio.challengebackend.model.HistorialPeticionDto;
import cl.desafio.challengebackend.service.domain.SumaConPorcentajeResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * Lógica de negocio asociada a la gestión del historial de peticiones realizadas
 * por el cliente.
 */
public interface HistorialPeticionService {

    /**
     * Obtener todos los registros del historial de peticiones.
     *
     * @param offset inicio consulta.
     * @param limit  límite de registros a consultar.
     * @return los registros del historial de peticiones
     */
    List<HistorialPeticionDto> obtenerHistorialPeticionPorPaginacion(int offset, int limit);

    /**
     * Registrar en la base de datos el historial de la petición realizada por el cliente.
     * Este métdo se ejecuta de manera asíncrona.
     *
     * @param sumaConPorcentajeResponse respuesta a guardar.
     * @param urlPeticion               endpoint a guardar.
     *                                  * @return petición registrada de manera persistente.
     */
    @Async
    HistorialPeticionDto guardarHistorialPeticion(SumaConPorcentajeResponse sumaConPorcentajeResponse, String urlPeticion);
}