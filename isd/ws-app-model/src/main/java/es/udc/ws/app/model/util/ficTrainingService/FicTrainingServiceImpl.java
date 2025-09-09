package es.udc.ws.app.model.util.ficTrainingService;

import javax.sql.DataSource;

import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.model.util.curso.SqlCursoDao;
import es.udc.ws.app.model.util.curso.SqlCursoDaoFactory;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.AlreadyCanceledException;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.CursoEmpezadoException;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.NotEnougthPlazasDispException;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.IncorrectUserException;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.InscriptionNoCancelableException;

import es.udc.ws.app.model.util.inscripcion.Inscripcion;
import es.udc.ws.app.model.util.inscripcion.SqlInscripcionDao;
import es.udc.ws.app.model.util.inscripcion.SqlInscripcionDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;


public class FicTrainingServiceImpl implements FicTrainingService {

    private final DataSource dataSource;
    private SqlCursoDao cursoDao = null;
    private SqlInscripcionDao inscripcionDao = null;

    public FicTrainingServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        cursoDao = SqlCursoDaoFactory.getDao();
        inscripcionDao = SqlInscripcionDaoFactory.getDao();
    }
    public static void validateDates(LocalDateTime fechaCreacion, LocalDateTime fechaComienzo) throws InputValidationException {

        if (fechaCreacion == null || fechaComienzo == null) {
            throw new InputValidationException("Both creation date and start date must be provided.");
        }


        if (!fechaComienzo.isAfter(fechaCreacion)) {
            throw new InputValidationException("Invalid start date: " + fechaComienzo + ". It must be after the creation date: " + fechaCreacion);
        }
    }



    private void validateCurso(Curso curso) throws InputValidationException{

        PropertyValidator.validateMandatoryString("nombre", curso.getNombre());
        PropertyValidator.validateMandatoryString("ciudad",curso.getCiudad());
        PropertyValidator.validateLong("maxPlazas",curso.getMaxPlazas(),1,MAX_PLAZAS);
        PropertyValidator.validateDouble("precio",curso.getPrecio(),0,MAX_PRECIO);
        validateDates(curso.getFechaAlta(),curso.getFechaComienzo());
    }


    @Override
    public Curso addCurso(Curso curso) throws InputValidationException {

        curso.setFechaAlta(LocalDateTime.now());
        validateCurso(curso);


        try(Connection connection = dataSource.getConnection()){

            try{

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso createdCurso = cursoDao.create(connection, curso);

                connection.commit();

                return createdCurso;


            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch(RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Curso> buscarCursoLocFecha(String ciudad, LocalDateTime fecha) throws InputValidationException {
        if (ciudad == null || ciudad.trim().isEmpty()) throw new InputValidationException("La ciudad no puede ser nula o vacía");
        if (fecha == null) throw new InputValidationException("La fecha no puede ser nula");
        try (Connection connection = dataSource.getConnection()) {return cursoDao.findByCityAndDate(connection, ciudad, fecha);}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    @Override
    public Curso buscarCurso(Long cursoId) throws InstanceNotFoundException{
        try (Connection connection = dataSource.getConnection()) {return cursoDao.find(connection, cursoId);}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    @Override

    public void cancel_inscription(Long idInscripcion, String emailUser)
            throws InputValidationException, InstanceNotFoundException,InscriptionNoCancelableException,AlreadyCanceledException,IncorrectUserException {

        PropertyValidator.validateMandatoryString("nombre",emailUser);

        if (idInscripcion == null || idInscripcion <= 0 ){
            throw new InputValidationException("idInscripcion debe ser un valo positivo y no nulo");
        }


        try(Connection connection = dataSource.getConnection()){

            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Inscripcion inscripcion = inscripcionDao.findInscripcion(connection,idInscripcion);
                Curso curso = cursoDao.find(connection,inscripcion.getIdCurso());
                LocalDateTime momentAct = LocalDateTime.now();

                try {
                    if (!inscripcion.getEmailUser().equals(emailUser)) {
                        throw new IncorrectUserException(inscripcion.getIdCurso(), emailUser);
                    }
                    if (momentAct.isAfter(curso.getFechaComienzo().minusDays(7))) {
                        throw new InscriptionNoCancelableException(idInscripcion);
                    }
                    if (inscripcion.getFechaCancelacion() != null) {
                        throw new AlreadyCanceledException(idInscripcion);
                    }


                    inscripcion.setFechaCancelacion(momentAct);
                    curso.setPlazasDisponibles(curso.getPlazasDisponibles() + 1);


                    inscripcionDao.update(connection, inscripcion);
                    cursoDao.update(connection, curso);

                    connection.commit();
                }catch (InscriptionNoCancelableException |AlreadyCanceledException|IncorrectUserException e){
                    connection.commit();
                    throw e;
                }

            }catch (InstanceNotFoundException e){
                connection.commit();
                throw e;
            } catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            } catch(RuntimeException | Error e){
                connection.rollback();
                throw e;
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }


    }
    @Override
    public Long inscripcion_curso(String email, String numTarjeta, Long idCurso)
            throws InstanceNotFoundException, InputValidationException, CursoEmpezadoException, NotEnougthPlazasDispException {

        PropertyValidator.validateCreditCard(numTarjeta);
        PropertyValidator.validateMandatoryString("email",email);
        if(idCurso == null || idCurso <= 0 ){
            throw new InputValidationException("El id de Curso debe ser un valor positivo (No puede ser 0 ni nulo)");
        }

        try (Connection connection = dataSource.getConnection()) {

            try {
                // Preparamos conexión
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                //hacemos inscripción
                Curso curso = cursoDao.find(connection, idCurso);

                LocalDateTime fechaActual= LocalDateTime.now();

                if(curso.getFechaComienzo().isBefore(fechaActual)){
                    throw new CursoEmpezadoException(idCurso,curso.getFechaComienzo());
                }

                if(curso.getPlazasDisponibles() == 0){
                    throw new NotEnougthPlazasDispException(idCurso, curso.getPlazasDisponibles());
                }

                Inscripcion inscripcion = inscripcionDao.create(connection, new Inscripcion(email, numTarjeta, LocalDateTime.now(), null,idCurso));
                curso.setPlazasDisponibles(curso.getPlazasDisponibles()-1);
                cursoDao.update(connection,curso);

                Long idInscripcion = inscripcion.getidInscripcion();


                connection.commit();

                return idInscripcion;

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);

            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public List<Inscripcion> obt_todas_inscripciones(String emailUser) throws InputValidationException{
        try (Connection connection = dataSource.getConnection()) {
            if(emailUser == null){
                throw new InputValidationException("El email de Usuario no puede ser nulo");
            }
            return inscripcionDao.obt_todas_inscripciones(connection, emailUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }





}