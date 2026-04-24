package equipo5.pentaflow.modelos;

import java.io.*;
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

    // EJERCICIO 3: Formato Cadena
    public String aCadena() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.join("|", clave, nombre, String.valueOf(precio), 
                           String.valueOf(cantidad), marca, fechaReg.format(dtf));
    }

    // EJERCICIO 3: Formato Binario Fijo (86 bytes)
    public byte[] aBinarioFijo() {
        ByteBuffer buffer = ByteBuffer.allocate(86);
        buffer.put(ajustar(clave, 6).getBytes());
        buffer.put(ajustar(nombre, 30).getBytes());
        buffer.putDouble(precio);
        buffer.putInt(cantidad);
        buffer.put(ajustar(marca, 30).getBytes());
        buffer.putLong(fechaReg.atStartOfDay(ZoneOffset.UTC).toEpochSecond());
        return buffer.array();
    }

    // EJERCICIO 5: Simulación Protobuf (Binario Estructurado con Tags)
    public byte[] aProtobuf() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeByte(1); dos.writeUTF(clave);
            dos.writeByte(2); dos.writeUTF(nombre);
            dos.writeByte(3); dos.writeDouble(precio);
            dos.writeByte(4); dos.writeInt(cantidad);
            dos.writeByte(5); dos.writeUTF(marca);
            dos.writeByte(6); dos.writeLong(fechaReg.atStartOfDay(ZoneOffset.UTC).toEpochSecond());
        } catch (IOException e) { e.printStackTrace(); }
        return baos.toByteArray();
    }

    private String ajustar(String t, int l) {
        return (t.length() > l) ? t.substring(0, l) : String.format("%-" + l + "s", t);
    }
}