package cl.desafio.challengebackend.service.mapper;

import cl.desafio.challengebackend.entity.HistorialPeticion;
import cl.desafio.challengebackend.model.HistorialPeticionDto;
import org.jetbrains.annotations.NotNull;

/**
 * Clase que permite transformar un objeto para el manero de la información de Historial de peticiones.
 */
public class HistorialPeticionMapper {

    private HistorialPeticionMapper() {
    }

    /**
     * Convierte un objeto de tipo DTO a Entity.
     * @param historialPeticionDto objeto DTO.
     * @return objeto de tipo Entity.
     */
    public static HistorialPeticion convertToEntity(@NotNull HistorialPeticionDto historialPeticionDto){
        HistorialPeticion historialPeticion = new HistorialPeticion();
        historialPeticion.setFechaCreacion(historialPeticionDto.fechaCreacion());
        historialPeticion.setEndpoint(historialPeticionDto.endpoint());
        historialPeticion.setParametros(historialPeticionDto.parametros());
        historialPeticion.setRespuesta(historialPeticionDto.respuesta());
        return historialPeticion;
    }

    /**
     * Convierte un objeto de tipo Entity a DTO.
     * @param historialPeticion objeto entity.
     * @return objeto de tipo DTO.
     */
    public static HistorialPeticionDto convertToDto(@NotNull HistorialPeticion historialPeticion){
        return new HistorialPeticionDto(historialPeticion.getId(), historialPeticion.getEndpoint()
                , historialPeticion.getParametros(), historialPeticion.getRespuesta()
                , historialPeticion.getFechaCreacion());
    }
}
