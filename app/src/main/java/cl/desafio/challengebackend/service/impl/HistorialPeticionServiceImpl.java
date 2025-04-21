package cl.desafio.challengebackend.service.impl;

import cl.desafio.challengebackend.entity.HistorialPeticion;
import cl.desafio.challengebackend.repository.HistorialPeticionRepository;
import cl.desafio.challengebackend.service.HistorialPeticionService;
import cl.desafio.challengebackend.model.HistorialPeticionDto;
import cl.desafio.challengebackend.service.domain.SumaConPorcentajeResponse;
import cl.desafio.challengebackend.service.mapper.HistorialPeticionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistorialPeticionServiceImpl implements HistorialPeticionService {

    private final HistorialPeticionRepository historialPeticionRepository;

    public List<HistorialPeticionDto> obtenerHistorialPeticionPorPaginacion(int offset, int limit) {
        log.info("[obtenerHistorialPeticionPorPaginacion] INICIO - Obtener el historial de peticiones con offset [{}]" +
                " y limit [{}]", offset, limit);

        final List<HistorialPeticion> historialPeticionesByPage = historialPeticionRepository.findHistorialPeticiones(
                (offset * limit), limit);
        final List<HistorialPeticionDto> historialPeticionesList = historialPeticionesByPage.stream()
                .map(HistorialPeticionMapper::convertToDto).toList();
        log.info("[obtenerHistorialPeticionPorPaginacion] FINOK - Historial de peticiones con [{}] registros"
                , historialPeticionesList.size());
        return historialPeticionesList;
    }

    public HistorialPeticionDto guardarHistorialPeticion(SumaConPorcentajeResponse sumaConPorcentajeResponse,
                                                         String urlPeticion) {

        HistorialPeticionDto historialPeticionDto = new HistorialPeticionDto(null, urlPeticion
                , MessageFormat.format("{0} + {1} + {2}", sumaConPorcentajeResponse.getNumero1(),
                sumaConPorcentajeResponse.getNumero2(), sumaConPorcentajeResponse.getPorcentaje()),
                String.valueOf(sumaConPorcentajeResponse.getSuma()), LocalDateTime.now());
        HistorialPeticion historialPeticion = HistorialPeticionMapper.convertToEntity(historialPeticionDto);

        log.info("[guardarHistorialPeticion] INICIO - registrando petición [{}] ", historialPeticion);
        HistorialPeticion saveHistorialPeticion = historialPeticionRepository.save(historialPeticion);
        log.info("[guardarHistorialPeticion] FINOK - se registró la petición [{}] de manera exitosa", saveHistorialPeticion);
        return HistorialPeticionMapper.convertToDto(saveHistorialPeticion);
    }

}
