package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientInscriptionNoCancelableException extends RuntimeException {
  private Long idInscripcion;
  private LocalDateTime plazoCancelacion;

  public ClientInscriptionNoCancelableException(Long idInscripcion) {

    super("La inscripcion con id =\"" + idInscripcion + "\" no puede ser cancelada, faltan menos de 7 dias para el comienzo del curso\"");
    this.idInscripcion = idInscripcion;

  }


  public Long getIdInscripcion() {return idInscripcion;}


  public void setIdInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}



}
