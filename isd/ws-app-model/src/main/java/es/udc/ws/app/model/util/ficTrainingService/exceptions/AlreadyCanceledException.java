package es.udc.ws.app.model.util.ficTrainingService.exceptions;

public class AlreadyCanceledException extends RuntimeException {

    private Long idInscripcion;

    public AlreadyCanceledException(Long idInscripcion) {
        super("La inscripcion con id=\""+idInscripcion+"\" ya ha sido cancelada");

        this.idInscripcion = idInscripcion;
    }

    public Long getIdInscripcion() {return idInscripcion;}

    public void setIdInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}
}
