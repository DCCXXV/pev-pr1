package org.pr1.ui;

import javax.swing.*;
import java.awt.*;

public class Tablero extends JPanel {

    private int[][] tablero;
    private int tamCelda = 30;

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

        // Tamaño total del tablero
        int anchoTablero = tablero[0].length * tamCelda;
        int altoTablero = tablero.length * tamCelda;

        // Calcular desplazamiento para centrar
        int offsetX = (getWidth() - anchoTablero) / 2;
        int offsetY = (getHeight() - altoTablero) / 2;

        for (int fila = 0; fila < tablero.length; fila++) {
            for (int col = 0; col < tablero[0].length; col++) {

                int x = offsetX + col * tamCelda;
                int y = offsetY + fila * tamCelda;

                // Dibujar la celda según el valor
                switch (tablero[fila][col]) {
                    case 0:
                        g2.setColor(Color.WHITE);
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        break;
                    case 1:
                        g2.setColor(new Color(72, 201, 176));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        // Dibujar el punto en el centro
                        g2.setColor(Color.RED);
                        int diametro = tamCelda / 4;
                        int centroX = x + tamCelda / 2 - diametro / 2;
                        int centroY = y + tamCelda / 2 - diametro / 2;
                        g2.fillOval(centroX, centroY, diametro, diametro);
                        break;
                    case 2:
                        g2.setColor(new Color(72, 201, 176));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        break;
                    case 3:
                        g2.setColor(Color.BLACK);
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        break;
                }

                // Dibujar bordes
                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tamCelda, tamCelda);
            }
        }
    }

    public void setTablero(int[][] nuevoTablero) {
        this.tablero = nuevoTablero;
    }
}
