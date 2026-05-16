package ec.edu.monster.config;

public class AppConfig {
  /** Pon aquí la URL real del WSDL expuesto por tu servidor */
  public static final String WSDL_URL = "http://localhost:8080/WS_EurekaBank_Server/WSEureka?wsdl";

  /** Namespace por defecto para @WebService en paquete ec.edu.monster.ws */
  public static final String SERVICE_NS = "http://ws.monster.edu.ec/";               // <-- AJUSTAR si tu WSDL dice otro

  /** OJO: En tu @WebService(serviceName = "WSEureka") => el nombre del service es "WSEureka" */
  public static final String SERVICE_NAME = "WSEureka";                               // <-- AJUSTAR si tu WSDL dice otro
  
  public static final boolean DEBUG = true;

}
