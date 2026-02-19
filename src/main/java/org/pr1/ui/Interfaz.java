package org.pr1.ui;

import javax.swing.*;
import java.awt.*;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Configuración Algoritmo Genético");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        //se crean los paneles



        //panel del tablero optimo
        int[][] matriz = {
                {2, 2, 1, 2, 2},
                {2, 1, 2, 2, 2},
                {2, 3, 2, 1, 2},
                {2, 0, 3, 2, 0},
                {1, 2, 2, 2, 2}
        };
        Tablero tablero = new Tablero(matriz);

        //panel de la grafica
        Grafica grafica = new Grafica(
            new int[]{1,2,3},
            new int[]{1,2,3},
            new double[]{1,2,3}
        );

        Configuracion configuracion = new Configuracion(tablero);

        //se meten los paneles en el panel principal
        mainPanel.add(configuracion);
        mainPanel.add(tablero);
        mainPanel.add(grafica);

        //se mete el panel princiapl
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}