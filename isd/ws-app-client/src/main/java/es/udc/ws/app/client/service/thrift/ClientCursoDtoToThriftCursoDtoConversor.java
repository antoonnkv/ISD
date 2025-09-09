package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.thrift.ThriftCursoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientCursoDtoToThriftCursoDtoConversor {
    public static List<ClientCursoDto> toClientCursoDtos(List<ThriftCursoDto> triftCursoDtos) {
        List<ClientCursoDto> clientCursoDtos = new ArrayList<>(triftCursoDtos.size());
        for (ThriftCursoDto thrCursoDto : triftCursoDtos) clientCursoDtos.add(toClientCursoDto(thrCursoDto));
        return clientCursoDtos;
    }

    public static ClientCursoDto toClientCursoDto(ThriftCursoDto thriftCursoDto) {
        return new ClientCursoDto(thriftCursoDto.getIdCurso(), thriftCursoDto.getNombre(), thriftCursoDto.getCiudad(),
                (float)thriftCursoDto.getPrecio(), LocalDateTime.parse(thriftCursoDto.getFechaComienzo()), thriftCursoDto.getMaxPlazas());
    }

    public static ThriftCursoDto toThriftCursoDto(ClientCursoDto clientCursoDto) {
        Long idCurso = clientCursoDto.getIdCurso();
        return new ThriftCursoDto(
                idCurso == null ? -1 : idCurso.longValue(), clientCursoDto.getNombre(), clientCursoDto.getCiudad(),
                clientCursoDto.getPlazasReservadas(), clientCursoDto.getPrecio(), clientCursoDto.getMaxPlazas(),
                clientCursoDto.getFechaComienzo().toString());
    }
}
