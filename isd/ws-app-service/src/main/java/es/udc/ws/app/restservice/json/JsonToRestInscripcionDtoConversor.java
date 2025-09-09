package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.restservice.dto.RestInscripcionDto;
import java.util.List;

public class JsonToRestInscripcionDtoConversor {
    public static ObjectNode toObjectNode(RestInscripcionDto inscripcion){
        ObjectNode nodoInscripcion= JsonNodeFactory.instance.objectNode();

        if(inscripcion.getidInscripcion() != null){
            nodoInscripcion.put("idInscripcion",inscripcion.getidInscripcion());
        }
        int i = inscripcion.getNumTarjeta().length();
        nodoInscripcion.put("emailUser", inscripcion.getEmailUser()).
                put("numTarjeta", inscripcion.getNumTarjeta().substring(i-4,i)).
                put("fechaHoraCreacion",inscripcion.getFechaHoraCreacion()).
                put("fechaCancelacion",inscripcion.getFechaCancelacion()).
                put("idCurso",inscripcion.getIdCurso());

        return nodoInscripcion;

    }

    public static ArrayNode toArrayNode(List<RestInscripcionDto> inscripciones) {
        ArrayNode inscripcionesNode  = JsonNodeFactory.instance.arrayNode();

        for(int i = 0; i < inscripciones.size();i++){

            RestInscripcionDto inscripcionDto = inscripciones.get(i);
            ObjectNode inscripcionObject = toObjectNode(inscripcionDto);
            inscripcionesNode.add(inscripcionObject);
        }
        return inscripcionesNode;
    }
}
