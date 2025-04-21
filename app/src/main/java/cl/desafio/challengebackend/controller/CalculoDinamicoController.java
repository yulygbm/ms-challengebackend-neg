package cl.desafio.challengebackend.controller;

import cl.desafio.challengebackend.model.HistorialPeticionDto;
import cl.desafio.challengebackend.model.SumaRequest;
import cl.desafio.challengebackend.service.CalculoDinamicoService;
import cl.desafio.challengebackend.service.domain.SumaConPorcentajeResponse;
import cl.desafio.challengebackend.service.impl.HistorialPeticionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

/**
 * Peticiones Rest asociadas a cálculos dinámicos.
 */
@RestController
@RequestMapping("/calculo-dinamico")
@Validated
@RequiredArgsConstructor
public class CalculoDinamicoController {

    private static final String REG_EXP_NUMEROS_ENTEROS = "\\d*";
    private final CalculoDinamicoService calculoDinamicoService;
    private final HistorialPeticionServiceImpl historialPeticionService;

    /**
     * Permite realizar la suma de dos números más un porcentaje dinámico,
     * también registra esta petición de manera persistente.
     *
     * @param sumaRequest contiene el primer número y segundo número a sumar.
     * @return resultado, que indica la suma de dos números más un porcentaje dinámico.
     */
    @PostMapping(path = "/suma", consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String calcularSumaDinamica(@Valid @RequestBody SumaRequest sumaRequest, HttpServletRequest request) {

        SumaConPorcentajeResponse sumaConPorcentajeResponse = calculoDinamicoService.calcularSumaConPorcentajeDinamico(
                sumaRequest);

        historialPeticionService.guardarHistorialPeticion(sumaConPorcentajeResponse, request.getRequestURI());

        return MessageFormat.format("Suma de {0} + {1} + {2}(porcentaje dinámico) = {3}",
                sumaConPorcentajeResponse.getNumero1(), sumaConPorcentajeResponse.getNumero2(),
                sumaConPorcentajeResponse.getPorcentaje(), sumaConPorcentajeResponse.getSuma());
    }

    /**
     * Consulta el historial de las peticiones registrada de manera persistente.
     *
     * @return lista del historial de peticiones.
     */
    @GetMapping(path = "/peticiones/historial")
    public List<HistorialPeticionDto> consultarHistorialPeticion(
            @Valid @RequestParam(required = false, defaultValue = "0") @Pattern(regexp = REG_EXP_NUMEROS_ENTEROS,
                    message = "offset no es de tipo numérico") String offset,
            @Valid @RequestParam(required = false, defaultValue = "10") @Pattern(regexp = REG_EXP_NUMEROS_ENTEROS,
                    message = "offset no es de tipo numérico") String limit) {
        return historialPeticionService.obtenerHistorialPeticionPorPaginacion(Integer.parseInt(offset), Integer.parseInt(limit));
    }
}
