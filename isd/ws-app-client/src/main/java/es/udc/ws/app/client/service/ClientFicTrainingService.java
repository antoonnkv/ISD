package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientFicTrainingService {

    public Long addCurso(ClientCursoDto curso)
        throws InputValidationException;

    public void cancelarInscripcion(Long idInscripcion,String userEmail) throws InputValidationException,
            InstanceNotFoundException, ClientAlreadyCanceledException, ClientIncorrectUserException, ClientInscriptionNoCancelableException;

    public List<ClientCursoDto> findCursos(String ciudad) throws InputValidationException;

    public ClientCursoDto findCursoById(Long cursoId) throws InstanceNotFoundException;

    public Long inscripcion_curso(String email, String numTarjeta, Long idCurso)
            throws InstanceNotFoundException, InputValidationException, ClientCursoEmpezadoException, ClientNotEnougthPlazasDispException;

    public List<ClientInscripcionDto> obt_todas_inscripciones(String emailUser) throws InputValidationException;


}
