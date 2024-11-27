/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package empleadomanager;

/**
 *
 * @author andru
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    
    private RandomAccessFile rcods, remps;
    
    /*
        Formato Codigo.emp
        int code
        Formato Empleados.emp
        int code
        String name
        double salary
        long fecha Contratacion
        long fecha despido
    */
    
    public EmpleadoManager () {
        try {
            //1 - Asegurar que el folder company exista
            File mf = new File("company");
            mf.mkdir();
            //2 - Instanciar RAFs dentro de company
            rcods = new RandomAccessFile("company/codigos.emp", "rw");
            remps = new RandomAccessFile("company/empleado.emp", "rw");
            initCodes();
        } catch (IOException e) {
            System.out.println("Error"+e.getMessage());
        }
    }
    
    private void initCodes()throws IOException{
        if (rcods.length()==0) {
            //Puntero ->   0
            rcods.writeInt(1);
            //Puntero ->   4
        }
    }
    
    private int getCode() throws IOException{
        rcods.seek(0);
        //Puntero     ->       0
        int code=rcods.readInt();
        //Puntero     ->       4
        rcods.seek(0);
        rcods.writeInt(code+1);
        return code;
    }
    
    public void addEmployee(String name, double salary) throws IOException{
        //Asegurar que el puntero este en el final del archivo
        remps.seek(remps.length());
        int code = getCode();
        //P -> 0
        remps.writeInt(code);
        //P -> 4
        remps.writeUTF(name);//Ana 8
        //p -> 12
        remps.writeDouble(salary);
        //P -> 20
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        //P -> 28
        remps.writeLong(0);
        //P -> 36 EOF
        //Asegurar crear folder y archivos individuales
        createEmployeeFolders(code);
    }
    
    private String employeeFolder(int code){
        return "company/empleado"+code;
    }
    
    private void createEmployeeFolders(int code) throws IOException{
        //Crear folder empleado+code
        File empDIR = new File(employeeFolder(code));
        empDIR.mkdir();
    }
    
    private RandomAccessFile salesFileFor(int code)throws IOException{
        String DIRpadre = employeeFolder(code);
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        String path = DIRpadre+"/ventas"+yearActual+".emp";
        return new RandomAccessFile(path, "rw");
    }
    
    private void createYearSalesFileFor(int code)throws IOException{
        
        RandomAccessFile ryear = salesFileFor(code);
        if(ryear.length()==0){
            for (int mes = 0; mes < 12; mes++) {
                ryear.writeDouble(0);
                ryear.writeBoolean(false);
                
            }
            
        }
    }
    //Code - Name - Salary - Fecha Con.
    public void employeeList()throws IOException{
        remps.seek(0);
        //P-->  36 < 36 False
        while(remps.getFilePointer ()<= remps.length()){
            //P->0
            int code=remps.readInt();
            //P->4
            String name=remps.readUTF();
            //P->12
            double salary=remps.readDouble();
            //P-> 20
            Date dateH= new Date(remps.readLong());
            //P->28
            if(remps.readLong()==0){
                System.out.println("Codigo: "+code+"Nombre: "+name+
                        "Salario: Lps."+salary+"Contratado: "+dateH);  
            }
            //P->36
            
        }
        
    }
    
    private boolean isEmployeeActive(int code)throws IOException{
        remps.seek(0);
        while(remps.getFilePointer()<remps.length()){
            int codigo=remps.readInt();
            long pos=remps.getFilePointer();
            remps.readUTF();
            remps.skipBytes(16);
            if (remps.readLong()==0&&codigo==code) {
                remps.seek(pos);
                return true;
            }
        }
        return false;
    }
    public boolean fireEmployee(int code)throws IOException{
        if (isEmployeeActive(code)) {
            String name = remps.readUTF();
            remps.skipBytes(16);
            remps.writeLong(new Date().getTime());
            System.out.println("Despidiendo a: "+name);
            return true;
        }
        return false;
    }
    
    public void addSaleToEmployee(int code, double monto)throws IOException{
        if (isEmployeeActive(code)) {
            
        }
    }

   
    
    public class empleadoManager {
    // Mantener los métodos anteriores...

    public void addSaleToEmployee(int code, double monto) throws IOException {
        if (isEmployeeActive(code)) {
            // Obtener el archivo de ventas para el empleado
            RandomAccessFile salesFile = salesFileFor(code);
            Calendar cal = Calendar.getInstance();
            int mesActual = cal.get(Calendar.MONTH); // Enero = 0, Diciembre = 11

            // Posicionarse en el mes actual
            salesFile.seek(mesActual * 9); // Cada registro: 8 bytes (double) + 1 byte (boolean)
            double ventasMes = salesFile.readDouble();
            salesFile.seek(mesActual * 9); // Volver a la posición de escritura
            salesFile.writeDouble(ventasMes + monto);
            System.out.println("Venta añadida exitosamente para el empleado con código " + code + ".");
        } else {
            System.out.println("El empleado con código " + code + " no está activo.");
        }
    }

    void payEmployee(int code) throws IOException {
        if (!isEmployeeActive(code)) {
            System.out.println("El empleado con código " + code + " no está activo.");
            return;
        }

        // Obtener datos básicos del empleado
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            if (empCode == code) {
                String name = remps.readUTF();
                double salary = remps.readDouble();
                remps.skipBytes(8); // Saltar fechas de contratación y despido
                RandomAccessFile salesFile = salesFileFor(code);

                Calendar cal = Calendar.getInstance();
                int mesActual = cal.get(Calendar.MONTH);
                int añoActual = cal.get(Calendar.YEAR);

                // Posicionarse en el mes actual
                salesFile.seek(mesActual * 9);
                double ventasMes = salesFile.readDouble();
                boolean pagado = salesFile.readBoolean();

                if (pagado) {
                    System.out.println("El empleado con código " + code + " ya ha recibido su pago este mes.");
                    return;
                }

                // Calcular los valores del recibo
                double comision = ventasMes * 0.10;
                double sueldoBase = salary + comision;
                double deduccion = sueldoBase * 0.035;
                double sueldoNeto = sueldoBase - deduccion;

                // Escribir recibo en el archivo del empleado
                String reciboPath = employeeFolder(code) + "/recibos.emp";
                RandomAccessFile recibosFile = new RandomAccessFile(reciboPath, "rw");
                recibosFile.seek(recibosFile.length());
                recibosFile.writeLong(new Date().getTime()); // Fecha de pago
                recibosFile.writeDouble(comision);
                recibosFile.writeDouble(sueldoBase);
                recibosFile.writeDouble(deduccion);
                recibosFile.writeDouble(sueldoNeto);
                recibosFile.writeInt(añoActual);
                recibosFile.writeInt(mesActual);
                recibosFile.close();

                // Marcar como pagado
                salesFile.seek(mesActual * 9 + 8); // Posición del booleano de pago
                salesFile.writeBoolean(true);

                // Mostrar salida
                System.out.println("Pago realizado a: " + name);
                System.out.println("Sueldo neto: Lps. " + sueldoNeto);
                return;
            } else {
                remps.skipBytes(28); // Saltar al siguiente registro
            }
        }
    }

   void printEmployee(int code) throws IOException {
        remps.seek(0);
        boolean found = false;
        while (remps.getFilePointer() < remps.length()) {
            int empCode = remps.readInt();
            if (empCode == code) {
                found = true;
                String name = remps.readUTF();
                double salary = remps.readDouble();
                Date dateH = new Date(remps.readLong());
                System.out.println("Código: " + empCode);
                System.out.println("Nombre: " + name);
                System.out.println("Salario: Lps. " + salary);
                System.out.println("Fecha de contratación: " + dateH);

                // Mostrar ventas anuales
                RandomAccessFile salesFile = salesFileFor(code);
                double totalVentas = 0;
                System.out.println("Ventas anuales:");
                for (int mes = 0; mes < 12; mes++) {
                    salesFile.seek(mes * 9);
                    double ventasMes = salesFile.readDouble();
                    totalVentas += ventasMes;
                    System.out.println("Mes " + (mes + 1) + ": Lps. " + ventasMes);
                }
                System.out.println("Total de ventas anuales: Lps. " + totalVentas);

                // Mostrar recibos históricos
                String reciboPath = employeeFolder(code) + "/recibos.emp";
                File recibosFile = new File(reciboPath);
                if (recibosFile.exists()) {
                    RandomAccessFile recibos = new RandomAccessFile(recibosFile, "r");
                    System.out.println("Recibos históricos:");
                    while (recibos.getFilePointer() < recibos.length()) {
                        long fechaPago = recibos.readLong();
                        double comision = recibos.readDouble();
                        double sueldoBase = recibos.readDouble();
                        double deduccion = recibos.readDouble();
                        double sueldoNeto = recibos.readDouble();
                        int año = recibos.readInt();
                        int mes = recibos.readInt();
                        System.out.println("Fecha de pago: " + new Date(fechaPago));
                        System.out.println("Comisión: Lps. " + comision);
                        System.out.println("Sueldo base: Lps. " + sueldoBase);
                        System.out.println("Deducción: Lps. " + deduccion);
                        System.out.println("Sueldo neto: Lps. " + sueldoNeto);
                        System.out.println("Año: " + año + ", Mes: " + (mes + 1));
                    }
                    recibos.close();
                } else {
                    System.out.println("No hay recibos históricos.");
                }
                return;
            } else {
                remps.skipBytes(28); // Saltar al siguiente registro
            }
        }
        if (!found) {
            System.out.println("Empleado con código " + code + " no encontrado.");
        }
    }
}

}
