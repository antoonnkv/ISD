package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyCanceledException extends RuntimeException {
    private Long idInscripcion;

    public ClientAlreadyCanceledException(Long idInscripcion) {
        super("La inscripcion con id=\""+idInscripcion+"\" ya ha sido cancelada");

        this.idInscripcion = idInscripcion;
    }

    public Long getIdInscripcion() {return idInscripcion;}

    public void setIdInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}
}
