package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class RestCursoDto {

    /////////////////////ATRIBUTOS/////////////////////
    Long idCurso;
    String nombre;
    String ciudad;
    int plazasDisponibles;
    float precio;
    int maxPlazas;
    String fechaComienzo;

    /////////////////////CONSTRUCTORES/////////////////////
    public RestCursoDto() {
    }
    public RestCursoDto(Long idCurso, String nombre, String ciudad, float precio, String fechaComienzo, int maxPlazas) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.precio = precio;
        this.fechaComienzo = fechaComienzo;
        this.maxPlazas = maxPlazas;
        this.plazasDisponibles = maxPlazas;
    }
    
    public RestCursoDto(Long idCurso, String nombre, String ciudad, float precio, String fechaComienzo, int maxPlazas, int plazasDisponibles) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.precio = precio;
        this.fechaComienzo = fechaComienzo;
        this.maxPlazas = maxPlazas;
        this.plazasDisponibles = plazasDisponibles;
    }
    
    /////////////////////SETTERS/////////////////////
    public void setIdCurso(Long idCurso){this.idCurso = idCurso;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setCiudad(String ciudad){this.ciudad = ciudad;}
    public void setPrecio(float precio){this.precio = precio;}
    public void setFechaComienzo(String fechaComienzo){this.fechaComienzo = fechaComienzo;}
    public void setMaxPlazas(int maxPlazas){this.maxPlazas = maxPlazas;}
    public void setPlazasDisponibles(int plazasDisponibles){this.plazasDisponibles = plazasDisponibles;}

    /////////////////////GETTERS/////////////////////
    public Long getIdCurso(){return idCurso;}
    public String getNombre(){return nombre;}
    public String getCiudad(){return ciudad;}
    public float getPrecio(){return precio;}
    public String getFechaComienzo(){return fechaComienzo;}
    public int getMaxPlazas(){return maxPlazas;}
    public int getPlazasDisponibles(){return plazasDisponibles;}

    /////////////////////TOSTRING/////////////////////
    @Override
    public String toString() {
        return "CursoDto [cursoId= " + idCurso + ", nombre= " + nombre + ", ciudad= " + ciudad + ", plazasDisponibles= " 
        + plazasDisponibles + ", precio= " + precio + ", maxPlazas= " + maxPlazas + ", fechaComienzo= " + fechaComienzo + "]";
    } 
}

