package equipo5.pentaflow.conexion;

import equipo5.pentaflow.modelos.Producto;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.time.LocalDate;

public class ClientePentaflow {
    public static void main(String[] args) {
        // Producto sugerido: Bomba Hidráulica
        Producto p = new Producto("BOM005", "Bomba Centrifuga de Lodos 5HP", 12450.50, 3, "Pentaflow-Engineers", LocalDate.now());

        try (Socket s = new Socket("localhost", 5000); DataOutputStream out = new DataOutputStream(s.getOutputStream())) {
            
            System.out.println(">>> ENVIANDO ACTIVOS AL MONITOR PENTAFLOW...");

            // 1. Envío Cadena
            byte[] cad = p.aCadena().getBytes();
            enviarPaquete(out, 1, cad);

            // 2. Envío Binario
            byte[] bin = p.aBinario();
            enviarPaquete(out, 2, bin);

            // 3. Envío XML (Ejercicio 4)
            byte[] xml = Files.readAllBytes(Paths.get("resources/esquemas/producto.xml"));
            enviarPaquete(out, 3, xml);

            System.out.println(">>> Procesos terminados.");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void enviarPaquete(DataOutputStream out, int tipo, byte[] datos) throws IOException {
        out.writeInt(tipo);
        out.writeInt(datos.length);
        out.write(datos);
        out.flush();
    }
}