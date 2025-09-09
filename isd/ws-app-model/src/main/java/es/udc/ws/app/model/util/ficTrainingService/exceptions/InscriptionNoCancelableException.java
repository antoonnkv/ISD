package es.udc.ws.app.model.util.ficTrainingService.exceptions;

import java.time.LocalDateTime;

public class InscriptionNoCancelableException extends Exception {

  private Long idInscripcion;
  private LocalDateTime plazoCancelacion;

  public InscriptionNoCancelableException(Long idInscripcion) {

    super("La inscripcion con id =\"" + idInscripcion + "\" no puede ser cancelada, faltan menos de 7 dias para el comienzo del curso\"");
    this.idInscripcion = idInscripcion;

  }


  public Long getIdInscripcion() {return idInscripcion;}


  public void setIdInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}

  public LocalDateTime getPlazoCancelacion() {
    return plazoCancelacion;
  }

  public void setPlazoCancelacion(LocalDateTime plazoCancelacion) {
    this.plazoCancelacion = plazoCancelacion;
  }
}
