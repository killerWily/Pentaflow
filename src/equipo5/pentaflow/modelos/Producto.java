package equipo5.pentaflow.modelos;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Entidad Producto para el proyecto Pentaflow.
 * Gestiona la transformación a String delimitado y Binario de longitud fija.
 */
public class Producto {
    private String clave, nombre, marca;
    private double precio;
    private int cantidad;
    private LocalDate fechaReg;

    public Producto(String clave, String nombre, double precio, int cantidad, String marca, LocalDate fechaReg) {
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.marca = marca;
        this.fechaReg = fechaReg;
    }

    // Formato Cadena: Clave|Nombre|Precio|Cantidad|Marca|Fecha (DD/MM/AAAA)
    public String aCadena() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.join("|", clave, nombre, String.valueOf(precio), 
                           String.valueOf(cantidad), marca, fechaReg.format(dtf));
    }

    // Formato Binario: Longitudes fijas según requerimiento (86 bytes total)
    public byte[] aBinario() {
        ByteBuffer buffer = ByteBuffer.allocate(86);
        buffer.put(ajustar(clave, 6).getBytes());
        buffer.put(ajustar(nombre, 30).getBytes());
        buffer.putDouble(precio);
        buffer.putInt(cantidad);
        buffer.put(ajustar(marca, 30).getBytes());
        buffer.putLong(fechaReg.atStartOfDay(ZoneOffset.UTC).toEpochSecond());
        return buffer.array();
    }

    private String ajustar(String t, int l) {
        return (t.length() > l) ? t.substring(0, l) : String.format("%-" + l + "s", t);
    }
}