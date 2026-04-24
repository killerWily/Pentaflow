package equipo5.pentaflow.modelos;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Entidad que representa un producto en el sistema Pentaflow.
 * Incluye metodos para exportar a formato de cadena delimitada y binario fijo.
 */
public class Producto {
    private String clave;     // Requisito: 6 caracteres
    private String nombre;    // Requisito: 30 caracteres
    private double precio;
    private int cantidad;
    private String marca;     // Requisito: 30 caracteres
    private LocalDate fechaReg;

    public Producto(String clave, String nombre, double precio, int cantidad, String marca, LocalDate fechaReg) {
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.marca = marca;
        this.fechaReg = fechaReg;
    }

    // --- FORMATO CADENA (DELIMITADO POR |) ---
    public String aFormatoCadena() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.join("|", 
            clave, 
            nombre, 
            String.valueOf(precio), 
            String.valueOf(cantidad), 
            marca, 
            fechaReg.format(dtf)
        );
    }

    // --- FORMATO BINARIO (LONGITUD FIJA) ---
    public byte[] aFormatoBinario() {
        // Calculo de bytes: 6(clave) + 30(nombre) + 8(double) + 4(int) + 30(marca) + 8(long epoch) = 86 bytes
        ByteBuffer buffer = ByteBuffer.allocate(86);
        
        // Cadenas con ajuste de tamaño fijo
        buffer.put(ajustarTexto(clave, 6).getBytes());
        buffer.put(ajustarTexto(nombre, 30).getBytes());
        
        buffer.putDouble(precio);
        buffer.putInt(cantidad);
        
        buffer.put(ajustarTexto(marca, 30).getBytes());
        
        // Fecha en formato Time Stamp (Epoch)
        long epoch = fechaReg.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        buffer.putLong(epoch);
        
        return buffer.array();
    }

    /**
     * Asegura que el string tenga exactamente la longitud deseada.
     * Si es corto, rellena con espacios; si es largo, lo recorta.
     */
    private String ajustarTexto(String texto, int longitud) {
        if (texto.length() > longitud) return texto.substring(0, longitud);
        return String.format("%-" + longitud + "s", texto);
    }

    // Getters para visualizacion en consola
    public String getNombre() { return nombre; }
}