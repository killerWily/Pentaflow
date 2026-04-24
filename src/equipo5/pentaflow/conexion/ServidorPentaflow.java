package equipo5.pentaflow.conexion;

import equipo5.pentaflow.util.ValidadorEsquema;
import java.io.*;
import java.net.*;

public class ServidorPentaflow {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println(">>> Servidor PENTAFLOW Online <<<");

        while (true) {
            try (Socket s = server.accept(); DataInputStream in = new DataInputStream(s.getInputStream())) {
                for (int i = 0; i < 3; i++) { // Esperar: 1.Cadena, 2.Binario, 3.XML
                    int tipo = in.readInt();
                    int tam = in.readInt();
                    long inicio = System.nanoTime();
                    
                    byte[] data = new byte[tam];
                    in.readFully(data);
                    
                    long fin = System.nanoTime();
                    String contenido = (tipo == 2) ? "Datos Binarios Recibidos" : new String(data);
                    
                    System.out.println("\n--- PAQUETE RECIBIDO ---");
                    System.out.println("Tipo: " + (tipo == 1 ? "Cadena" : tipo == 2 ? "Binario" : "XML"));
                    System.out.println("Tamaño: " + tam + " bytes");
                    System.out.println("Tiempo Proc: " + (fin - inicio) + " ns");
                    
                    if (tipo == 3) {
                        boolean v = ValidadorEsquema.validar(contenido, "resources/esquemas/producto.xsd");
                        System.out.println("Validación XSD: " + (v ? "EXITOSA" : "FALLIDA"));
                    }
                }
            } catch (Exception e) { System.out.println("Conexión cerrada."); }
        }
    }
}