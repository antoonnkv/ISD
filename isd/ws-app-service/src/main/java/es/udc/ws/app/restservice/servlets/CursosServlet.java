package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.util.curso.Curso;
import es.udc.ws.app.model.util.ficTrainingService.FicTrainingServiceFactory;
import es.udc.ws.app.restservice.dto.CursoToRestCursoDtoConversor;
import es.udc.ws.app.restservice.dto.RestCursoDto;
import es.udc.ws.app.restservice.json.JsonToRestCursoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;

public class CursosServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req,HttpServletResponse resp) throws IOException,
            InputValidationException {

        ServletUtils.checkEmptyPath(req);
        RestCursoDto cursoDto = JsonToRestCursoDtoConversor.toRestCursoDto(req.getInputStream());
        Curso curso = CursoToRestCursoDtoConversor.toCurso(cursoDto);

        curso = FicTrainingServiceFactory.getService().addCurso(curso);

        cursoDto = CursoToRestCursoDtoConversor.toRestCursoDto(curso);
        String cursoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + curso.getIdCurso();
        Map<String,String> headers = new HashMap<>(1);
        headers.put("Location",cursoURL);
        ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_CREATED,
                JsonToRestCursoDtoConversor.toObjectNode(cursoDto),headers);
    }
    
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InstanceNotFoundException, InputValidationException {
    	String keyWords;
   
       if(req.getPathInfo() == null || Objects.equals(req.getPathInfo(), "/")) {
           ServletUtils.checkEmptyPath(req);
           keyWords = req.getParameter("ciudad");
           List<Curso> cursos = FicTrainingServiceFactory.getService().buscarCursoLocFecha(keyWords, LocalDateTime.now());
           List<RestCursoDto> cursosDtos = CursoToRestCursoDtoConversor.toRestCursoDtoList(cursos);
           ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestCursoDtoConversor.toArrayNode(cursosDtos), null);
       } else {
           Long id = ServletUtils.getIdFromPath(req,"id");
           Curso curso = FicTrainingServiceFactory.getService().buscarCurso(id);
           RestCursoDto cursosDtos = CursoToRestCursoDtoConversor.toRestCursoDto(curso);
           ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestCursoDtoConversor.toObjectNode(cursosDtos), null);
       }
    }

}
