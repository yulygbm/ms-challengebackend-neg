package cl.desafio.challengebackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase que representa la tabla HistorialPeticion en la base de datos.
 */
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "historialpeticion", schema = "calculos")
public class HistorialPeticion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String endpoint;
    private String parametros;
    private String respuesta;
    private LocalDateTime fechaCreacion;
}