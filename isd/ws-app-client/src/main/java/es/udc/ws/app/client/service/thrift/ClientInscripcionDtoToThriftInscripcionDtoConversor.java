package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.thrift.ThriftInscripcionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientInscripcionDtoToThriftInscripcionDtoConversor {

    public static List<ClientInscripcionDto> toClientInscripcionDtos(List<ThriftInscripcionDto> listaInscripciones){

        List<ClientInscripcionDto> listaClientInscripcionDto = new ArrayList<>(listaInscripciones.size());
        for(ThriftInscripcionDto inscripcion: listaInscripciones){
            listaClientInscripcionDto.add(toClientInscripcionDto(inscripcion));
        }

        return listaClientInscripcionDto;
    }

    private static ClientInscripcionDto toClientInscripcionDto(ThriftInscripcionDto inscripcion){
        String fechaCancelacion = inscripcion.getFechaCancelacion();
        return new ClientInscripcionDto(inscripcion.getIdInscripcion(), inscripcion.getEmailUser(), inscripcion.getNumTarjeta(),
                LocalDateTime.parse(inscripcion.getFechaHoraCreacion()),
                fechaCancelacion == null ? null: LocalDateTime.parse(fechaCancelacion), inscripcion.getIdCurso());

    }
}
