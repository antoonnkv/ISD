package es.udc.ws.app.model.util.inscripcion;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlInscripcionDaoFactory {

    private final static String NOMBRE_DE_CLASE = "SqlInscripcionDaoFactory.className";
    private static es.udc.ws.app.model.util.inscripcion.SqlInscripcionDao daoInscripcion = null;

    private  SqlInscripcionDaoFactory(){}

    private static es.udc.ws.app.model.util.inscripcion.SqlInscripcionDao getInstance(){
        try{
            String NombreClaseDao = ConfigurationParametersManager.getParameter(NOMBRE_DE_CLASE);
            Class claseDao = Class.forName(NombreClaseDao);
            return (es.udc.ws.app.model.util.inscripcion.SqlInscripcionDao) claseDao.getDeclaredConstructor().newInstance();
        }catch (Exception except){ throw new RuntimeException(except);}
    }

    public synchronized static es.udc.ws.app.model.util.inscripcion.SqlInscripcionDao getDao(){
        if(daoInscripcion == null) daoInscripcion = getInstance();
        return daoInscripcion;
    }
}
