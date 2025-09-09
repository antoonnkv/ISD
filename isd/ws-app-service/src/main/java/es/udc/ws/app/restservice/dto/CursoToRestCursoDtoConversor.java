package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.restservice.dto.RestCursoDto;

public class CursoToRestCursoDtoConversor {

    public static Curso toCurso(RestCursoDto curso) {
        return new Curso(curso.getIdCurso(),curso.getNombre(),curso.getCiudad(), curso.getPrecio(),LocalDateTime.parse(curso.getFechaComienzo()),curso.getMaxPlazas());
    }

    public static RestCursoDto toRestCursoDto(Curso curso) {
        return new RestCursoDto(curso.getIdCurso(), curso.getNombre(), curso.getCiudad(), curso.getPrecio(), curso.getFechaComienzo().toString(), curso.getMaxPlazas(), curso.getPlazasDisponibles());
    }

    public static List<RestCursoDto> toRestCursoDtoList(List<Curso> cursos) {
	List<RestCursoDto> cursosDto = new ArrayList<>(cursos.size());
	for (int i = 0; i < cursos.size(); i++) cursosDto.add(toRestCursoDto(cursos.get(i)));
	return cursosDto;
    }

}

