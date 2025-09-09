package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;
import java.lang.reflect.InvocationTargetException;

public class ClientFicTrainingServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientFicTrainingServiceFactory.className";
    private static Class<ClientFicTrainingService> serviceClass = null;

    private ClientFicTrainingServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientFicTrainingService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientFicTrainingService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientFicTrainingService getService() {

        try {
            return (ClientFicTrainingService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}

