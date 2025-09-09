package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.thrift.ThriftCursoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CursoToThriftCursoDtoConversor {
    public static Curso toCurso(ThriftCursoDto curso) {
        return new Curso(curso.getIdCurso(), curso.getNombre(), curso.getCiudad(),
                (float)curso.getPrecio(), LocalDateTime.parse(curso.getFechaComienzo()),  curso.getMaxPlazas());
    }

    public static List<ThriftCursoDto> toThriftCursoDtos(List<Curso> cursos) {
        List<ThriftCursoDto> dtos = new ArrayList<>(cursos.size());
        for (Curso curso : cursos) dtos.add(toThriftCursoDto(curso));
        return dtos;
    }

    public static ThriftCursoDto toThriftCursoDto(Curso curso) {
        return new ThriftCursoDto(curso.getIdCurso(), curso.getNombre(), curso.getCiudad(),
                curso.getPlazasDisponibles(), curso.getPrecio(), curso.getMaxPlazas(),
                curso.getFechaComienzo().toString());
    }
}
