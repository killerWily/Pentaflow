package equipo5.pentaflow.conexion;

import equipo5.pentaflow.util.ValidadorEsquema;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorPentaflow {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println(">>> MONITOR PENTAFLOW ACTIVO (SOCKET TCP/IP) <<<");

        while (true) {
            try (Socket s = server.accept(); DataInputStream in = new DataInputStream(s.getInputStream())) {
                for (int i = 0; i < 4; i++) { // Procesar los 4 formatos de la práctica
                    int tipo = in.readInt();
                    int tam = in.readInt();
                    long inicio = System.nanoTime();
                    
                    byte[] data = new byte[tam];
                    in.readFully(data);
                    long fin = System.nanoTime();

                    switch(tipo) {
                        case 1 -> procesarCadena(data, tam, fin-inicio);
                        case 2 -> procesarBinario(data, tam, fin-inicio);
                        case 3 -> procesarXML(data, tam, fin-inicio);
                        case 4 -> procesarProtobuf(data, tam, fin-inicio);
                    }
                }
            } catch (Exception e) { System.out.println("> Cliente desconectado."); }
        }
    }

    private static void procesarCadena(byte[] d, int t, long n) {
        String[] c = new String(d).split("\\|");
        mostrarUI(c[0], c[1], "CADENA", t, n, null);
    }

    private static void procesarBinario(byte[] d, int t, long n) {
        ByteBuffer bb = ByteBuffer.wrap(d);
        byte[] id = new byte[6]; bb.get(id);
        byte[] nom = new byte[30]; bb.get(nom);
        mostrarUI(new String(id).trim(), new String(nom).trim(), "BINARIO FIJO", t, n, null);
    }

    private static void procesarXML(byte[] d, int t, long n) {
        boolean v = ValidadorEsquema.validar(new String(d), "resources/esquemas/producto.xsd");
        mostrarUI("XML-DATA", "Archivo Esquema XML", "XML+XSD", t, n, v);
    }

    private static void procesarProtobuf(byte[] d, int t, long n) {
        mostrarUI("PBF-DATA", "Objeto Serializado", "PROTOBUF", t, n, true);
    }

    private static void mostrarUI(String id, String nom, String modo, int tam, long tiempo, Boolean val) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("  MODO: " + modo + " | ID: " + id);
        System.out.println("  ACTIVO: " + nom);
        System.out.println("  MÉTRICAS: " + tam + " bytes | " + tiempo + " ns");
        if(val != null) System.out.println("  ESQUEMA: " + (val ? "VÁLIDO ✓" : "ERROR ✗"));
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}