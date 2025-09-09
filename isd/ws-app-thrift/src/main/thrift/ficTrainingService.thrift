namespace java es.udc.ws.app.thrift

struct ThriftCursoDto {
    1: i64 idCurso
    2: string nombre
    3: string ciudad
    4: i32 plazasDisponibles
    5: double precio
    6: i32 maxPlazas
    7: string fechaComienzo
}

struct ThriftInscripcionDto {
    1: i64 idInscripcion
    2: i64 idCurso
    3: string emailUser
    4: string numTarjeta
    5: string fechaHoraCreacion
    6: string fechaCancelacion
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}


//Excepciones propias

exception ThriftCursoEmpezadoException{
    1: i64 idCurso
    2: string FechaComienzo
}

exception ThriftNotEnougthPlazasDispException{
    1: i64 idCurso
    2: i32 plazasDisp
}


exception ThriftAlreadyCanceledException{
    1:i64 idInscripcion
    2:string fechaComienzo
}

exception ThriftInscriptionNoCancelableException{
    1:i64 idInscripcion
    2:string plazoCancelacion
}
exception ThriftIncorrectUserException{
    1:i64 idInscripcion
    2:string emailUser
}


service ThriftFicTrainingService {
    ThriftCursoDto addCurso (1: ThriftCursoDto curso) throws (1: ThriftInputValidationException e)
    list<ThriftCursoDto> findCursos (1: string ciudad) throws (1: ThriftInputValidationException e)
    i64 inscripcion_curso(1: i64 cursoId, 2: string emailUser, 3: string numTarjeta) throws(1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee, 3: ThriftCursoEmpezadoException eee, 4: ThriftNotEnougthPlazasDispException eeee)
    ThriftCursoDto buscarCurso(1: i64 cursoId) throws (1: ThriftInstanceNotFoundException e)
    void cancelarInscripcion(1:i64 idInscripcion, 2:string userEmail) throws (1: ThriftInputValidationException e,
                             2: ThriftInstanceNotFoundException ee, 3: ThriftAlreadyCanceledException ac,
                             4: ThriftIncorrectUserException iu, 5: ThriftInscriptionNoCancelableException nc)
    list<ThriftInscripcionDto> obt_todas_inscripciones(1:string emailUser) throws(1: ThriftInputValidationException e)


}
