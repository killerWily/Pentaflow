package equipo5.pentaflow.conexion;

import equipo5.pentaflow.modelos.Producto;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.time.LocalDate;

public class ClientePentaflow {
    public static void main(String[] args) {
        Producto p = new Producto("BOM005", "Bomba Centrifuga 5HP", 12450.5, 3, "Pentaflow", LocalDate.now());

        try (Socket s = new Socket("localhost", 5000); DataOutputStream out = new DataOutputStream(s.getOutputStream())) {
            // 1. Cadena
            enviar(out, 1, p.aCadena().getBytes());
            // 2. Binario Fijo
            enviar(out, 2, p.aBinarioFijo());
            // 3. XML
            enviar(out, 3, Files.readAllBytes(Paths.get("resources/esquemas/producto.xml")));
            // 4. Protobuf
            enviar(out, 4, p.aProtobuf());

            System.out.println(">>> Todos los esquemas de Pentaflow enviados.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void enviar(DataOutputStream out, int tipo, byte[] data) throws IOException {
        out.writeInt(tipo);
        out.writeInt(data.length);
        out.write(data);
        out.flush();
    }
}