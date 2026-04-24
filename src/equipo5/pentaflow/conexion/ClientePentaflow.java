package equipo5.pentaflow.conexion;

import equipo5.pentaflow.modelos.Producto;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.time.LocalDate;

public class ClientePentaflow {
    public static void main(String[] args) {
        Producto p = new Producto("PNT500", "Sensor Laser Industrial", 450.0, 10, "Pentacorp", LocalDate.now());

        try (Socket s = new Socket("localhost", 5000); DataOutputStream out = new DataOutputStream(s.getOutputStream())) {
            
            // 1. Enviar Cadena
            byte[] c = p.aCadena().getBytes();
            enviar(out, 1, c);

            // 2. Enviar Binario
            byte[] b = p.aBinario();
            enviar(out, 2, b);

            // 3. Enviar XML (desde archivo)
            byte[] x = Files.readAllBytes(Paths.get("resources/esquemas/producto.xml"));
            enviar(out, 3, x);

            System.out.println(">>> Todos los formatos Pentaflow enviados con éxito.");

        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void enviar(DataOutputStream out, int tipo, byte[] d) throws IOException {
        out.writeInt(tipo);
        out.writeInt(d.length);
        out.write(d);
    }
}