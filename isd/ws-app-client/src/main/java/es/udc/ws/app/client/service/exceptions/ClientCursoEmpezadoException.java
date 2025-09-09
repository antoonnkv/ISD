package es.udc.ws.app.client.service.exceptions;

import java.security.spec.ECFieldF2m;
import java.time.LocalDateTime;

public class ClientCursoEmpezadoException extends Exception {
    private Long idCurso;
    private LocalDateTime fechaComienzo;
    public ClientCursoEmpezadoException(Long idCurso, LocalDateTime fechaComienzo){
        super("Curse with id = \""+idCurso+"\" has been started = \""+
                fechaComienzo+"\")");
        this.idCurso = idCurso;
        this.fechaComienzo = fechaComienzo;
    }

    public LocalDateTime getFechaComienzo(){
        return fechaComienzo;
    }
    public void setFechaComienzo(LocalDateTime fechaComienzo){this.fechaComienzo = fechaComienzo;}

    public Long getIdCurso(){
        return idCurso;
    }
    public void setIdCurso(Long idCurso){this.idCurso = idCurso;}
}
