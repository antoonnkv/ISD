package es.udc.ws.app.client.service.rest.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCursoDtoConversor {


    public static ObjectNode toObjectNode(ClientCursoDto curso) throws IOException {
        ObjectNode obj = JsonNodeFactory.instance.objectNode();

        if (curso.getIdCurso() != null) obj.put("idCurso", curso.getIdCurso());

        obj.put("nombre", curso.getNombre()).put("ciudad", curso.getCiudad()).put("plazasReservadas", curso.getPlazasReservadas())
                .put("precio", curso.getPrecio()).put("fechaComienzo", curso.getFechaComienzo().toString()).put("maxPlazas", curso.getMaxPlazas());

        return obj;
    }


    public static ClientCursoDto toClientCursoDto(InputStream jsonObj) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonObj);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else{
                return toClientCursoDto(rootNode);
            }
        } catch (ParsingException ex) {throw ex;}
        catch (Exception e) {throw new ParsingException(e);}
    }

    private static ClientCursoDto toClientCursoDto(JsonNode cursoNode) throws ParsingException {
        if (cursoNode.getNodeType() != JsonNodeType.OBJECT) throw new ParsingException("Unrecognized JSON (object expected)");
        else {
            ObjectNode cursoObject = (ObjectNode) cursoNode;

            JsonNode cursoIdNode = cursoObject.get("idCurso");
            Long cursoId = (cursoIdNode != null) ? cursoIdNode.longValue() : null;

            String nombre = cursoObject.get("nombre").textValue().trim();
            String ciudad = cursoObject.get("ciudad").textValue().trim();
            int maxPlazas = cursoObject.get("maxPlazas").intValue();
            int plazasReservadas = maxPlazas - cursoObject.get("plazasDisponibles").intValue();
            float precio = cursoObject.get("precio").floatValue();
            String fechaComienzo = cursoObject.get("fechaComienzo").textValue().trim();
	    ClientCursoDto c = new ClientCursoDto(cursoId, nombre, ciudad, precio, LocalDateTime.parse(fechaComienzo), maxPlazas);
	    c.setPlazasReservadas(plazasReservadas);
            return c;
        }
    }

	public static List<ClientCursoDto> toClientCursoDtos(InputStream jsonCursos) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonCursos);

			if (rootNode.getNodeType() != JsonNodeType.ARRAY) throw new ParsingException("Unrecognized JSON (array expected)");
			else {
				ArrayNode cursosArray = (ArrayNode) rootNode;
				List<ClientCursoDto> cursosDtos = new ArrayList<>(cursosArray.size());
				for (JsonNode cursoNode : cursosArray) cursosDtos.add(toClientCursoDto(cursoNode));
				return cursosDtos;
			}
		} catch (ParsingException ex) {throw ex;} 
        catch (Exception e) {throw new ParsingException(e);}
	}
}
