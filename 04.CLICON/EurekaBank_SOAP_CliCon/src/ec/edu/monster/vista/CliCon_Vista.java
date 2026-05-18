/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.CliCon_Controlador;
import ec.edu.monster.ws.Movimiento;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author JOIS
 */
public class CliCon_Vista {

    private static final String USUARIO = "MONSTER";
    private static final String PASS = "MONSTER9";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        CliCon_Controlador controlador = new CliCon_Controlador();
        Scanner scanner = new Scanner(System.in);

        int opcion;

        System.out.println("╔═════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        E U R E K A B A N K                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════╝ ");

        System.out.println("\033[34m\n--------------------\033[0m");
        System.out.println("\033[34m     BIENVENIDO     \033[0m");
        System.out.println("\033[34m--------------------\033[0m");

        // Validación de usuario
        System.out.print("\nIngrese su usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        if (USUARIO.equals(usuario) && PASS.equals(password)) {
            System.out.println("\033[32m\nAcceso exitoso\033[0m");

            do {
                // Mostrar el menú
                System.out.println("\033[34m\n==========================\033[0m");
                System.out.println("\033[34m       MENÚ PRINCIPAL       \033[0m");
                System.out.println("\033[34m==========================\033[0m\n");
                System.out.println("1. Consultar movimientos");
                System.out.println("2. Realizar depósito");
                System.out.println("3. Realizar retiro");
                System.out.println("4. Realizar transferencia");
                System.out.println("5. Salir");
                System.out.print("\033[34m\nSeleccione una opción: \033[0m");

                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1:
                        // Consultar movimientos
                        System.out.println("\033[35m\n============================\033[0m");
                        System.out.println("\033[35m         MOVIMIENTOS        \033[0m");
                        System.out.println("\033[35m============================\033[0m");
                        System.out.print("\n\033[35mIngrese el número de cuenta: \033[0m");
                        String cuentaConsulta = scanner.nextLine();

                        List<Movimiento> movimientos = controlador.traerMovimientos(cuentaConsulta);
                        if (movimientos.isEmpty()) {
                            System.out.println("\033[31mNo se encontraron movimientos para la cuenta ingresada.\033[0m");
                        } else {

                            // Encabezado de la tabla
                            System.out.println("\n===========================================================================================================================");

                            System.out.printf("%-20s %-20s %-25s %-15s %-20s %-20s%n",
                                    "     Cuenta     ", " Nro Movimiento ", "          Fecha        ", "              Tipo   ", "          Acción    ", "    Importe     ");
                            System.out.println("===========================================================================================================================");

                            // Filas de datos
                            for (Movimiento mov : movimientos) {
                                System.out.printf("%-20s %-20s %-30s %-25s %-15s %-10.2f%n",
                                        mov.getCuenta(),
                                        mov.getNromov(),
                                        mov.getFecha(),
                                        mov.getTipo(),
                                        mov.getAccion(),
                                        mov.getImporte());
                            }
                        }
                        break;

                    case 2:
                        // Realizar depósito
                        System.out.println("\033[36m\n============================\033[0m");
                        System.out.println("\033[36m         DEPÓSITO          \033[0m");
                        System.out.println("\033[36m============================\033[0m");
                        System.out.print("\033[36m\nIngrese el número de cuenta: \033[0m");
                        String cuentaDeposito = scanner.nextLine();
                        System.out.print("\033[36mIngrese el importe a depositar: \033[0m");
                        double importe = scanner.nextDouble();

                        var resultadoDep = controlador.regDeposito(cuentaDeposito, importe);
                        if (resultadoDep.getEstado() == 1) {
                            System.out.println("\033[32m\nDepósito realizado exitosamente.\033[0m");
                            System.out.printf("\033[36mNuevo saldo de la cuenta %s: %.2f\033[0m%n",
                                    cuentaDeposito, resultadoDep.getSaldo());
                        } else {
                            System.out.println("\033[31m\nError al realizar el depósito. Verifique los datos e intente nuevamente.\033[0m");
                        }

                        break;

                    case 3:
                        // Realizar retiro
                        System.out.println("\033[36m\n============================\033[0m");
                        System.out.println("\033[36m         RETIRO          \033[0m");
                        System.out.println("\033[36m============================\033[0m");
                        System.out.print("\033[36m\nIngrese el número de cuenta: \033[0m");
                        String cuentaRetiro = scanner.nextLine();
                        System.out.print("\033[36mIngrese el importe a retirar: \033[0m");
                        double importeRetiro = scanner.nextDouble();

                        var resultadoRet = controlador.regRetiro(cuentaRetiro, importeRetiro);
                        if (resultadoRet.getEstado() == 1) {
                            System.out.println("\033[32m\nRetiro realizado exitosamente.\033[0m");
                            System.out.printf("\033[36mNuevo saldo de la cuenta %s: %.2f\033[0m%n",
                                    cuentaRetiro, resultadoRet.getSaldo());
                        } else {
                            System.out.println("\033[31m\nError al realizar el retiro.\033[0m");
                        }

                        break;

                    case 4:
                        // Realizar transferencia
                        System.out.println("\033[36m\n============================\033[0m");
                        System.out.println("\033[36m       TRANSFERENCIA        \033[0m");
                        System.out.println("\033[36m============================\033[0m");

                        System.out.print("\033[36m\nIngrese el número de cuenta origen: \033[0m");
                        String cuentaOrigen = scanner.nextLine();
                        System.out.print("\033[36mIngrese el número de cuenta destino: \033[0m");
                        String cuentaDestino = scanner.nextLine();
                        System.out.print("\033[36mIngrese el importe a transferir: \033[0m");
                        double importeTransferencia = scanner.nextDouble();

                        var resultadoTrans = controlador.regTransferencia(cuentaOrigen, cuentaDestino, importeTransferencia);
                        if (resultadoTrans.getEstado() == 1) {
                            System.out.println("\033[32m\nTransferencia realizada exitosamente.\033[0m");
                            System.out.printf("\033[36mNuevo saldo de la cuenta origen %s: %.2f\033[0m%n",
                                    cuentaOrigen, resultadoTrans.getSaldo());
                        } else {
                            System.out.println("\033[31m\nError al realizar la transferencia. Verifique los datos e intente nuevamente.\033[0m");
                        }

                        break;

                    case 5:
                        // Salir
                        System.out.println("\nSaliendo del sistema. ¡Gracias!");
                        break;

                    default:
                        System.out.println("\033[31mOpción no válida. Intent e nuevamente.\033[0m");
                }

                System.out.println(); // Salto de línea para mejorar legibilidad
            } while (opcion != 5);
        } else {
            System.out.println("\033[31mAcceso denegado\033[0m");
        }

        scanner.close();
    }

}
