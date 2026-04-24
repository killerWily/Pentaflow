package equipo5.pentaflow.conexion;

import equipo5.pentaflow.modelos.Producto;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

public class ClientePentaflow {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5000;

        // Crear una instancia de prueba
        Producto prod = new Producto("MNT001", "Bomba Centrifuga 5HP", 12500.00, 2, "FlowServe", LocalDate.now());

        try (Socket socket = new Socket(host, puerto);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("--- PENTAFLOW CLIENTE: INICIANDO ENVIO ---");

            // 1. ENVIO EN FORMATO CADENA
            byte[] datosCadena = prod.aFormatoCadena().getBytes();
            salida.writeInt(1); // Indicador de tipo: 1 = Cadena
            salida.writeInt(datosCadena.length);
            salida.write(datosCadena);
            System.out.println("Paquete cadena enviado (" + datosCadena.length + " bytes)");

            // 2. ENVIO EN FORMATO BINARIO
            byte[] datosBinarios = prod.aFormatoBinario();
            salida.writeInt(2); // Indicador de tipo: 2 = Binario
            salida.writeInt(datosBinarios.length);
            salida.write(datosBinarios);
            System.out.println("Paquete binario enviado (" + datosBinarios.length + " bytes)");

            salida.flush();

        } catch (IOException e) {
            System.err.println("Error en la conexion: " + e.getMessage());
        }
    }
}