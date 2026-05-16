package ec.edu.monster.controlador;

import ec.edu.monster.servicio.EurekaService;
import ec.edu.monster.ws.Movimiento;
import ec.edu.monster.ws.OperacionCuentaResponse;
import java.util.List;

public class CliCon_Controlador {
    
    public List<Movimiento> traerMovimientos(String cuenta){
        EurekaService service = new EurekaService();
        return service.traerMovimientos(cuenta);
    }
    
    public OperacionCuentaResponse regDeposito(String cuenta, double importe){
        EurekaService service = new EurekaService();
        return service.regDeposito(cuenta, importe);
    }
    
    public OperacionCuentaResponse regRetiro(String cuenta, double importe){
        EurekaService service = new EurekaService();
        return service.regRetiro(cuenta, importe);
    }
    
    public OperacionCuentaResponse regTransferencia(String cuentaOrigen, String cuentaDestino, double importe){
        EurekaService service = new EurekaService();
        return service.regTransferencia(cuentaOrigen, cuentaDestino, importe);
    }
}
