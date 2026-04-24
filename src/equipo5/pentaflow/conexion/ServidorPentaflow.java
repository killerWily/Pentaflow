package equipo5.pentaflow.conexion;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServidorPentaflow {
    public static void main(String[] args) {
        int puerto = 5000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("--- SERVIDOR PENTAFLOW: ESCUCHANDO EN PUERTO " + puerto + " ---");

            while (true) {
                try (Socket cliente = servidor.accept();
                     DataInputStream entrada = new DataInputStream(cliente.getInputStream())) {

                    for (int i = 0; i < 2; i++) { // Esperamos 2 paquetes (Cadena y Binario)
                        
                        int tipo = entrada.readInt();
                        int tamanoBytes = entrada.readInt();
                        
                        // --- INICIO DE MEDICION DE TIEMPO ---
                        long tiempoInicio = System.nanoTime();
                        
                        byte[] buffer = new byte[tamanoBytes];
                        entrada.readFully(buffer);

                        if (tipo == 1) {
                            procesarCadena(buffer, tamanoBytes, tiempoInicio);
                        } else {
                            procesarBinario(buffer, tamanoBytes, tiempoInicio);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Sesion finalizada: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void procesarCadena(byte[] datos, int bytes, long tInicio) {
        String contenido = new String(datos);
        long tFin = System.nanoTime();
        
        System.out.println("\n[RECEPCION CADENA]");
        System.out.println("Contenido: " + contenido);
        System.out.println("Tamaño: " + bytes + " bytes");
        System.out.println("Tiempo de procesamiento: " + (tFin - tInicio) + " nanosegundos");
    }

    private static void procesarBinario(byte[] datos, int bytes, long tInicio) {
        // Reconstrucción básica para demostrar procesamiento
        ByteBuffer bb = ByteBuffer.wrap(datos);
        
        byte[] claveB = new byte[6];
        byte[] nombreB = new byte[30];
        bb.get(claveB);
        bb.get(nombreB);
        double precio = bb.getDouble();
        int cant = bb.getInt();
        
        long tFin = System.nanoTime();

        System.out.println("\n[RECEPCION BINARIA]");
        System.out.println("Contenido (Clave|Nombre): " + new String(claveB).trim() + " | " + new String(nombreB).trim());
        System.out.println("Precio recuperado: $" + precio + " | Cantidad: " + cant);
        System.out.println("Tamaño: " + bytes + " bytes");
        System.out.println("Tiempo de procesamiento: " + (tFin - tInicio) + " nanosegundos");
    }
}