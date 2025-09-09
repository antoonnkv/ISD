package es.udc.ws.app.client.service.rest.json;

import java.io.InputStream;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;



public  class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode nodoRaiz = objectMapper.readTree(ex);
            if(nodoRaiz.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("JSON no reconocible (se esparaba un objeto");

            }else{
                String tipoError = nodoRaiz.get("errorType").textValue();
                if(tipoError.equals("InputValidation")){
                    return toInputValidationException(nodoRaiz);
                }else{
                    throw new ParsingException("Tipo de error no reconocido"+tipoError);

                }
            }

        }catch (ParsingException p_except){
            throw p_except;
        }catch (Exception e){

            throw new ParsingException(e);
        }
    }
    private static InputValidationException toInputValidationException(JsonNode nodoRaiz){
        String message = nodoRaiz.get("message").textValue();
        return new InputValidationException(message);

    }

    public static Exception fromNotFoundErrorCode(InputStream ex){
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode nodoRaiz = objectMapper.readTree(ex);
            if(nodoRaiz.getNodeType() != JsonNodeType.OBJECT) throw new ParsingException("JSON no reconocido (se esperaba objeto");
            else{
                String tipoError = nodoRaiz.get("errorType").textValue();
                if(tipoError.equals("InstanceNotFound")) return toInstanceNotFoundException(nodoRaiz);
                else throw new ParsingException("Tipo de error no reconocido: "+tipoError);
            }
        }catch (ParsingException p_ex){throw p_ex;}
        catch (Exception e){throw new ParsingException(e);}
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode nodoRaiz){
        String idInstance = nodoRaiz.get("instanceId").textValue();
        String tipoInstance = nodoRaiz.get("instanceType").textValue();
        return new InstanceNotFoundException(idInstance, tipoInstance);
    }


    public static Exception fromForbiddenErrorCode(InputStream excepcion){
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(excepcion);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("JSON irreconocible (se esperaba objeto)");

            }else {
                String tipoError = rootNode.get("errorType").textValue();
                if (tipoError.equals("CursoEmpezado")) {
                    return toCursoEmpezadoException(rootNode);
                } else if (tipoError.equals("NotEnoughtPlazasDisp")) {
                    return toNotEnoughtPlazasDispException(rootNode);
                } else if (tipoError.equals("AlreadyCanceled")){
                    return toAlreadyCanceledException(rootNode);
                } else if (tipoError.equals("IncorrectUser")){
                    return toClientIncorrectUserException(rootNode);
                }else if(tipoError.equals("InscriptionNoCancelable")){
                    return toInscripcionNoCancelableException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + tipoError);
                }
            }
        }catch (ParsingException p_except){
            throw p_except;
        }catch (Exception e){
            throw new ParsingException(e);
        }
    }

    private static ClientCursoEmpezadoException toCursoEmpezadoException(JsonNode nodoRaiz){
        Long idCurso = nodoRaiz.get("idCurso").longValue();
        String fechaComienzoString = nodoRaiz.get("fechaComienzo").textValue();
        LocalDateTime fechaComienzo = null;
        if(fechaComienzoString != null){
            fechaComienzo = LocalDateTime.parse(fechaComienzoString);
        }
        return new ClientCursoEmpezadoException(idCurso,fechaComienzo);
    }
    private static ClientNotEnougthPlazasDispException  toNotEnoughtPlazasDispException(JsonNode nodoRaiz){
        Long idCurso = nodoRaiz.get("idCurso").longValue();
        int plazasDisponibles = nodoRaiz.get("plazasDisponibles").intValue();
        return new ClientNotEnougthPlazasDispException(idCurso,plazasDisponibles);
    }

    private static ClientAlreadyCanceledException toAlreadyCanceledException(JsonNode nodoRaiz){
        Long idInscripcion = nodoRaiz.get("idInscripcion").longValue();
        return new ClientAlreadyCanceledException(idInscripcion);
    }

    private static ClientInscriptionNoCancelableException toInscripcionNoCancelableException(JsonNode nodoRaiz){
        Long idInscripcion = nodoRaiz.get("idInscripcion").longValue();
        return new ClientInscriptionNoCancelableException(idInscripcion);
    }

    private static ClientIncorrectUserException toClientIncorrectUserException(JsonNode nodoRaiz){
        Long idInscripcion = nodoRaiz.get("idInscripcion").longValue();
        String userEmail = nodoRaiz.get("userEmail").textValue();

        return new ClientIncorrectUserException(idInscripcion, userEmail);
    }




}
