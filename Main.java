/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package empleadomanager;

/**
 *
 * @author andru
 */

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmpleadoManager manager = new EmpleadoManager();

        while (true) {
            System.out.println("\n--- Menú de Gestión de Empleados ---");
            System.out.println("1. Agregar empleado");
            System.out.println("2. Listar empleados");
            System.out.println("3. Despedir empleado");
            System.out.println("4. Registrar venta a empleado");
            System.out.println("5. Pagar a empleado");
            System.out.println("6. Imprimir datos de empleado");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1: // Agregar empleado
                        System.out.print("Ingrese el nombre del empleado: ");
                        String name = scanner.nextLine();
                        System.out.print("Ingrese el salario del empleado: ");
                        double salary = scanner.nextDouble();
                        manager.addEmployee(name, salary);
                        System.out.println("Empleado agregado exitosamente.");
                        break;

                    case 2: // Listar empleados
                        System.out.println("Lista de empleados:");
                        manager.employeeList();
                        break;

                    case 3: // Despedir empleado
                        System.out.print("Ingrese el código del empleado a despedir: ");
                        int codeToFire = scanner.nextInt();
                        if (manager.fireEmployee(codeToFire)) {
                            System.out.println("Empleado despedido exitosamente.");
                        } else {
                            System.out.println("No se pudo despedir al empleado. Puede que ya esté despedido o no exista.");
                        }
                        break;

                    case 4: // Registrar venta a empleado
                        System.out.print("Ingrese el código del empleado: ");
                        int codeToAddSale = scanner.nextInt();
                        System.out.print("Ingrese el monto de la venta: ");
                        double saleAmount = scanner.nextDouble();
                        manager.addSaleToEmployee(codeToAddSale, saleAmount);
                        break;

                    case 5: // Pagar a empleado
                        System.out.print("Ingrese el código del empleado a pagar: ");
                        int codeToPay = scanner.nextInt();
                        manager.payEmployee(codeToPay);
                        break;

                    case 6: // Imprimir datos de empleado
                        System.out.print("Ingrese el código del empleado: ");
                        int codeToPrint = scanner.nextInt();
                        manager.printEmployee(codeToPrint);
                        break;

                    case 7: // Salir
                        System.out.println("Saliendo del programa...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}


