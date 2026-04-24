package equipo5.pentaflow.conexion;

import equipo5.pentaflow.modelos.Carrito;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClienteTienda extends JFrame {
    private JTextField txtCantBomba, txtCantMotor;

    public ClienteTienda() {
        setTitle("PENTAFLOW - Cliente de Compras");
        setSize(350, 250);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel(" Cant. Bomba (BOM005):"));
        txtCantBomba = new JTextField("0");
        add(txtCantBomba);

        add(new JLabel(" Cant. Motor (MOT001):"));
        txtCantMotor = new JTextField("0");
        add(txtCantMotor);

        JButton btnComprar = new JButton("Realizar Pedido");
        btnComprar.setBackground(Color.GREEN);
        btnComprar.addActionListener(e -> enviarCarrito());
        add(btnComprar);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void enviarCarrito() {
        try (Socket s = new Socket("localhost", 6000);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {

            Carrito miCarrito = new Carrito();
            
            // Agregar Bomba si la cantidad > 0
            int qB = Integer.parseInt(txtCantBomba.getText());
            if(qB > 0) miCarrito.agregar("BOM005", "Bomba Centrifuga", qB, 12450.50);
            
            // Agregar Motor si la cantidad > 0
            int qM = Integer.parseInt(txtCantMotor.getText());
            if(qM > 0) miCarrito.agregar("MOT001", "Motor Trifasico", qM, 8500.00);

            out.writeObject(miCarrito); // Enviar al servidor
            
            String respuesta = (String) in.readObject();
            if (respuesta.startsWith("OK") || respuesta.startsWith("COMPRA")) {
                JOptionPane.showMessageDialog(this, respuesta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, respuesta, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor Pentaflow.");
        }
    }

    public static void main(String[] args) { new ClienteTienda(); }
}