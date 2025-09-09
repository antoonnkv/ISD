package es.udc.ws.app.model.util.curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlCursoDao extends AbstractSqlCursoDao{

    @Override
    public Curso create(Connection connection, Curso curso){

        //creamos string correspondiente a consulta sql
        String consultaSqlString = "INSERT INTO Curso" +
                " (nombre, ciudad, plazasDisponibles, precio, maxPlazas, fechaComienzo,fechaAlta)"
                +" VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prepStatement = connection.prepareStatement(consultaSqlString, Statement.RETURN_GENERATED_KEYS)){
            int j = 1;
            //cubrimos parámetros de consulta
            prepStatement.setString(j++, curso.getNombre());
            prepStatement.setString(j++, curso.getCiudad());
            prepStatement.setInt(j++, curso.getMaxPlazas());
            prepStatement.setFloat(j++, curso.getPrecio());
            prepStatement.setInt(j++, curso.getMaxPlazas());
            prepStatement.setTimestamp(j++,Timestamp.valueOf(curso.getFechaComienzo()));
            prepStatement.setTimestamp(j++, Timestamp.valueOf(curso.getFechaAlta()));

            //Ejecutar consulta
            prepStatement.executeUpdate();

            //Obtener id de Curso
            ResultSet resultset = prepStatement.getGeneratedKeys();

            if (!resultset.next()){
                throw new SQLException("JBC driver no devolvió clave generada");

            }
            Long cursoId = resultset.getLong(1);


            //devolvemos objeto curso
            return new Curso(cursoId, curso.getNombre(),curso.getCiudad(), curso.getPrecio(),
                    curso.getFechaComienzo(), curso.getFechaAlta(),curso.getMaxPlazas());

        } catch (SQLException excep){
            throw new RuntimeException(excep);
        }
    }
}
