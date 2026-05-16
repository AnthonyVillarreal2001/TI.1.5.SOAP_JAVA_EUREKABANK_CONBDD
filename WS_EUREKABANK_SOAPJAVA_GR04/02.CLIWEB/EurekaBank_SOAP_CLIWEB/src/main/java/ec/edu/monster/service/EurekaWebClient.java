package ec.edu.monster.service;

import ec.edu.monster.ws.Movimiento;
import ec.edu.monster.ws.OperacionCuentaResponse;
import ec.edu.monster.ws.WSEureka;
import ec.edu.monster.ws.WSEureka_Service;
import java.util.List;

public class EurekaWebClient {

    private static WSEureka getPort() {
        WSEureka_Service service = new WSEureka_Service();
        return service.getWSEurekaPort();
    }

    public static String validarIngreso(String usuario, String password) {
        return getPort().validarIngreso(usuario, password);
    }

    public static List<Movimiento> traerMovimientos(String cuenta) {
        return getPort().traerMovimientos(cuenta);
    }

    public static OperacionCuentaResponse regDeposito(String cuenta, double importe) {
        return getPort().regDeposito(cuenta, importe);
    }

    public static OperacionCuentaResponse regRetiro(String cuenta, double importe) {
        return getPort().regRetiro(cuenta, importe);
    }

    public static OperacionCuentaResponse regTransferencia(String cuentaOrigen, String cuentaDestino, double importe) {
        return getPort().regTransferencia(cuentaOrigen, cuentaDestino, importe);
    }
}
