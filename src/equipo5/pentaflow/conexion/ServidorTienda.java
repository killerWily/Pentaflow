package equipo5.pentaflow.conexion;

import equipo5.pentaflow.modelos.Carrito;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServidorTienda extends JFrame {
    private Map<String, Integer> inventario = new HashMap<>();
    private JTextArea logArea;
    private DefaultListModel<String> modeloLista;

    public ServidorTienda() {
        // 1. Inventario inicial de Pentaflow
        inventario.put("BOM005", 10); // Bomba de Lodos
        inventario.put("MOT001", 5);  // Motor Trifásico
        inventario.put("SEN002", 20); // Sensor Láser

        // 2. Interfaz Gráfica
        setTitle("PENTAFLOW SERVER - Gestión de Inventario");
        setSize(500, 400);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        modeloLista = new DefaultListModel<>();
        actualizarVistaInventario();

        JList<String> listaInv = new JList<>(modeloLista);
        add(new JLabel("  Inventario Actual:"), BorderLayout.NORTH);
        add(new JScrollPane(listaInv), BorderLayout.WEST);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        iniciarServidor();
    }

    private void actualizarVistaInventario() {
        modeloLista.clear();
        inventario.forEach((id, cant) -> modeloLista.addElement(id + ": " + cant + " unidades"));
    }

    private void iniciarServidor() {
        new Thread(() -> {
            try (ServerSocket ss = new ServerSocket(6000)) {
                logArea.append(">>> Servidor de Tienda Pentaflow Online en Puerto 6000\n");
                while (true) {
                    Socket s = ss.accept();
                    new ManejadorCompra(s).start();
                }
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    private class ManejadorCompra extends Thread {
        private Socket s;
        public ManejadorCompra(Socket s) { this.s = s; }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {

                Carrito carrito = (Carrito) in.readObject();
                String resultado = procesarVenta(carrito);
                out.writeObject(resultado); // Enviar respuesta al cliente

            } catch (Exception e) { logArea.append("! Error en conexión con cliente.\n"); }
        }

        private synchronized String procesarVenta(Carrito c) {
            // Validaciones del Ejercicio 6
            if (c.getItems().isEmpty()) return "ERROR: El carrito está vacío.";

            for (Carrito.Item item : c.getItems()) {
                if (item.cantidad <= 0 || item.precio <= 0) return "ERROR: Valores inválidos.";
                if (inventario.getOrDefault(item.id, 0) < item.cantidad) {
                    logArea.append("> Venta fallida: Stock insuficiente de " + item.id + "\n");
                    return "ERROR: Stock insuficiente para " + item.nombre;
                }
            }

            // Si todo es válido, descontar
            for (Carrito.Item item : c.getItems()) {
                inventario.put(item.id, inventario.get(item.id) - item.cantidad);
            }
            
            actualizarVistaInventario();
            logArea.append("> Venta Exitosa: Carrito procesado.\n");
            return "COMPRA EXITOSA: Su pedido está en camino.";
        }
    }

    public static void main(String[] args) { new ServidorTienda(); }
}