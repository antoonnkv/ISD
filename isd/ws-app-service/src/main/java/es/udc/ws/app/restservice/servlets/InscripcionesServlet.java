package es.udc.ws.app.restservice.servlets;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import es.udc.ws.app.model.util.ficTrainingService.FicTrainingServiceFactory;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.*;
import es.udc.ws.app.model.util.inscripcion.Inscripcion;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscripcionDtoConversor;
import es.udc.ws.app.restservice.dto.InscripcionToRestInscripcionConversor;

import es.udc.ws.app.restservice.dto.RestInscripcionDto;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class InscripcionesServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse respuesta) throws IOException,
            InputValidationException, es.udc.ws.util.exceptions.InstanceNotFoundException {

        if(request.getPathInfo() ==null || Objects.equals(request.getPathInfo(), "/")){
            ServletUtils.checkEmptyPath(request);
            Long idCurso = ServletUtils.getMandatoryParameterAsLong(request,"idCurso");
            String emailUser = ServletUtils.getMandatoryParameter(request, "emailUser");
            String numTarjeta= ServletUtils.getMandatoryParameter(request, "numTarjeta");
            try {
                Long idInscripcion = FicTrainingServiceFactory.getService().inscripcion_curso(emailUser, numTarjeta, idCurso);
                ObjectNode nodoIdInscripcion= JsonNodeFactory.instance.objectNode();
                nodoIdInscripcion.put("idInscripcion",idInscripcion);

                String urlInscripcion = ServletUtils.normalizePath(request.getRequestURL().toString());
                Map<String, String> cabeceras = new HashMap<>(1);
                cabeceras.put("Location", urlInscripcion);
                ServletUtils.writeServiceResponse(respuesta, HttpServletResponse.SC_CREATED,nodoIdInscripcion.numberNode(idInscripcion),cabeceras);

            }catch (CursoEmpezadoException c_e){
                ServletUtils.writeServiceResponse(respuesta,HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toCursoEmpezadoException(c_e),null);
            }catch (NotEnougthPlazasDispException e){
                ServletUtils.writeServiceResponse(respuesta,HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toNotEnoughtPlazasDispException(e),null);
            }


        }
        else{
            String path = request.getPathInfo();
            String[] SplittedPath = path.split("/");


            if(!SplittedPath[2].equals("cancelar")){
                throw new InputValidationException("No reconocido");
            }

            Long idInscripcion = Long.valueOf(SplittedPath[1]);

            String userEmail = ServletUtils.getMandatoryParameter(request, "emailUser").trim();

            try{
                FicTrainingServiceFactory.getService().cancel_inscription(idInscripcion,userEmail);

            } catch (AlreadyCanceledException e){
                ServletUtils.writeServiceResponse(respuesta,HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toAlreadyCanceledException(e),null);

            } catch (InscriptionNoCancelableException e) {
                ServletUtils.writeServiceResponse(respuesta,HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInscripcionNoCancelableException(e),null);

            } catch (IncorrectUserException e){
                ServletUtils.writeServiceResponse(respuesta,HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toIncorrectUserException(e),null);
            }

            ServletUtils.writeServiceResponse(respuesta,HttpServletResponse.SC_NO_CONTENT,null,null);
        }
    }


    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse resp) throws IOException,
            InputValidationException, es.udc.ws.util.exceptions.InstanceNotFoundException {

        ServletUtils.checkEmptyPath(request);
        String emailUser = request.getParameter("emailUser");

        List<Inscripcion> listaInscripciones = FicTrainingServiceFactory.getService().obt_todas_inscripciones(emailUser);

        List<RestInscripcionDto> inscripcionDtos = InscripcionToRestInscripcionConversor.toRestInscripcionDtoList(listaInscripciones);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestInscripcionDtoConversor.toArrayNode(inscripcionDtos), null);

    }

}
