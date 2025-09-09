package es.udc.ws.app.client.ui;

import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.client.service.ClientFicTrainingService;
import es.udc.ws.app.client.service.ClientFicTrainingServiceFactory;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public class AppServiceClient {
    public static void main(String[] args) {
        if(args.length == 0) printUsageAndExit();
        ClientFicTrainingService clientFicTrainingService = ClientFicTrainingServiceFactory.getService();

        //"[añadir] AppServiceClient -a <NombreCurso> <CiudadCurso> <Precio> <MaxPlazas> <DataComienzo>"
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args,6,new int[]{3,4});

            try {
                String nombre = args[1].trim();
                String ciudad = args[2].trim();
                float precio = Float.parseFloat(args[3]);
                int maxPlazas  = Integer.parseInt(args[4]);
                LocalDateTime fechaComienzo = LocalDateTime.parse(args[5]);
                Long cursoId = clientFicTrainingService.addCurso(new ClientCursoDto(null,nombre,ciudad,precio,fechaComienzo,maxPlazas));

                System.out.println("Curso con id = "+cursoId+" creado correctamente");

            } catch(NumberFormatException | InputValidationException ex){
                ex.printStackTrace(System.err);
            } catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }


        //[buscar_ciudad] AppServiceClient -findCourses <ciudad>
        if("-findCourses".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});
            try {
                List<ClientCursoDto> cursos = clientFicTrainingService.findCursos(args[1]);
                System.out.println("Encontrado(s) " + cursos.size() + " curso(s) en '" + args[1] + "'");
                for (ClientCursoDto cursoDto : cursos) System.out.println(cursoDto.toString());
            } catch (Exception ex) { ex.printStackTrace(System.err);}
        }

        //[buscar_id] AppServiceClient -findCourse <idCurso>
        if("-findCourse".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});
            try {
                ClientCursoDto curso = clientFicTrainingService.findCursoById(Long.parseLong(args[1]));
                System.out.println("Encontrado el curso con id '" + args[1] + "'");
                System.out.println(curso.toString());
            } catch (Exception ex) {ex.printStackTrace(System.err);}
        }

        //[inscribir] AppServiceClient -inscribe <userEmail> <courseId> <creditCardNumber>
        if("-inscribe".equalsIgnoreCase(args[0])){

            validateArgs(args,4,new int[] {2});
            long idInscripcion;
            try{
                idInscripcion = clientFicTrainingService.inscripcion_curso(args[1],args[3], Long.parseLong(args[2]));
                System.out.println("Incripción con id "+idInscripcion+" en curso con id "+args[2]+" se ha realizado correctamente");

            } catch (Exception e){
                e.printStackTrace(System.err);
            }
        }

        //[cancelar] AppServiceClient -cancel <idInscripcion> <idCurso>
        if("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {1});

            try{
                clientFicTrainingService.cancelarInscripcion(Long.valueOf(args[1]),args[2]);
                System.out.println("La inscripcion con id "+args[1]+" del usuario "+args[2]+" fue cancelada correctamente.");

            }catch(InputValidationException | InstanceNotFoundException | NumberFormatException |
                   ClientInscriptionNoCancelableException | ClientAlreadyCanceledException | ClientIncorrectUserException ex){
                ex.printStackTrace(System.err);
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }

        //[obtener] AppServiceClient -findInscriptions <emailUser>
        if("-findInscriptions".equalsIgnoreCase(args[0])){

            validateArgs(args,2,new int[] {});
            try {
                List<ClientInscripcionDto> inscripciones = clientFicTrainingService.obt_todas_inscripciones(args[1]);
                System.out.println("Encontradas "+inscripciones.size()+
                        " inscripcion(es) asociadas a usuari@ '"+ args[1]+"'");
                for(int i = 0; i < inscripciones.size();i++){
                    ClientInscripcionDto inscripcionDto = inscripciones.get(i);
                    System.out.println(inscripcionDto.toString());

                }

            }catch (Exception e){
                e.printStackTrace(System.err);
            }

        }



    }
    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) printUsageAndExit();
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {Double.parseDouble(args[position]);}
            catch(NumberFormatException n) {printUsageAndExit();}
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage(){
        System.err.println("Usage:\n" +
                "[añadir] AppServiceClient -a <NombreCurso> <CiudadCurso> <Precio> <MaxPlazas> <DataComienzo>\n"+
                "[buscar_ciudad] AppServiceClient -findCourses <ciudad>\n"+
                "[buscar_id] AppServiceClient -findCourse <idCurso>\n"+
                "[inscribir] AppServiceClient -inscribe <emailUser> <tarjeta> <idCurso>\n"+
                "[cancelar] AppServiceClient -cancel <idInscripcion> <idCurso>\n"+
                "[obtener] AppServiceClient -findInscriptions <emailUser>"
        );
    }

}

