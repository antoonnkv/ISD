package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.model.util.inscripcion.Inscripcion;
import es.udc.ws.app.thrift.ThriftCursoDto;
import es.udc.ws.app.thrift.ThriftInscripcionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InscripcionToThriftInscripcionDtoConversor {
    public static List<ThriftInscripcionDto> toThriftInscripcionDtos(List<Inscripcion> inscripciones){
        List<ThriftInscripcionDto> inscripcionesDto = new ArrayList<>(inscripciones.size());

        for(Inscripcion inscrip : inscripciones){
            inscripcionesDto.add(toThriftInscripcionDto(inscrip));
        }
        return inscripcionesDto;
    }


    public static ThriftInscripcionDto toThriftInscripcionDto(Inscripcion inscripcion){
        LocalDateTime fechaCancelacion= inscripcion.getFechaCancelacion();
        return new ThriftInscripcionDto(inscripcion.getidInscripcion(), inscripcion.getIdCurso(), inscripcion.getEmailUser(), inscripcion.getNumTarjeta(),
                inscripcion.getFechaHoraCreacion().toString(),fechaCancelacion == null ? null:fechaCancelacion.toString());

    }
}
