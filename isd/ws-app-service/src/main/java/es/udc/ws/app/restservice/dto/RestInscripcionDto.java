package es.udc.ws.app.restservice.dto;
import java.time.LocalDateTime;
import java.util.Objects;

public class RestInscripcionDto {

    /////////////////////ATRIBUTOS/////////////////////
    private Long idInscripcion;
    private String emailUser;
    private String numTarjeta;
    private String fechaHoraCreacion;
    private String fechaCancelacion;
    private Long idCurso;

    /////////////////////CONSTRUCTORES/////////////////////
    public RestInscripcionDto() {
    }

    public RestInscripcionDto(Long idInscripcion, String emailUser, String numTarjeta, String fechaHoraCreacion,String fechaCancelacion, Long idCurso) {
        this.idInscripcion = idInscripcion;
        this.emailUser = emailUser;
        this.numTarjeta = numTarjeta;
        this.fechaCancelacion = fechaCancelacion;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.idCurso = idCurso;
    }

    /////////////////////SETTERS/////////////////////
    public void setidInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}
    public void setEmailUser(String emailUser) {this.emailUser = emailUser;}
    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;}
    public void setNumTarjeta(String numTarjeta) {this.numTarjeta = numTarjeta;}
    public void setidCurso(Long idCurso) {this.idCurso = idCurso;}
    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;}

    /////////////////////GETTERS/////////////////////
    public Long getidInscripcion() {return idInscripcion;}
    public String getEmailUser() {return emailUser;}
    public String getFechaHoraCreacion() {return fechaHoraCreacion;}
    public String getNumTarjeta() {return numTarjeta;}
    public Long getIdCurso() {return idCurso;}
    public String getFechaCancelacion() {return fechaCancelacion;}

    @Override
    public String toString() {
        return "InscripcionDto [inscripcionID= " + idInscripcion + ", userEmail= " + emailUser + ", tarjeta= " + numTarjeta + ", fechaCreacion= "
                + fechaHoraCreacion + ", fechaCancelacion= " + fechaCancelacion + ", idCurso= " + idCurso + "]";
    }
}

