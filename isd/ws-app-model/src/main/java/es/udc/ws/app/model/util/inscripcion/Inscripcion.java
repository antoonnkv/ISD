package es.udc.ws.app.model.util.inscripcion;

import java.time.LocalDateTime;
import java.util.Objects;

public class Inscripcion {

    /////////////////////ATRIBUTOS/////////////////////
    private Long idInscripcion;
    private String emailUser;
    private String numTarjeta;
    private LocalDateTime fechaHoraCreacion;
    private LocalDateTime fechaCancelacion;
    private Long idCurso;

    /////////////////////CONSTRUCTORES/////////////////////
    public Inscripcion(String emailUser, String numTarjeta, LocalDateTime fechaHoraCreacion,LocalDateTime fechaCancelacion, Long idCurso) {
        this.emailUser = emailUser;
        this.numTarjeta = numTarjeta;
        this.fechaCancelacion = (fechaCancelacion != null) ? fechaCancelacion.withNano(0) : null;
        this.fechaHoraCreacion = (fechaHoraCreacion != null) ? fechaHoraCreacion.withNano(0) : null;
        this.idCurso = idCurso;
    }

    public Inscripcion(Long idInscripcion, String emailUser, String numTarjeta, LocalDateTime fechaHoraCreacion,LocalDateTime fechaCancelacion, Long idCurso) {
        this(emailUser,numTarjeta,fechaHoraCreacion,fechaCancelacion,idCurso);
        this.idInscripcion = idInscripcion;
    }

    /////////////////////SETTERS/////////////////////
    public void setidInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}
    public void setEmailUser(String emailUser) {this.emailUser = emailUser;}
    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
        this.fechaHoraCreacion = (fechaHoraCreacion != null) ? fechaHoraCreacion.withNano(0) : null;}
    public void setNumTarjeta(String numTarjeta) {this.numTarjeta = numTarjeta;}
    public void setidCurso(Long idCurso) {this.idCurso = idCurso;}
    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = (fechaCancelacion != null) ? fechaCancelacion.withNano(0) : null;}

    /////////////////////GETTERS/////////////////////
    public Long getidInscripcion() {return idInscripcion;}
    public String getEmailUser() {return emailUser;}
    public LocalDateTime getFechaHoraCreacion() {return fechaHoraCreacion;}
    public String getNumTarjeta() {return numTarjeta;}
    public Long getIdCurso() {return idCurso;}
    public LocalDateTime getFechaCancelacion() {return fechaCancelacion;}

    /////////////////////EQUALS & HASHCODE/////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscripcion other = (Inscripcion) o;
        if ((idInscripcion == null && other.idInscripcion != null) || (!Objects.equals(idInscripcion, other.idInscripcion))) return false;
        if ((emailUser == null && other.emailUser != null) || (!Objects.equals(emailUser, other.emailUser))) return false;
        if ((numTarjeta == null && other.numTarjeta != null) || (!Objects.equals(numTarjeta, other.numTarjeta))) return false;
        if ((fechaHoraCreacion == null && other.fechaHoraCreacion != null) || (!Objects.equals(fechaHoraCreacion, other.fechaHoraCreacion))) return false;
        if ((fechaCancelacion == null && other.fechaCancelacion != null) || (!Objects.equals(fechaCancelacion, other.fechaCancelacion))) return false;
        return ((idCurso == null && other.idCurso != null) || (!Objects.equals(idCurso, other.idCurso)));
    }

    @Override
    public int hashCode() {return Objects.hash(idInscripcion,emailUser,numTarjeta,fechaHoraCreacion,fechaCancelacion,idCurso);}
}
