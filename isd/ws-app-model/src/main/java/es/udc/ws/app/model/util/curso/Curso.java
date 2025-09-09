package es.udc.ws.app.model.util.curso;

import java.time.LocalDateTime;
import java.util.Objects;

public class Curso {

    /////////////////////ATRIBUTOS/////////////////////
    Long idCurso;
    String nombre;
    String ciudad;
    int plazasDisponibles;
    float precio;
    int maxPlazas;
    LocalDateTime fechaComienzo;
    LocalDateTime fechaAlta;

    /////////////////////CONSTRUCTORES/////////////////////
    public Curso(String nombre, String ciudad, float precio, LocalDateTime fechaComienzo, int maxPlazas) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.precio = precio;
        this.fechaComienzo = (fechaComienzo != null) ? fechaComienzo.withNano(0) : null;
        this.maxPlazas = maxPlazas;
        this.plazasDisponibles = maxPlazas;
    }

    public Curso(Long idCurso, String nombre, String ciudad, float precio, LocalDateTime fechaComienzo, int maxPlazas) {
        this(nombre,ciudad,precio,fechaComienzo,maxPlazas);
        this.idCurso = idCurso;
    }

    public Curso(Long idCurso, String nombre, String ciudad, float precio, LocalDateTime fechaComienzo, LocalDateTime fechaAlta, int maxPlazas) {
        this(idCurso,nombre,ciudad,precio,fechaComienzo,maxPlazas);
        this.fechaAlta = (fechaAlta != null) ? fechaAlta.withNano(0) : null;
    }

    /////////////////////SETTERS/////////////////////
    public void setIdCurso(Long idCurso){this.idCurso = idCurso;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setCiudad(String ciudad){this.ciudad = ciudad;}
    public void setPrecio(float precio){this.precio = precio;}
    public void setFechaComienzo(LocalDateTime fechaComienzo){this.fechaComienzo = (fechaComienzo != null) ? fechaComienzo.withNano(0) : null;}
    public void setFechaAlta(LocalDateTime fechaAlta){this.fechaAlta = (fechaAlta != null) ? fechaAlta.withNano(0) : null;}
    public void setMaxPlazas(int maxPlazas){this.maxPlazas = maxPlazas;}
    public void setPlazasDisponibles(int plazasDisponibles){this.plazasDisponibles = plazasDisponibles;}

    /////////////////////GETTERS/////////////////////
    public Long getIdCurso(){return idCurso;}
    public String getNombre(){return nombre;}
    public String getCiudad(){return ciudad;}
    public float getPrecio(){return precio;}
    public LocalDateTime getFechaComienzo(){return fechaComienzo;}
    public LocalDateTime getFechaAlta(){return fechaAlta;}
    public int getMaxPlazas(){return maxPlazas;}
    public int getPlazasDisponibles(){return plazasDisponibles;}

    /////////////////////EQUALS & HASHCODE/////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso other = (Curso) o;
        if ((idCurso == null && other.idCurso != null) || (!Objects.equals(idCurso, other.idCurso))) return false;
        if ((nombre == null && other.nombre != null) || (!Objects.equals(nombre, other.nombre))) return false;
        if ((ciudad == null && other.ciudad != null) || (!Objects.equals(ciudad, other.ciudad))) return false;
        if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio)) return false;
        if (plazasDisponibles != other.plazasDisponibles) return false;
        if (maxPlazas != other.maxPlazas) return false;
        if ((fechaComienzo == null && other.fechaComienzo != null) || (!Objects.equals(fechaComienzo, other.fechaComienzo))) return false;
        return (fechaAlta != null || other.fechaAlta == null) && (Objects.equals(fechaAlta, other.fechaAlta));
    }
    @Override
    public int hashCode() {return Objects.hash(idCurso, nombre, ciudad, plazasDisponibles, precio, maxPlazas, fechaComienzo, fechaAlta);}
}
