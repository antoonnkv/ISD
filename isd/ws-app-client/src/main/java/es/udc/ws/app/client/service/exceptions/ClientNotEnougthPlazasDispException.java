package es.udc.ws.app.client.service.exceptions;

public class ClientNotEnougthPlazasDispException extends Exception{
    private Long idCurso;
    private int plazasDisponibles;

    public ClientNotEnougthPlazasDispException(Long idCurso, int plazasDisponibles ){
        super("Curse with id = \""+idCurso+"\" has = \""+ plazasDisponibles+"\" available places");
        this.idCurso = idCurso;
        this.plazasDisponibles = plazasDisponibles;
    }


    public int getPlazasDisponibles(){
        return plazasDisponibles;
    }
    public void setPlazasDisponibles(int plazasDisponibles){this.plazasDisponibles = plazasDisponibles;}

    public Long getIdCurso(){
        return idCurso;
    }
    public void setIdCurso(Long idCurso){this.idCurso = idCurso;}

}
