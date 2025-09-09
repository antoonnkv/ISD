package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientInscripcionDto {

    /////////////////////ATRIBUTOS/////////////////////
    private Long idInscripcion;
    private String emailUser;
    private String numTarjeta;
    private LocalDateTime fechaHoraCreacion;
    private LocalDateTime fechaCancelacion;
    private Long idCurso;

    /////////////////////CONSTRUCTORES/////////////////////
    public ClientInscripcionDto() {
    }

    public ClientInscripcionDto(Long idInscripcion, String emailUser, String numTarjeta, LocalDateTime fechaHoraCreacion,LocalDateTime fechaCancelacion, Long idCurso) {
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
    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;}
    public void setNumTarjeta(String numTarjeta) {this.numTarjeta = numTarjeta;}
    public void setidCurso(Long idCurso) {this.idCurso = idCurso;}
    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;}

    /////////////////////GETTERS/////////////////////
    public Long getidInscripcion() {return idInscripcion;}
    public String getEmailUser() {return emailUser;}
    public LocalDateTime getFechaHoraCreacion() {return fechaHoraCreacion;}
    public String getNumTarjeta() {return numTarjeta;}
    public Long getIdCurso() {return idCurso;}
    public LocalDateTime getFechaCancelacion() {return fechaCancelacion;}

    @Override
    public String toString() {
        String inscriocionDto ="Inscripcion [InscripcionID= " + idInscripcion + ", Email Usuario= " + emailUser + ", Tarjeta= " + numTarjeta + ", Fecha Creacion= "
                + fechaHoraCreacion;
        if(fechaCancelacion == null){
            inscriocionDto+= ", Fecha Cancelacion= No cancelada";
        }else{
            inscriocionDto+= ", Fecha Cancelacion= "+fechaCancelacion;
        }
        inscriocionDto+=  ", CursoID= " + idCurso + "]";

        return inscriocionDto;
    }
}


