package es.udc.ws.app.model.util.curso;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;
import java.time.LocalDateTime;
import java.sql.Connection;

public interface SqlCursoDao {
    public Curso create(Connection connection, Curso curso);
    public List<Curso> findByCityAndDate(Connection connection, String city, LocalDateTime date);
    public Curso find(Connection connection, Long cursoId) throws InstanceNotFoundException;
    public void update(Connection connection, Curso curso)
            throws InstanceNotFoundException;
    public void remove(Connection connection, Long idCurso)
        throws InstanceNotFoundException;
}

