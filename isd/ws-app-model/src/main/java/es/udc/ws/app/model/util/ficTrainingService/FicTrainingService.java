package es.udc.ws.app.model.util.ficTrainingService;
import es.udc.ws.app.model.util.curso.*;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.*;
import es.udc.ws.app.model.util.inscripcion.Inscripcion;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;
import java.time.LocalDateTime;

public interface FicTrainingService {
    public Curso addCurso(Curso curso) throws InputValidationException;
    public void cancel_inscription(Long idInscription, String emailUser) throws InscriptionNoCancelableException,
            InstanceNotFoundException, AlreadyCanceledException, IncorrectUserException, InputValidationException;
    public Long inscripcion_curso(String email, String numTarjeta, Long idCurso)
            throws InstanceNotFoundException, InputValidationException, CursoEmpezadoException, NotEnougthPlazasDispException;
    public List<Inscripcion> obt_todas_inscripciones(String emailUser) throws InputValidationException;
    public Curso buscarCurso(Long cursoId) throws InstanceNotFoundException;
    public List<Curso> buscarCursoLocFecha(String ciudad, LocalDateTime fecha) throws InputValidationException;
}
