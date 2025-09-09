package es.udc.ws.app.restservice.json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCursoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonToRestCursoDtoConversor {

    public static ObjectNode toObjectNode(RestCursoDto curso) {
        ObjectNode cursoObject = JsonNodeFactory.instance.objectNode();

        cursoObject.put("idCurso", curso.getIdCurso()).
                put("nombre", curso.getNombre()).
                put("ciudad", curso.getCiudad()).
                put("plazasDisponibles", curso.getPlazasDisponibles()).
                put("precio", curso.getPrecio()).
                put("maxPlazas", curso.getMaxPlazas()).
                put("fechaComienzo", curso.getFechaComienzo());
        return cursoObject;
    }

    public static ArrayNode toArrayNode(List<RestCursoDto> cursos) {
        ArrayNode cursosNode  = JsonNodeFactory.instance.arrayNode();

        for(int i = 0; i < cursos.size();i++){

            RestCursoDto cursoDto = cursos.get(i);
            ObjectNode cursoObject = toObjectNode(cursoDto);
            cursosNode.add(cursoObject);
        }
        return cursosNode;
    }

    public static RestCursoDto toRestCursoDto(InputStream jsonCurso) throws ParsingException {

        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode nodoRaiz = objectMapper.readTree(jsonCurso);

            if(nodoRaiz.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("JSON no reconocible (objeto esperado");
            }
            else{
                ObjectNode cursoObject = (ObjectNode) nodoRaiz;
                JsonNode nodoCursoId = cursoObject.get("idCurso");
                Long idCurso;
                if(nodoCursoId !=null){
                    idCurso = nodoCursoId.longValue();
                }
                else{
                    idCurso = null;
                }

                String nombre = cursoObject.get("nombre").textValue().trim();
                String ciudad = cursoObject.get("ciudad").textValue().trim();
                float precio = cursoObject.get("precio").floatValue();
                int maxplazas = cursoObject.get("maxPlazas").intValue();
                String fechaComienzo = cursoObject.get("fechaComienzo").textValue().trim();




                return new RestCursoDto(idCurso, nombre, ciudad,precio,fechaComienzo,maxplazas, maxplazas);

            }

        }catch (ParsingException p_ex){
            throw p_ex;
        }catch (Exception except){
            throw new ParsingException(except);
        }
    }
}
