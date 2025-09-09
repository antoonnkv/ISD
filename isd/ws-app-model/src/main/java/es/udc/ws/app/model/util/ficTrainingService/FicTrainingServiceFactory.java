package es.udc.ws.app.model.util.ficTrainingService;

import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.app.model.util.ficTrainingService.FicTrainingService;

public class FicTrainingServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "FicTrainingServiceFactory.className";
    private static FicTrainingService service = null;

    private FicTrainingServiceFactory(){
    }

    private static FicTrainingService getInstance(){
        try{
            String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (FicTrainingService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public synchronized static FicTrainingService getService(){

        if(service == null){
            service = getInstance();
        }
        return service;
    }
}
