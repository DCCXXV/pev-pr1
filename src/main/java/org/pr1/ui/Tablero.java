package org.pr1.ui;

import javax.swing.*;
import java.awt.*;

public class Tablero extends JPanel {

    private int[][] tablero;
    private int tamCelda = 50;

    public Tablero(int[][] tablero) {
        this.tablero = tablero;
        setPreferredSize(new Dimension(
                tablero[0].length * tamCelda,
                tablero.length * tamCelda
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int fila = 0; fila < tablero.length; fila++) {
            for (int col = 0; col < tablero[0].length; col++) {

                int x = col * tamCelda;
                int y = fila * tamCelda;

                // Fondo
                //g2.setColor(Color.WHITE);
                //g2.fillRect(x, y, tamCelda, tamCelda);

                // Columnas
                if (tablero[fila][col] == 1) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x, y, tamCelda, tamCelda);
                }

                // Líneas de la cuadrícula
                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tamCelda, tamCelda);

                // Cámaras
                if (tablero[fila][col] == 2) {
                    g2.setColor(Color.BLUE);
                    g2.fillOval(x + 10, y + 10, tamCelda - 20, tamCelda - 20);

                    // Dibujo del cono de visión
                    int radio = tamCelda * 2; // 2 casillas de profundidad
                    int centroX = x + tamCelda / 2;
                    int centroY = y + tamCelda / 2;
                    int inicioAngulo = -15; // 30 grados de apertura centrado
                    int anguloApertura = 30;

                    g2.setColor(new Color(214, 19, 19, 255)); // amarillo semi-transparente
                    g2.fillArc(centroX - radio, centroY - radio, radio * 2, radio * 2, inicioAngulo, anguloApertura);
                }
            }
        }
    }
}
