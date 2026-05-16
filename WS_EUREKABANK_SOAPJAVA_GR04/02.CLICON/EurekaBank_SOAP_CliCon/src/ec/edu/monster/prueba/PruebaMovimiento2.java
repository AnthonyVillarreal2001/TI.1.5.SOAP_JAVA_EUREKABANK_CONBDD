/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.prueba;

import ec.edu.monster.servicio.EurekaService;

/**
 *
 * @author JOIS
 */
public class PruebaMovimiento2 {
    public static void main(String[] args) {
        try {
            //dato de la prueba
            String cuenta = "00100001";
            double importe = 150;
            
            //proceso
            EurekaService service =new EurekaService();
            service.regDeposito(cuenta, importe);
            System.out.println("Proceso ok");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
