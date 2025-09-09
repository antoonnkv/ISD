package es.udc.ws.app.model.util.inscripcion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlInscripcionDao implements SqlInscripcionDao {

    protected AbstractSqlInscripcionDao() {
    }

    public Inscripcion findInscripcion(Connection connection,Long InscripcionId)
            throws InstanceNotFoundException {

        String queryString = "SELECT emailUser, numTarjeta,"
                + " fechaHoraCreacion, fechaCancelacion, idCurso FROM Inscripcion WHERE idInscripcion = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, InscripcionId.longValue());

            ResultSet resultadoConsulta = preparedStatement.executeQuery();

            if (!resultadoConsulta.next()) {
                throw new InstanceNotFoundException(InscripcionId , Inscripcion.class.getName());
            }

            i = 1;
            //obtenemos resultados consulta en orden
            // (String emailUser, String numTarjeta, LocalDateTime fechaHora, int plazoCancelacion, Long idCurso)
            String emailUser = resultadoConsulta.getString(i++);
            String numTarjeta = resultadoConsulta.getString(i++);

            Timestamp fechaHoraCreacionTs = resultadoConsulta.getTimestamp(i++); //obtenemos fecha en formato timestamp sql
            LocalDateTime fechaHoraCreacion = fechaHoraCreacionTs.toLocalDateTime();     //convertimos a LocaldateTime

            Timestamp fechaCancelacionTs = resultadoConsulta.getTimestamp(i++);
            LocalDateTime fechaCancelacion = fechaCancelacionTs != null
                    ? fechaCancelacionTs.toLocalDateTime()
                    : null;

            Long idCurso = resultadoConsulta.getLong(i++);

            //devolvemos la inscripción buscada
            return new Inscripcion(InscripcionId, emailUser, numTarjeta, fechaHoraCreacion, fechaCancelacion, idCurso);




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void update(Connection connection, Inscripcion inscripcion)
            throws InstanceNotFoundException {

        String queryString = "UPDATE Inscripcion SET idCurso = ?, emailUser = ?, numTarjeta = ?, fechaCancelacion = ? WHERE idInscripcion = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i=1;

            preparedStatement.setLong(i++, inscripcion.getIdCurso());
            preparedStatement.setString(i++, inscripcion.getEmailUser());
            preparedStatement.setString(i++, inscripcion.getNumTarjeta());
            preparedStatement.setTimestamp(i++,Timestamp.valueOf(inscripcion.getFechaCancelacion()));
            preparedStatement.setLong(i++,inscripcion.getidInscripcion());

            int updateRows = preparedStatement.executeUpdate();

            if(updateRows == 0){
                throw new InstanceNotFoundException(inscripcion.getidInscripcion(),Inscripcion.class.getName());
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscripcion> obt_todas_inscripciones(Connection connection, String email) {

        String consultaString = "SELECT idInscripcion, emailUser, numTarjeta, fechaHoraCreacion,"
                + " fechaCancelacion, idCurso FROM Inscripcion " + "WHERE emailUser = ? ORDER BY fechaHoraCreacion";

        try (PreparedStatement prepStatement = connection.prepareStatement(consultaString)) {
            int i = 1;
            prepStatement.setString(i++, email);

            ResultSet resultadoConsulta = prepStatement.executeQuery(); //ejecuta la consulta

            //inicializa lista inscripciones
            List<Inscripcion> listaInscripciones = new ArrayList<Inscripcion>();

            //bucle que crea la lista con las inscripciones del usuario
            while (resultadoConsulta.next()) {
                int j = 1;
                Long idInscripcion = resultadoConsulta.getLong(j++);
                String emailUser = resultadoConsulta.getString(j++);
                String numTarjeta = resultadoConsulta.getString(j++);

                Timestamp fechaHoraCreacionTs = resultadoConsulta.getTimestamp(j++);
                LocalDateTime fechaHoraCreacion = fechaHoraCreacionTs.toLocalDateTime();

                Timestamp cancelacionTs = resultadoConsulta.getTimestamp(j++);
                LocalDateTime fechaCancelacion;

                if(cancelacionTs!= null){
                    fechaCancelacion = cancelacionTs.toLocalDateTime();
                }else {
                    fechaCancelacion = null;
                }

                Long idCurso = resultadoConsulta.getLong(j++);

                listaInscripciones.add(new Inscripcion(idInscripcion,emailUser, numTarjeta, fechaHoraCreacion, fechaCancelacion, idCurso));
            }
            return listaInscripciones.reversed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void remove(Connection connection, Long idInscripcion)
            throws InstanceNotFoundException {

        String consultaString = "DELETE FROM Inscripcion"+
                " WHERE idInscripcion = ?";

        try (PreparedStatement prepStatement = connection.prepareStatement(consultaString)){
            int numFilasEliminadas;
            int j = 1;

            prepStatement.setLong(j++, idInscripcion);
            numFilasEliminadas = prepStatement.executeUpdate();
            if (numFilasEliminadas == 0){
                throw new InstanceNotFoundException(idInscripcion, Inscripcion.class.getName());
            }

        }catch (SQLException except){
            throw new RuntimeException(except);
        }
    }

}