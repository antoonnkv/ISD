package es.udc.ws.app.client.service.thrift;


import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.ClientFicTrainingService;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftClientFicTrainingService implements ClientFicTrainingService {
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientFicTrainingService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public Long addCurso(ClientCursoDto curso) throws InputValidationException {

        ThriftFicTrainingService.Client client = getCliente();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return client.addCurso(ClientCursoDtoToThriftCursoDtoConversor.toThriftCursoDto(curso)).getIdCurso();
        }
        catch (ThriftInputValidationException e) {throw new InputValidationException(e.getMessage());}
        catch (Exception e) {throw new RuntimeException(e);}
    }

    @Override
    public List<ClientCursoDto> findCursos(String ciudad) throws InputValidationException {
        ThriftFicTrainingService.Client client = getCliente();
        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            List<ThriftCursoDto> thriftCursos = client.findCursos(ciudad);
            return ClientCursoDtoToThriftCursoDtoConversor.toClientCursoDtos(thriftCursos);
        } catch (ThriftInputValidationException e) {throw new InputValidationException(e.getMessage());} 
        catch (Exception e) {throw new RuntimeException(e);}
    }


    @Override
    public ClientCursoDto findCursoById(Long idCurso) throws InstanceNotFoundException {

        ThriftFicTrainingService.Client client = getCliente();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientCursoDtoToThriftCursoDtoConversor.toClientCursoDto(client.buscarCurso(idCurso));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public Long inscripcion_curso( String emailUser,String numTarjeta, Long cursoId) throws
            InstanceNotFoundException, ClientCursoEmpezadoException, ClientNotEnougthPlazasDispException {
        ThriftFicTrainingService.Client client = getCliente();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.inscripcion_curso(cursoId, emailUser, numTarjeta);

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftCursoEmpezadoException e) {
            throw new ClientCursoEmpezadoException(e.getIdCurso(), LocalDateTime.parse(e.getFechaComienzo()));
        } catch (ThriftNotEnougthPlazasDispException e) {
            throw new ClientNotEnougthPlazasDispException(e.getIdCurso(), e.getPlazasDisp());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void cancelarInscripcion(Long idInscripcion, String userEmail) throws InputValidationException, InstanceNotFoundException,
            ClientAlreadyCanceledException, ClientIncorrectUserException, ClientInscriptionNoCancelableException {
        ThriftFicTrainingService.Client cliente = getCliente();

        try(TTransport transport = cliente.getInputProtocol().getTransport()){
            transport.open();
            cliente.cancelarInscripcion(idInscripcion, userEmail);


        }catch (ThriftInputValidationException inp_e){
            throw new InputValidationException(inp_e.getMessage());
        }
        catch (ThriftInstanceNotFoundException ins_e){
            throw new InstanceNotFoundException(ins_e.getInstanceId(),ins_e.getInstanceType());
        }
        catch (ThriftAlreadyCanceledException ac_e){
            throw new ClientAlreadyCanceledException(ac_e.getIdInscripcion());
        }catch (ThriftIncorrectUserException iu_e){
            throw new ClientIncorrectUserException(iu_e.getIdInscripcion(), iu_e.getEmailUser());
        }catch (ThriftInscriptionNoCancelableException nc_e){
            throw new ClientInscriptionNoCancelableException(nc_e.getIdInscripcion());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<ClientInscripcionDto> obt_todas_inscripciones(String emailUser) throws InputValidationException{
        ThriftFicTrainingService.Client cliente = getCliente();

        try(TTransport transport = cliente.getInputProtocol().getTransport()){

            transport.open();
            List<ThriftInscripcionDto> listaInscripcionThr = cliente.obt_todas_inscripciones(emailUser);

            return ClientInscripcionDtoToThriftInscripcionDtoConversor.toClientInscripcionDtos(listaInscripcionThr);
        }
        catch (ThriftInputValidationException inp_e){
            throw new InputValidationException(inp_e.getMessage());
        }catch (Exception except){

            except.printStackTrace(System.err);
            throw new RuntimeException(except);
        }

    }

    private ThriftFicTrainingService.Client getCliente() {
        try {
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);
            return new ThriftFicTrainingService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }

}