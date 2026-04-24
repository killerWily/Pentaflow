package equipo5.pentaflow.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrito implements Serializable {
    private static final long serialVersionUID = 1L; // Para compatibilidad de versiones
    private List<Item> items = new ArrayList<>();

    public void agregar(String id, String nombre, int cantidad, double precio) {
        items.add(new Item(id, nombre, cantidad, precio));
    }

    public List<Item> getItems() { return items; }

    // Clase interna que representa un producto dentro del carrito
    public static class Item implements Serializable {
        public String id, nombre;
        public int cantidad;
        public double precio;

        public Item(String id, String nombre, int cantidad, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precio = precio;
        }
    }
}