package es.udc.ws.app.model.util.inscripcion;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SqlInscripcionDao {
    public Inscripcion create(Connection connection, Inscripcion inscripcion);
    public void update(Connection connection, Inscripcion inscripcion)
            throws InstanceNotFoundException;
    public List<Inscripcion> obt_todas_inscripciones(Connection connection, String email);
    public Inscripcion findInscripcion(Connection connection,Long idInscripcion) throws InstanceNotFoundException;
    public void remove(Connection connection, Long idInscripcion)
            throws InstanceNotFoundException;
}
