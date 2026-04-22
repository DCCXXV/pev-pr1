package G12P3.ui;

import java.awt.*;
import javax.swing.*;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Programación Genética, Rover Lunar");
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Tablero tablero = new Tablero();
        Grafica grafica = new Grafica();
        PanelFenotipo fenotipo = new PanelFenotipo();
        Configuracion configuracion = new Configuracion(
            tablero,
            grafica,
            fenotipo
        );

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.add(configuracion);
        topPanel.add(tablero);
        topPanel.add(grafica);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(fenotipo, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}
