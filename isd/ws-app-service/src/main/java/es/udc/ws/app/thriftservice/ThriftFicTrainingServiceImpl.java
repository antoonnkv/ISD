package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.ficTrainingService.FicTrainingServiceFactory;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.*;
import es.udc.ws.app.model.util.inscripcion.Inscripcion;
import es.udc.ws.app.thrift.*;
import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.thrift.ThriftCursoDto;
import es.udc.ws.app.thrift.ThriftInputValidationException;


import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftFicTrainingServiceImpl implements ThriftFicTrainingService.Iface {

    @Override
    public ThriftCursoDto addCurso(ThriftCursoDto cursoDto) throws ThriftInputValidationException {
        Curso curso = CursoToThriftCursoDtoConversor.toCurso(cursoDto);
        try {
            Curso addedCurso = FicTrainingServiceFactory.getService().addCurso(curso);
            return CursoToThriftCursoDtoConversor.toThriftCursoDto(addedCurso);
        } catch (InputValidationException e) {throw new ThriftInputValidationException(e.getMessage());}
    }

    @Override
    public List<ThriftCursoDto> findCursos(String keywords) throws ThriftInputValidationException {
        try {
            List<Curso> cursos = FicTrainingServiceFactory.getService().buscarCursoLocFecha(keywords, LocalDateTime.now());
            return CursoToThriftCursoDtoConversor.toThriftCursoDtos(cursos);
        } catch (InputValidationException e) {throw new ThriftInputValidationException(e.getMessage());}
    }

    @Override
    public long inscripcion_curso(long cursoId, String emailUser, String numTarjeta) throws
            ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftCursoEmpezadoException, ThriftNotEnougthPlazasDispException {

        try {

            return FicTrainingServiceFactory.getService().inscripcion_curso(emailUser, numTarjeta, cursoId);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (NotEnougthPlazasDispException e) {
            throw new ThriftNotEnougthPlazasDispException(e.getIdCurso(),e.getPlazasDisponibles());
        } catch (CursoEmpezadoException e) {
            throw new ThriftCursoEmpezadoException(e.getIdCurso(), e.getFechaComienzo().toString());
        }
    }


    @Override
    public ThriftCursoDto buscarCurso(long cursoId) throws ThriftInstanceNotFoundException {
        try {
            Curso curso = FicTrainingServiceFactory.getService().buscarCurso(cursoId);

            return CursoToThriftCursoDtoConversor.toThriftCursoDto(curso);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().
                    substring(e.getInstanceType().lastIndexOf('.') + 1));
        }


    }


    @Override
    public void cancelarInscripcion(long idInscripcion, String userEmail) throws ThriftInputValidationException, ThriftInstanceNotFoundException,
            ThriftAlreadyCanceledException, ThriftIncorrectUserException, ThriftInscriptionNoCancelableException{
        try{
            FicTrainingServiceFactory.getService().cancel_inscription(idInscripcion, userEmail);;

        }catch (InputValidationException inp_e){
            throw new ThriftInputValidationException(inp_e.getMessage());
        }
        catch (InstanceNotFoundException ins_e){
            throw new ThriftInstanceNotFoundException(ins_e.getInstanceId().toString(),
                    ins_e.getInstanceType().substring(ins_e.getInstanceType().lastIndexOf('.') + 1));
        }
        catch (AlreadyCanceledException ac_e){
            throw new ThriftAlreadyCanceledException(ac_e.getIdInscripcion(), ac_e.getMessage());
        }catch (IncorrectUserException iu_e){
            throw new ThriftIncorrectUserException(iu_e.getIdInscripcion(), iu_e.getUserEmail());
        }catch (InscriptionNoCancelableException nc_e){
            throw new ThriftInscriptionNoCancelableException(nc_e.getIdInscripcion(), nc_e.getPlazoCancelacion().toString());
        }

    }
    @Override
    public List<ThriftInscripcionDto> obt_todas_inscripciones(String email) throws ThriftInputValidationException{
        try{
            List<Inscripcion> inscripciones = FicTrainingServiceFactory.getService().obt_todas_inscripciones(email);
            return InscripcionToThriftInscripcionDtoConversor.toThriftInscripcionDtos(inscripciones);

        }catch (InputValidationException inp_e) {
            throw new ThriftInputValidationException(inp_e.getMessage());
        }

    }

}
