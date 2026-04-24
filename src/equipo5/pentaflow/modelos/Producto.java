package equipo5.pentaflow.modelos;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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

    // Formato Cadena para Ejercicio 3
    public String aCadena() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.join("|", clave, nombre, String.valueOf(precio), 
                           String.valueOf(cantidad), marca, fechaReg.format(dtf));
    }

    // Formato Binario para Ejercicio 3 (Longitud fija: 86 bytes)
    public byte[] aBinario() {
        ByteBuffer buffer = ByteBuffer.allocate(86);
        buffer.put(ajustarTexto(clave, 6).getBytes());
        buffer.put(ajustarTexto(nombre, 30).getBytes());
        buffer.putDouble(precio);
        buffer.putInt(cantidad);
        buffer.put(ajustarTexto(marca, 30).getBytes());
        buffer.putLong(fechaReg.atStartOfDay(ZoneOffset.UTC).toEpochSecond());
        return buffer.array();
    }

    private String ajustarTexto(String texto, int longitud) {
        if (texto.length() > longitud) return texto.substring(0, longitud);
        return String.format("%-" + longitud + "s", texto);
    }
}