package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.*;

public class AppExceptionToJsonConversor {
    //public static ObjectNode toAlreadyCanceledException(AlreadyCanceledException except){}
    public static ObjectNode toCursoEmpezadoException(CursoEmpezadoException except){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","CursoEmpezado");

        exceptionObject.put("idCurso",except.getIdCurso());


        exceptionObject.put("fechaComienzo",except.getFechaComienzo().toString());

        return exceptionObject;

    }
    // public static ObjectNode toIncorrectUserException(IncorrectUserException except){}
    //public static ObjectNode toInscripcionNoCancelableException(InscripcionNoCancelableException except){}
    public static ObjectNode toNotEnoughtPlazasDispException(NotEnougthPlazasDispException except){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","NotEnoughtPlazasDisp");

        exceptionObject.put("idCurso",except.getIdCurso());


        exceptionObject.put("plazasDisponibles",except.getPlazasDisponibles());

        return exceptionObject;
    }

    public static ObjectNode toAlreadyCanceledException(AlreadyCanceledException e) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","AlreadyCanceled");
        exceptionObject.put("idInscripcion",(e.getIdInscripcion() != null) ? e.getIdInscripcion() : null);

        return exceptionObject;
    }

    public static ObjectNode toIncorrectUserException(IncorrectUserException e) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","IncorrectUser");
        exceptionObject.put("idInscripcion",(e.getIdInscripcion() != null) ? e.getIdInscripcion() : null);
        exceptionObject.put("userEmail",(e.getUserEmail() != null) ? e.getUserEmail() : null);

        return exceptionObject;
    }

    public static ObjectNode toInscripcionNoCancelableException(InscriptionNoCancelableException e) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","InscriptionNoCancelable");
        exceptionObject.put("idInscripcion",(e.getIdInscripcion() != null) ? e.getIdInscripcion() : null);

        return exceptionObject;
    }
}
