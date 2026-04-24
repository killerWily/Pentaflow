package equipo5.pentaflow.conexion;

import equipo5.pentaflow.util.ValidadorEsquema;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorPentaflow {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println(">>> MONITOR PENTAFLOW: ESPERANDO ACTIVOS EN PUERTO 5000 <<<");

        while (true) {
            try (Socket s = server.accept(); DataInputStream in = new DataInputStream(s.getInputStream())) {
                for (int i = 0; i < 3; i++) { // Procesa Cadena, Binario y XML
                    int tipo = in.readInt();
                    int tamano = in.readInt();
                    long inicio = System.nanoTime();
                    
                    byte[] buffer = new byte[tamano];
                    in.readFully(buffer);
                    long fin = System.nanoTime();

                    if (tipo == 1) procesarCadena(buffer, tamano, fin - inicio);
                    else if (tipo == 2) procesarBinario(buffer, tamano, fin - inicio);
                    else if (tipo == 3) procesarXML(buffer, tamano, fin - inicio);
                }
            } catch (Exception e) { System.out.println("> Sesión finalizada."); }
        }
    }

    private static void procesarCadena(byte[] datos, int tam, long tiempo) {
        String[] c = new String(datos).split("\\|");
        mostrarInterfaz(c[0], c[1], c[4], Double.parseDouble(c[2]), Integer.parseInt(c[3]), "CADENA DELIMITADA", tam, tiempo, null);
    }

    private static void procesarBinario(byte[] datos, int tam, long tiempo) {
        ByteBuffer bb = ByteBuffer.wrap(datos);
        byte[] clave = new byte[6];   bb.get(clave);
        byte[] nombre = new byte[30]; bb.get(nombre);
        double precio = bb.getDouble();
        int cant = bb.getInt();
        byte[] marca = new byte[30];  bb.get(marca);

        mostrarInterfaz(new String(clave).trim(), new String(nombre).trim(), 
                       new String(marca).trim(), precio, cant, "BINARIO FIJO", tam, tiempo, null);
    }

    private static void procesarXML(byte[] datos, int tam, long tiempo) {
        String xml = new String(datos);
        boolean esValido = ValidadorEsquema.validar(xml, "resources/esquemas/producto.xsd");
        // Nota: Aquí podrías parsear el XML para mostrarlo en la interfaz si lo deseas
        mostrarInterfaz("XML-VAL", "Archivo XML de Producto", "Varios", 0.0, 0, "ESQUEMA XML", tam, tiempo, esValido);
    }

    private static void mostrarInterfaz(String id, String nom, String marca, double precio, int stock, String modo, int tam, long tiempo, Boolean validacion) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PENTAFLOW - MONITOR DE ACTIVOS          ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("  > MODO DE RECEPCIÓN: " + modo);
        System.out.println("  > ID PRODUCTO      : " + id);
        System.out.println("  > NOMBRE           : " + nom);
        System.out.println("  > MARCA            : " + marca);
        System.out.println("  > PRECIO UNITARIO  : $" + precio);
        System.out.println("  > STOCK ACTUAL     : " + stock + " piezas");
        if (validacion != null) System.out.println("  > VALIDACIÓN XSD   : " + (validacion ? "EXITOSA ✓" : "FALLIDA ✗"));
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("  [MÉTRICAS DE RED]");
        System.out.println("  Tamaño del paquete : " + tam + " bytes");
        System.out.println("  Latencia de proc.  : " + tiempo + " ns");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}