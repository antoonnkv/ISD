package es.udc.ws.app.client.service.rest.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientInscripcionDtoConversor {
    public static Long toClientInscripcionDto(InputStream jsonInscripcion) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscripcion);

            if (rootNode.getNodeType() != JsonNodeType.NUMBER)

                throw new ParsingException("Unrecognized JSON (object expected)");
            else {
                return rootNode.longValue();
            }
        } catch (ParsingException ex) {throw ex;}
        catch (Exception e) {throw new ParsingException(e);}
    }
    private static ClientInscripcionDto toClientInscripcionDto(JsonNode inscripcionNode) throws ParsingException {
        if (inscripcionNode.getNodeType() != JsonNodeType.OBJECT) throw new ParsingException("Unrecognized JSON (object expected)");
        else {
            ObjectNode inscripcionObject = (ObjectNode) inscripcionNode;

            JsonNode inscripcionIdNode = inscripcionObject.get("idInscripcion");
            Long inscripcionId = (inscripcionIdNode != null) ? inscripcionIdNode.longValue() : null;

            String emailUser = inscripcionObject.get("emailUser").textValue().trim();
            String numTarjeta = inscripcionObject.get("numTarjeta").textValue().trim();
            String fechaHoraCreacion = inscripcionObject.get("fechaHoraCreacion").textValue().trim();
            String fechaCancelacion;
            if(inscripcionObject.get("fechaCancelacion").textValue() == null){

                fechaCancelacion = null;
            } else {
                fechaCancelacion = inscripcionObject.get("fechaCancelacion").textValue().trim();
            }
            Long idCurso = inscripcionObject.get("idCurso").longValue();

            return new ClientInscripcionDto(inscripcionId, emailUser, numTarjeta,LocalDateTime.parse(fechaHoraCreacion), (fechaCancelacion != null) ? LocalDateTime.parse(fechaCancelacion) : null, idCurso);
        }
    }


    public static List<ClientInscripcionDto> toClientInscripcionDtos(InputStream jsonInscripciones) throws ParsingException {

        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode nodoRaiz = objectMapper.readTree(jsonInscripciones);

            if (nodoRaiz.getNodeType() != JsonNodeType.ARRAY) throw new ParsingException("Unrecognized JSON (array expected)");
            else {
                ArrayNode arrayInscripciones = (ArrayNode) nodoRaiz;
                List<ClientInscripcionDto> inscripcionesDtos = new ArrayList<>(arrayInscripciones.size());
                for (JsonNode nodoInscripcion : arrayInscripciones) inscripcionesDtos.add(toClientInscripcionDto(nodoInscripcion));
                return inscripcionesDtos;
            }
        } catch (ParsingException ex) {throw ex;}
        catch (Exception e) {throw new ParsingException(e);}
    }

}
