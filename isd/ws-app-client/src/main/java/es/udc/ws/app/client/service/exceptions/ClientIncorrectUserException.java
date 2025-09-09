package es.udc.ws.app.client.service.exceptions;

public class ClientIncorrectUserException extends RuntimeException {
    private Long idInscripcion;
    private String userEmail;

    public ClientIncorrectUserException(Long idInscripcion, String userEmail) {
        super("Inscripcion con id =\"" + idInscripcion + "\" no pertenece al usuario =\"" + userEmail + "\"");

        this.idInscripcion = idInscripcion;
        this.userEmail = userEmail;
    }

    public Long getIdInscripcion() {return idInscripcion;}

    public String getUserEmail() {return userEmail;}

    public void setIdInscripcion(Long idInscripcion) {this.idInscripcion = idInscripcion;}

    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}

}
