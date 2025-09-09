package es.udc.ws.app.restservice.dto;
import es.udc.ws.app.restservice.dto.RestInscripcionDto;
import es.udc.ws.app.model.util.inscripcion.Inscripcion;

import java.util.ArrayList;
import java.util.List;

public class InscripcionToRestInscripcionConversor {
    public static List<RestInscripcionDto> toRestInscripcionDtoList(List<Inscripcion> inscripciones) {
        List<RestInscripcionDto> inscripcionesDto = new ArrayList<>(inscripciones.size());
        for (int i = 0; i < inscripciones.size(); i++) inscripcionesDto.add(toRestInscripcionDto(inscripciones.get(i)));
        return inscripcionesDto;
    }
    public static RestInscripcionDto toRestInscripcionDto(Inscripcion ins) {
        String fechaCancelacion;
        if(ins.getFechaCancelacion()!=null){
            fechaCancelacion = ins.getFechaCancelacion().toString();
        }else fechaCancelacion = null;
        return new RestInscripcionDto(ins.getidInscripcion(), ins.getEmailUser(), ins.getNumTarjeta(), ins.getFechaHoraCreacion().toString(),fechaCancelacion, ins.getIdCurso());
    }

}
