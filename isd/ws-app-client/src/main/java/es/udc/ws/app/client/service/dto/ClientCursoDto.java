package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientCursoDto {

    /////////////////////ATRIBUTOS/////////////////////
    Long idCurso;
    String nombre;
    String ciudad;
    float precio;
    int maxPlazas;
    int plazasReservadas;
    LocalDateTime fechaComienzo;

    /////////////////////CONSTRUCTORES/////////////////////
    public ClientCursoDto() {
    }

    public ClientCursoDto(Long idCurso, String nombre, String ciudad, float precio, LocalDateTime fechaComienzo, int maxPlazas) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.precio = precio;
        this.fechaComienzo = fechaComienzo;
        this.maxPlazas = maxPlazas;
        this.plazasReservadas = 0;
    }

    /////////////////////SETTERS/////////////////////
    public void setIdCurso(Long idCurso){this.idCurso = idCurso;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setCiudad(String ciudad){this.ciudad = ciudad;}
    public void setPrecio(float precio){this.precio = precio;}
    public void setFechaComienzo(LocalDateTime fechaComienzo){this.fechaComienzo = fechaComienzo;}
    public void setMaxPlazas(int maxPlazas){this.maxPlazas = maxPlazas;}
    public void setPlazasReservadas(int plazasReservadas){this.plazasReservadas = plazasReservadas;}

    /////////////////////GETTERS/////////////////////
    public Long getIdCurso(){return idCurso;}
    public String getNombre(){return nombre;}
    public String getCiudad(){return ciudad;}
    public float getPrecio(){return precio;}
    public LocalDateTime getFechaComienzo(){return fechaComienzo;}
    public int getMaxPlazas(){return maxPlazas;}
    public int getPlazasReservadas(){return plazasReservadas;}

    /////////////////////TOSTRING/////////////////////
    @Override
    public String toString() {
        return "Curso [CursoId= " + idCurso + ", Nombre= " + nombre + ", Ciudad= " + ciudad + ", Precio= " +
                precio + ", Fecha Comienzo= " + fechaComienzo + ", Max. Plazas= " + maxPlazas + ", Plazas Reservadas= " + plazasReservadas + "]";
    }
}

