package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.model.util.curso.SqlCursoDao;
import es.udc.ws.app.model.util.curso.SqlCursoDaoFactory;
import es.udc.ws.app.model.util.ficTrainingService.FicTrainingService;
import es.udc.ws.app.model.util.ficTrainingService.FicTrainingServiceFactory;
import es.udc.ws.app.model.util.ficTrainingService.exceptions.*;
import es.udc.ws.app.model.util.inscripcion.Inscripcion;
import es.udc.ws.app.model.util.inscripcion.SqlInscripcionDao;
import es.udc.ws.app.model.util.inscripcion.SqlInscripcionDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppServiceTest {

    private static FicTrainingService ficTrainingService = null;
    private static SqlInscripcionDao inscripcionDao = null;
    private static SqlCursoDao cursoDao = null;
    private final String USUARIO_CORRECTO = "usuarioCorrecto";
    private final String TARJETA_CORRECTA = "1234567890123456";


    @BeforeAll

    public static void init() {

        DataSource dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE,dataSource);

        ficTrainingService = FicTrainingServiceFactory.getService();

        cursoDao = SqlCursoDaoFactory.getDao();

        inscripcionDao = SqlInscripcionDaoFactory.getDao();

    }


    private Curso crearCursoValido(String Nombre){
        LocalDateTime fechaCorrecta =LocalDateTime.now().plusDays(200);
        return new Curso(Nombre,"Madrid",20,fechaCorrecta,30);
    }

    private Curso crearCursoPorCiudad(String Ciudad){
        LocalDateTime fechaCorrecta = LocalDateTime.now().plusDays(200);
        return new Curso("CURSO",Ciudad,20,fechaCorrecta,30);
    }

    private Curso crearCursoPorFecha(LocalDateTime fecha){
        return new Curso("CURSO","Madrid",20,fecha,30);
    }

    private Curso createCurso(Curso curso){
        Curso cursoNuevo = null;

        try{
            cursoNuevo = ficTrainingService.addCurso(curso);
        }catch(InputValidationException e){
            throw new RuntimeException();
        }
        return cursoNuevo;
    }
    private Inscripcion createInscripcion(String email, String numTarj, Long idCurso){
        Long idInscripcionNueva;
        try{
            idInscripcionNueva = ficTrainingService.inscripcion_curso(email, numTarj, idCurso);
        } catch (InstanceNotFoundException | InputValidationException  e) {
            throw new RuntimeException(e);
        }
        return findInscripcion(idInscripcionNueva);
    }


    private void removeCurso(Long idCurso){

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection conexion = dataSource.getConnection()){

            try{

                conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conexion.setAutoCommit(false);

                cursoDao.remove(conexion,idCurso);

                conexion.commit();

            } catch (InstanceNotFoundException e){
                conexion.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                conexion.rollback();
                throw e;
            } catch (RuntimeException | Error e){
                conexion.rollback();
                throw e;
            }

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    private void removeInscripcion(Long idInscripcion){

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection conexion = dataSource.getConnection()){

            try{

                conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conexion.setAutoCommit(false);

                inscripcionDao.remove(conexion,idInscripcion);

                conexion.commit();

            } catch (InstanceNotFoundException e){
                conexion.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                conexion.rollback();
                throw e;
            } catch (RuntimeException | Error e){
                conexion.rollback();
                throw e;
            }

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    private void updateCurso(Curso curso){

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection conexion = dataSource.getConnection()){

            try{

                conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conexion.setAutoCommit(false);

                cursoDao.update(conexion,curso);

                conexion.commit();

            } catch (InstanceNotFoundException e){
                conexion.commit();
                throw new RuntimeException(e);
            } catch (SQLException e){
                conexion.rollback();
                throw e;
            } catch (RuntimeException | Error e){
                conexion.rollback();
                throw e;
            }

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }
    private Inscripcion findInscripcion(Long inscripcion) {

        DataSource baseDatos = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        Inscripcion inscripcionEncontrada = null;

        try (Connection connection = baseDatos.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                inscripcionEncontrada = inscripcionDao.findInscripcion(connection, inscripcion);

                connection.commit();
            } catch (InstanceNotFoundException except) {
                connection.commit();
                throw new RuntimeException(except);

            } catch (SQLException sqlEx) {
                connection.rollback();
                throw new RuntimeException(sqlEx);

            } catch (RuntimeException | Error error) {
                connection.rollback();
                throw error;

            }
            return inscripcionEncontrada;
        } catch (SQLException sqlE){
            throw new RuntimeException();
        }
    }

    private Curso findCurso(Long idCurso) {

        DataSource baseDatos = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        Curso cursoEncontrado = null;

        try (Connection connection = baseDatos.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                cursoEncontrado = cursoDao.find(connection, idCurso);

                connection.commit();
            } catch (InstanceNotFoundException except) {
                connection.commit();
                throw new RuntimeException(except);

            } catch (SQLException sqlEx) {
                connection.rollback();
                throw new RuntimeException(sqlEx);

            } catch (RuntimeException | Error error) {
                connection.rollback();
                throw error;

            }
            return cursoEncontrado;
        } catch (SQLException sqlE){
            throw new RuntimeException();
        }
    }

    @Test
    public void testAddCursoAndFindCurso() throws InputValidationException, InstanceNotFoundException {
        Curso curso = crearCursoValido("Curso1");
        Curso cursoAnadido = null;

        try {

            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);
            cursoAnadido = ficTrainingService.addCurso(curso);
            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            Curso cursoEncontrado = ficTrainingService.buscarCurso(cursoAnadido.getIdCurso());

            assertEquals(cursoAnadido, cursoEncontrado);
            assertTrue((cursoEncontrado.getFechaAlta().compareTo(beforeCreationDate) >= 0)
                    && (cursoEncontrado.getFechaAlta().compareTo(afterCreationDate) <= 0));

        } finally {if (cursoAnadido!=null) removeCurso(cursoAnadido.getIdCurso());}
    }
    @Test
    public void testAddCursoInvalido() {

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setNombre(null);
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setNombre("");
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setCiudad(null);
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setCiudad("");
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setPrecio((short) -1);
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setPrecio((short) MAX_PRECIO + 1);
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setMaxPlazas((short) -1);
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

        assertThrows(InputValidationException.class, () -> {
            Curso curso = crearCursoValido("Curso1");
            curso.setMaxPlazas((short) MAX_PLAZAS + 1);
            Curso cursoAnadido = ficTrainingService.addCurso(curso);
            removeCurso(cursoAnadido.getIdCurso());
        });

    }



    @Test
    public void testFindNonExistentCurso() {
        assertThrows(InstanceNotFoundException.class, () -> ficTrainingService.buscarCurso(100L));
    }

    @Test
    public void testFindCursos() throws InputValidationException {
        List<Curso> cursos = new ArrayList<>();
        List<Curso> cursosEncontrados = new ArrayList<>();
        List<Curso> cursosVacio = new ArrayList<>();
        List<Curso> cursosFecha = new ArrayList<>();
        List<Curso> cursosCiudad = new ArrayList<>();
        Curso curso1 = createCurso(crearCursoValido("Curso10"));
        Curso curso2 = createCurso(crearCursoValido("Curso11"));
        Curso curso3 = createCurso(crearCursoValido("Curso12"));
        Curso curso4 = createCurso(crearCursoPorCiudad("Barcelona"));
        Curso curso5 = createCurso(crearCursoPorFecha(LocalDateTime.of(2028, 11, 5, 10, 0)));
        cursos.add(curso1);
        cursos.add(curso2);
        cursos.add(curso3);
        cursos.add(curso4);
        cursos.add(curso5);
        try {
            cursosEncontrados = ficTrainingService.buscarCursoLocFecha("Madrid",curso1.getFechaAlta());
            assertEquals(4, cursosEncontrados.size());

            cursosVacio = ficTrainingService.buscarCursoLocFecha("Coruña",curso2.getFechaAlta());
            assertEquals(0, cursosVacio.size());

            cursosFecha = ficTrainingService.buscarCursoLocFecha("Madrid",LocalDateTime.of(2027, 11, 5, 10, 0));
            assertEquals(1, cursosFecha.size());

            cursosCiudad = ficTrainingService.buscarCursoLocFecha("Barcelona",LocalDateTime.of(2020, 11, 5, 10, 0));
            assertEquals(1, cursosCiudad.size());

        } finally { for (Curso curso : cursos) removeCurso(curso.getIdCurso());}
    }

    @Test
    public void testFindCursosWithInvalidParams(){
        assertThrows(InputValidationException.class, () -> ficTrainingService.buscarCursoLocFecha("CIUDAD",null));
    }

    @Test
    public void test_hacer_inscripcion_en_curso_Correctamente()
            throws InstanceNotFoundException, InputValidationException, CursoEmpezadoException, NotEnougthPlazasDispException {

        Curso curso = createCurso(crearCursoValido("Curso1"));
        int plazasDisponibles = curso.getPlazasDisponibles();
        Inscripcion inscripcionBuscada = null;
        Long idInscripcion;

        try {

            LocalDateTime fechaAnteriorInscripcion = LocalDateTime.now().withNano(0);
            idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO, TARJETA_CORRECTA, curso.getIdCurso());
            LocalDateTime fechaPosteriorInscripcion = LocalDateTime.now().withNano(0);

            inscripcionBuscada = findInscripcion(idInscripcion);

            assertEquals(inscripcionBuscada.getidInscripcion(),idInscripcion);
            assertEquals(inscripcionBuscada.getEmailUser(),USUARIO_CORRECTO);
            assertEquals(inscripcionBuscada.getNumTarjeta(),TARJETA_CORRECTA);
            assertEquals(inscripcionBuscada.getIdCurso(), curso.getIdCurso());
            assertTrue((inscripcionBuscada.getFechaHoraCreacion().compareTo(fechaPosteriorInscripcion)<=0)&&(inscripcionBuscada.getFechaHoraCreacion().compareTo(fechaAnteriorInscripcion)>=0));

            Curso cursoBuscado = findCurso(curso.getIdCurso());
            assertEquals(cursoBuscado.getPlazasDisponibles(),plazasDisponibles-1);
        } finally {
            if (inscripcionBuscada != null) {
                removeInscripcion(inscripcionBuscada.getidInscripcion());
            }
            removeCurso(curso.getIdCurso());
        }
    }

    @Test
    public void testInscripcionCursoInexistente() {
        assertThrows(InstanceNotFoundException.class,() -> {
            Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,TARJETA_CORRECTA,1L);
            removeInscripcion(idInscripcion);
        });
    }

    @Test
    public void testInscripcionTarjetaMal() {
        Curso curso = createCurso(crearCursoValido("CursoTM"));
        try{
            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,null,curso.getIdCurso());
                removeInscripcion(idInscripcion);
            });

            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,"",curso.getIdCurso());
                removeInscripcion(idInscripcion);
            });

        } finally {
            removeCurso(curso.getIdCurso());
        }
    }
    @Test
    public void testInscripcionEmailMal() {
        Curso curso = createCurso(crearCursoValido("CursoEM"));
        try{
            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso("",TARJETA_CORRECTA,curso.getIdCurso());
                removeInscripcion(idInscripcion);
            });

            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(null,TARJETA_CORRECTA,curso.getIdCurso());
                removeInscripcion(idInscripcion);
            });

        } finally {
            removeCurso(curso.getIdCurso());
        }
    }
    @Test
    public void testIdCursoNegativoONulo() {
        Curso curso = createCurso(crearCursoValido("CursoidN"));
        try{
            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,TARJETA_CORRECTA,null);
                removeInscripcion(idInscripcion);
            });

            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,TARJETA_CORRECTA,-1L);
                removeInscripcion(idInscripcion);
            });

            assertThrows(InputValidationException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,TARJETA_CORRECTA,0L);
                removeInscripcion(idInscripcion);
            });

        } finally {
            removeCurso(curso.getIdCurso());
        }
    }

    @Test
    public void testInscripcionNotEnougthPlazasDisp(){

        LocalDateTime fechaComienzo =LocalDateTime.now().plusDays(50).withNano(0);
        Curso curso = createCurso(new Curso("CursoLl","A Coruña",100,fechaComienzo,1));
        Inscripcion PrimeraInscripcion = null;
        try{
            String email = "email@user1";
            String numTarj = "1233567899123478";
            PrimeraInscripcion = createInscripcion(email,numTarj,curso.getIdCurso());


            assertThrows(NotEnougthPlazasDispException.class, () -> {
                String email2 = "email@user2";
                String numTarj2 = "1234567890123456";
                Long idInscripcion = ficTrainingService.inscripcion_curso(email2,numTarj2,curso.getIdCurso());
                removeInscripcion(idInscripcion);
            });
            removeInscripcion(PrimeraInscripcion.getidInscripcion());
        } finally {

            removeCurso(curso.getIdCurso());
        }
    }


    @Test
    public void testInscripcionCursoEmpezado(){
        LocalDateTime fechaComienzo = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime fechaComienzo2 = LocalDateTime.now().minusDays(5).withNano(0);
        Curso curso = createCurso(new Curso("CursoE","A Coruña",150,fechaComienzo,1));
        curso.setFechaComienzo(fechaComienzo2);
        updateCurso(curso);
        try{
            assertThrows(CursoEmpezadoException.class, () -> {
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO,TARJETA_CORRECTA,curso.getIdCurso());
                removeInscripcion(idInscripcion);
            });
        } finally {
            removeCurso(curso.getIdCurso());
        }

    }

    @Test
    public void testCancelacionCorrecta() throws InputValidationException, InstanceNotFoundException,InscriptionNoCancelableException {
        Curso curso = createCurso(crearCursoValido("CursoCancelar"));

        try{
            Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO, TARJETA_CORRECTA, curso.getIdCurso());
            ficTrainingService.cancel_inscription(idInscripcion, USUARIO_CORRECTO);
            Inscripcion inscripcionCancel = findInscripcion(idInscripcion);
            assertNotNull(inscripcionCancel.getFechaCancelacion());

            removeInscripcion(idInscripcion);

        } finally{
            removeCurso(curso.getIdCurso());
        }
    }

    @Test

    public void testCancelarInscripcionNotFoundException() {
        assertThrows(InstanceNotFoundException.class, () ->
                ficTrainingService.cancel_inscription(1L, USUARIO_CORRECTO));
    }


    @Test
    public void testCancelarInputValidation(){

        assertThrows(InputValidationException.class, () ->{
            ficTrainingService.cancel_inscription(null,USUARIO_CORRECTO);
        });

        assertThrows(InputValidationException.class, () ->{
            ficTrainingService.cancel_inscription(-1L,USUARIO_CORRECTO);
        });

        assertThrows(InputValidationException.class, () ->{
            ficTrainingService.cancel_inscription(1L,null);
        });

    }

    @Test

    public void testCancelarInscripcionCancelada() throws InstanceNotFoundException, InputValidationException, InscriptionNoCancelableException {

        Curso curso = createCurso(crearCursoValido("CursoYaCancelado"));
        Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO, TARJETA_CORRECTA, curso.getIdCurso());

        ficTrainingService.cancel_inscription(idInscripcion, USUARIO_CORRECTO);

        assertThrows(AlreadyCanceledException.class, () ->
                ficTrainingService.cancel_inscription(idInscripcion, USUARIO_CORRECTO));

        removeInscripcion(idInscripcion);
        removeCurso(curso.getIdCurso());
    }

    @Test

    public void testCancelacionUserIncorrecto() {
        Curso curso = createCurso(crearCursoPorFecha(LocalDateTime.now().plusDays(5)));

        try{
            assertThrows(IncorrectUserException.class, () ->{
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO, TARJETA_CORRECTA, curso.getIdCurso());
                ficTrainingService.cancel_inscription(idInscripcion,"OTRO_USUARIO");

                removeInscripcion(idInscripcion);
            });
        } finally{
            removeCurso(curso.getIdCurso());
        }
    }

    @Test

    public void testCancelacionFueraDePlazo() {
        Curso curso = createCurso(crearCursoPorFecha(LocalDateTime.now().plusDays(5)));

        try{
            assertThrows(InscriptionNoCancelableException.class, () ->{
                Long idInscripcion = ficTrainingService.inscripcion_curso(USUARIO_CORRECTO, TARJETA_CORRECTA, curso.getIdCurso());
                ficTrainingService.cancel_inscription(idInscripcion, USUARIO_CORRECTO);

                removeInscripcion(idInscripcion);
            });
        } finally{
            removeCurso(curso.getIdCurso());
        }
    }


    @Test
    public void testobt_todas_inscripciones() {
        List<Curso> listacursos = new LinkedList<Curso>();
        List<Inscripcion> listaIncripciones = new LinkedList<Inscripcion>();

        Curso curso1 = createCurso(crearCursoValido("Curso1"));
        Curso curso2 = createCurso(crearCursoValido("Curso2"));
        Curso curso3 = createCurso(crearCursoValido("Curso3"));
        Curso curso4 = createCurso(crearCursoValido("Curso4"));
        listacursos.add(curso1);
        listacursos.add(curso2);
        listacursos.add(curso3);
        listacursos.add(curso4);
        Inscripcion Inscripcion1, Inscripcion2, Inscripcion3, Inscripcion4;

        try{
            Inscripcion1 = createInscripcion(USUARIO_CORRECTO, TARJETA_CORRECTA, curso1.getIdCurso());
            Inscripcion2 = createInscripcion(USUARIO_CORRECTO, TARJETA_CORRECTA, curso2.getIdCurso());
            Inscripcion3 = createInscripcion(USUARIO_CORRECTO, TARJETA_CORRECTA, curso3.getIdCurso());
            Inscripcion4 = createInscripcion(USUARIO_CORRECTO, TARJETA_CORRECTA, curso4.getIdCurso());

            //obtenemos las incripciones ordenadas por fecha de creación de la inscripción
            //(primero las más recientes)
            listaIncripciones.add(Inscripcion4);
            listaIncripciones.add(Inscripcion3);
            listaIncripciones.add(Inscripcion2);
            listaIncripciones.add(Inscripcion1);


            List<Inscripcion> inscripcionesObtenidas = ficTrainingService.obt_todas_inscripciones(USUARIO_CORRECTO);

            assertEquals(listaIncripciones.size(), inscripcionesObtenidas.size());
            for(int i = 0; i < listaIncripciones.size();i++){
                assertEquals(listaIncripciones.get(i).getidInscripcion(), inscripcionesObtenidas.get(i).getidInscripcion());
            }

        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        } finally {
            for(Inscripcion ins: listaIncripciones){
                removeInscripcion(ins.getidInscripcion());
            }
            for(Curso c: listacursos){
                removeCurso(c.getIdCurso());
            }

        }

    }

    @Test
    public void testobt_todas_inscripcionesNOUSER() {

        assertThrows(InputValidationException.class, () -> {
            List<Inscripcion> inscripcionesObtenidas = ficTrainingService.obt_todas_inscripciones(null);
        });

    }

    @Test
    public void testobt_todas_inscripcionesUSERSININSCRIPCIONES() {
        List<Curso> listacursos = new LinkedList<Curso>();
        List<Inscripcion> listaIncripciones = new LinkedList<Inscripcion>();
        Curso curso1 = createCurso(crearCursoValido("Curso1"));
        Curso curso2 = createCurso(crearCursoValido("Curso2"));
        Curso curso3 = createCurso(crearCursoValido("Curso3"));
        Curso curso4 = createCurso(crearCursoValido("Curso4"));
        listacursos.add(curso1);
        listacursos.add(curso2);
        listacursos.add(curso3);
        listacursos.add(curso4);
        Inscripcion Inscripcion1, Inscripcion2, Inscripcion3, Inscripcion4;

        try{

            //hacer función en before all como createCurso para gestionar excepciones
            Inscripcion1 = createInscripcion(USUARIO_CORRECTO,TARJETA_CORRECTA, curso1.getIdCurso());
            Inscripcion2 = createInscripcion(USUARIO_CORRECTO, TARJETA_CORRECTA, curso2.getIdCurso());
            Inscripcion3 = createInscripcion(USUARIO_CORRECTO,TARJETA_CORRECTA, curso3.getIdCurso());
            Inscripcion4 = createInscripcion(USUARIO_CORRECTO, TARJETA_CORRECTA,curso4.getIdCurso());

            listaIncripciones.add(Inscripcion1);
            listaIncripciones.add(Inscripcion2);
            listaIncripciones.add(Inscripcion3);
            listaIncripciones.add(Inscripcion4);


            List<Inscripcion> inscripcionesObtenidas =  ficTrainingService.obt_todas_inscripciones("email-user@sin-inscripciones.com");

            assertEquals(inscripcionesObtenidas.size(),0);


        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        } finally {
            for(Inscripcion ins: listaIncripciones){
                removeInscripcion(ins.getidInscripcion());
            }
            for(Curso c: listacursos){
                removeCurso(c.getIdCurso());
            }

        }
    }
}
