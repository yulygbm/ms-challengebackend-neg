package cl.desafio.challengebackend.repository;

import cl.desafio.challengebackend.entity.HistorialPeticion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Esta interfaz proporciona acceso a los datos de la tabla HistorialPetciones
 * en la base de datos.
 */
public interface HistorialPeticionRepository extends JpaRepository<HistorialPeticion, Integer> {

    /**
     * Consulta el historial de peticiones por paginación.
     *
     * @param offset posición de inicio de consulta.
     * @param limit  cantidad de registros que traerá la consulta.
     * @return registros consultados.
     */
    @Query(value = "select hp.* from calculos.historialpeticion hp order by id limit :limit " +
            "offset :offset ", nativeQuery = true)
    List<HistorialPeticion> findHistorialPeticiones(int offset, int limit);
}
