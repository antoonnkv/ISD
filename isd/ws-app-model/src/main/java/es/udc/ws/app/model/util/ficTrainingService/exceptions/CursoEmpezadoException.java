package es.udc.ws.app.model.util.ficTrainingService.exceptions;

import java.time.LocalDateTime;

public class CursoEmpezadoException extends RuntimeException{
    private Long idCurso;
    private LocalDateTime fechaComienzo;
    public CursoEmpezadoException(Long idCurso, LocalDateTime fechaComienzo){
        super("Curse with id = \""+idCurso+"\" has been started = \""+
                fechaComienzo+"\")");

        this.idCurso = idCurso;
        this.fechaComienzo = fechaComienzo;
    }

    public LocalDateTime getFechaComienzo(){
        return fechaComienzo;
    }
    public void setFechaComienzo(LocalDateTime fechaComienzo){
        this.fechaComienzo = fechaComienzo;
    }
    public Long getIdCurso(){
        return idCurso;
    }
    public void setIdCurso(Long idCurso){
        this.idCurso = idCurso;
    }

}