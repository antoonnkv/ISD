package es.udc.ws.app.model.util.curso;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class AbstractSqlCursoDao implements SqlCursoDao {

    protected AbstractSqlCursoDao() {
    }

    @Override
    public List<Curso> findByCityAndDate(Connection connection, String city, LocalDateTime date) {

        String queryString = "SELECT idCurso, nombre, ciudad, plazasDisponibles, precio, maxPlazas, fechaComienzo, fechaAlta FROM Curso WHERE";
        queryString += " LOWER(ciudad) LIKE LOWER(?) AND";
        queryString += " fechaComienzo > ?";
        queryString += " ORDER BY fechaComienzo";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, "%" + city + "%");
            preparedStatement.setTimestamp(2, Timestamp.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Curso> cursos = new ArrayList<>();

            while (resultSet.next()) {
                int i = 1;
                Long idCurso = resultSet.getLong(i++);
                String nombre = resultSet.getString(i++);
                String ciudad = resultSet.getString(i++);
                int plazasDisponibles = resultSet.getInt(i++);
                float precio = resultSet.getFloat(i++);
                int maxPlazas = resultSet.getInt(i++);
                LocalDateTime fechaComienzo = resultSet.getTimestamp(i++).toLocalDateTime();
                LocalDateTime fechaAlta = resultSet.getTimestamp(i) != null ? resultSet.getTimestamp(i).toLocalDateTime() : null;
                Curso curso = new Curso(idCurso, nombre, ciudad, precio, fechaComienzo, fechaAlta, maxPlazas);
                curso.setPlazasDisponibles(plazasDisponibles);
                cursos.add(curso);
            }
            return cursos;
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    @Override
    public Curso find(Connection connection, Long cursoId) throws InstanceNotFoundException {

        String queryString = "SELECT nombre, ciudad, plazasDisponibles, precio, maxPlazas, fechaComienzo, fechaAlta"
                + " FROM Curso WHERE idCurso = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, cursoId.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) throw new InstanceNotFoundException(cursoId,Curso.class.getName());

            i = 1;
            String nombre = resultSet.getString(i++);
            String ciudad = resultSet.getString(i++);
            int plazasDisponibles = resultSet.getInt(i++);
            float precio = resultSet.getFloat(i++);
            int maxPlazas = resultSet.getInt(i++);
            LocalDateTime fechaComienzo = resultSet.getTimestamp(i++).toLocalDateTime();
            LocalDateTime fechaAlta = resultSet.getTimestamp(i).toLocalDateTime();

            Curso curso = new Curso(cursoId, nombre, ciudad, precio, fechaComienzo, fechaAlta, maxPlazas);
            curso.setPlazasDisponibles(plazasDisponibles);
            return curso;
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    @Override
    public void update(Connection connection, Curso curso)
            throws InstanceNotFoundException {

        String queryString = "UPDATE Curso SET nombre = ?, ciudad = ?, plazasDisponibles = ?, precio = ?, maxPlazas = ? , fechaComienzo = ? WHERE idCurso = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;

            preparedStatement.setString(i++, curso.getNombre());
            preparedStatement.setString(i++, curso.getCiudad());
            preparedStatement.setInt(i++, curso.getPlazasDisponibles());
            preparedStatement.setFloat(i++,curso.getPrecio());
            preparedStatement.setInt(i++, curso.getMaxPlazas());
            Timestamp date = curso.getFechaComienzo() != null ? Timestamp.valueOf(curso.getFechaComienzo()): null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setLong(i++, curso.getIdCurso());

            int updateRows = preparedStatement.executeUpdate();

            if(updateRows == 0){
                throw new InstanceNotFoundException(curso.getIdCurso(),Curso.class.getName());
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void remove(Connection connection, Long idCurso)
            throws InstanceNotFoundException {
        String consultaString = "DELETE FROM Curso" +
                " WHERE idCurso = ?";
        try (PreparedStatement prepStatement = connection.prepareStatement(consultaString)) {
            int numFilasEliminadas;
            int j = 1;

            prepStatement.setLong(j++, idCurso);
            numFilasEliminadas = prepStatement.executeUpdate();
            if (numFilasEliminadas == 0) {
                throw new InstanceNotFoundException(idCurso, Curso.class.getName());
            }

        } catch (SQLException except) {
            throw new RuntimeException(except);
        }
    }

}
