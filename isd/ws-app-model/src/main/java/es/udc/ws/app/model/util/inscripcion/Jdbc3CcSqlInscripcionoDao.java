package es.udc.ws.app.model.util.inscripcion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlInscripcionoDao extends AbstractSqlInscripcionDao {
    @Override
    public Inscripcion create(Connection connection, Inscripcion inscripcion){

        String consultaSqlString = "INSERT INTO Inscripcion" +
                "(emailUser, numTarjeta, fechaHoraCreacion, fechaCancelacion, idCurso)"
                +"VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement prepStatement = connection.prepareStatement(consultaSqlString, Statement.RETURN_GENERATED_KEYS)){
            int j = 1;

            //cubrimos parámetros de consulta y ejecutamos consulta
            prepStatement.setString(j++, inscripcion.getEmailUser());
            prepStatement.setString(j++, inscripcion.getNumTarjeta());

            prepStatement.setTimestamp(j++, Timestamp.valueOf(inscripcion.getFechaHoraCreacion()));
            if(inscripcion.getFechaCancelacion() != null){
                prepStatement.setTimestamp(j++, Timestamp.valueOf(inscripcion.getFechaCancelacion()));
            }else {
                prepStatement.setTimestamp(j++, null);
            }

            prepStatement.setLong(j++, inscripcion.getIdCurso());

            prepStatement.executeUpdate();
            //Obtener id de Inscripción
            ResultSet resultset = prepStatement.getGeneratedKeys();
            if (!resultset.next()){
                throw new SQLException("JBC driver no devolvió la clave generada");
            }
            Long idInscripcion = resultset.getLong(1);

            return new Inscripcion(idInscripcion, inscripcion.getEmailUser(), inscripcion.getNumTarjeta(),
                    inscripcion.getFechaHoraCreacion(), inscripcion.getFechaCancelacion(), inscripcion.getIdCurso());

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }



}
