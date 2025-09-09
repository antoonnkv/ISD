package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.ClientCursoEmpezadoException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnougthPlazasDispException;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientCursoDtoConversor;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientFicTrainingService;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientInscripcionDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientFicTrainingService implements ClientFicTrainingService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientFicTrainingService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addCurso(ClientCursoDto curso) throws InputValidationException {

        try{

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "cursos").
                    bodyStream(toInputStream(curso), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientCursoDtoConversor.toClientCursoDto(response.getEntity().getContent()).getIdCurso();

        } catch (InputValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override

    public void cancelarInscripcion(Long idInscripcion,String emailUser) throws InputValidationException,
            InstanceNotFoundException, ClientAlreadyCanceledException, ClientIncorrectUserException, ClientInscriptionNoCancelableException{

        try{

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "inscripciones/" + idInscripcion + "/cancelar").
                    bodyForm(
                            Form.form().
                                    add("emailUser", emailUser).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        }catch (InputValidationException| InstanceNotFoundException | ClientInscriptionNoCancelableException |
                ClientIncorrectUserException | ClientAlreadyCanceledException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<ClientCursoDto> findCursos(String ciudad) throws InputValidationException {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "cursos?ciudad=" + URLEncoder.encode(ciudad, "UTF-8")).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientCursoDtoConversor.toClientCursoDtos(response.getEntity().getContent());
        } catch (InputValidationException e) {throw e;}
        catch (Exception e) {throw new RuntimeException(e);}
    }


    @Override
    public ClientCursoDto findCursoById(Long cursoId) throws InstanceNotFoundException {

        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "cursos/" + cursoId).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientCursoDtoConversor.toClientCursoDto(response.getEntity().getContent());
        } catch (InstanceNotFoundException e) {throw e;}
        catch (Exception e) {throw new RuntimeException(e);}
    }

    @Override
    public Long inscripcion_curso(String email, String numTarjeta, Long idCurso)
            throws InstanceNotFoundException, InputValidationException, ClientCursoEmpezadoException, ClientNotEnougthPlazasDispException {
        try{
            ClassicHttpResponse respuesta = (ClassicHttpResponse) Request.post(getEndpointAddress() + "inscripciones").
                    bodyForm(
                            Form.form().
                                    add("emailUser", email).
                                    add("numTarjeta", numTarjeta).
                                    add("idCurso", Long.toString(idCurso)).
                                    build()).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, respuesta);
            return JsonToClientInscripcionDtoConversor.toClientInscripcionDto(
                    respuesta.getEntity().getContent());

        }catch (InputValidationException | InstanceNotFoundException | ClientCursoEmpezadoException |
                ClientNotEnougthPlazasDispException except){
            throw except;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientInscripcionDto> obt_todas_inscripciones(String emailUser) throws InputValidationException{
        try{
            ClassicHttpResponse respuesta = (ClassicHttpResponse) Request.get(getEndpointAddress() + "inscripciones?emailUser="
                            + URLEncoder.encode(emailUser, "UTF-8")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, respuesta);
            return JsonToClientInscripcionDtoConversor.toClientInscripcionDtos(respuesta.getEntity()
                    .getContent());
        } catch (InputValidationException  ex) {
            throw ex;
        }
        catch (Exception except){
            throw new RuntimeException(except);
        }

    }


    private synchronized String getEndpointAddress() {
        if(endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientCursoDto curso) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientCursoDtoConversor.toObjectNode(curso));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, ClassicHttpResponse respuesta) throws Exception{

        try{

            int statusCode = respuesta.getCode();

            if(statusCode == successCode){
                return;
            }

            switch (statusCode){
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(respuesta.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(respuesta.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN ->  throw JsonToClientExceptionConversor.fromForbiddenErrorCode(respuesta.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = " + statusCode);
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
