package ec.edu.monster.servicio;

import ec.edu.monster.ws.OperacionCuentaResponse;
import java.util.List;

public class EurekaService {

    public OperacionCuentaResponse regDeposito(String cuenta, double importe) {
        ec.edu.monster.ws.WSEureka_Service service = new ec.edu.monster.ws.WSEureka_Service();
        ec.edu.monster.ws.WSEureka port = service.getWSEurekaPort();
        return port.regDeposito(cuenta, importe); // ahora devuelve OperacionCuentaResponse
    }

    public List<ec.edu.monster.ws.Movimiento> traerMovimientos(String cuenta) {
        ec.edu.monster.ws.WSEureka_Service service = new ec.edu.monster.ws.WSEureka_Service();
        ec.edu.monster.ws.WSEureka port = service.getWSEurekaPort();
        return port.traerMovimientos(cuenta);
    }

    public OperacionCuentaResponse regRetiro(String cuenta, double importe) {
        ec.edu.monster.ws.WSEureka_Service service = new ec.edu.monster.ws.WSEureka_Service();
        ec.edu.monster.ws.WSEureka port = service.getWSEurekaPort();
        return port.regRetiro(cuenta, importe);
    }

    public OperacionCuentaResponse regTransferencia(String cuentaOrigen, String cuentaDestino, double importe) {
        ec.edu.monster.ws.WSEureka_Service service = new ec.edu.monster.ws.WSEureka_Service();
        ec.edu.monster.ws.WSEureka port = service.getWSEurekaPort();
        return port.regTransferencia(cuentaOrigen, cuentaDestino, importe);
    }
}
