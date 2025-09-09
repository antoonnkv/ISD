package es.udc.ws.app.thriftservice;

import  es.udc.ws.app.thrift.ThriftFicTrainingService;
import es.udc.ws.util.servlet.ThriftHttpServletTemplate;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

public class ThriftFicTrainingServiceServlet extends ThriftHttpServletTemplate {
    public ThriftFicTrainingServiceServlet() {super(createProcessor(), createProtocolFactory());}

    private static TProcessor createProcessor() {
        return new ThriftFicTrainingService.Processor<ThriftFicTrainingService.Iface>(new ThriftFicTrainingServiceImpl());
    }

    private static TProtocolFactory createProtocolFactory() {return new TBinaryProtocol.Factory();}

}
