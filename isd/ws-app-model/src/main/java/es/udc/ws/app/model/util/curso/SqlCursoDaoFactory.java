package es.udc.ws.app.model.util.curso;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlCursoDaoFactory {

    private final static String NOMBRE_DE_CLASE = "SqlCursoDaoFactory.className";
    private static es.udc.ws.app.model.util.curso.SqlCursoDao daoCurso = null;

    private  SqlCursoDaoFactory(){
    }
    private static es.udc.ws.app.model.util.curso.SqlCursoDao getInstance(){
        try{
            String NombreClaseDao = ConfigurationParametersManager.getParameter(NOMBRE_DE_CLASE);
            Class claseDao = Class.forName(NombreClaseDao);
            return (es.udc.ws.app.model.util.curso.SqlCursoDao) claseDao.getDeclaredConstructor().newInstance();
        }catch (Exception except){
            throw new RuntimeException(except);
        }
    }
    public synchronized static es.udc.ws.app.model.util.curso.SqlCursoDao getDao(){

        if(daoCurso == null){
            daoCurso = getInstance();
        }
        return daoCurso;
    }

}
