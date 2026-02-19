package org.pr1.ui;

import javax.swing.*;
import java.awt.*;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Configuración Algoritmo Genético");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        int[][] tablero = {
                {0, 0, 1, 0, 2},
                {0, 1, 0, 0, 0},
                {2, 0, 0, 1, 0},
                {0, 0, 0, 0, 0},
                {1, 0, 2, 0, 0}
        };

        Tablero tableroPanel = new Tablero(tablero);

        mainPanel.add(new Configuracion());
        mainPanel.add(tableroPanel);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}