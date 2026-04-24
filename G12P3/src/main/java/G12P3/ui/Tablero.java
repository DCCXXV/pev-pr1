package G12P3.ui;

import G12P3.ag.Cromosoma;
import java.awt.*;
import javax.swing.*;

public class Tablero extends JPanel {

    private int tamCelda = 28;

    private int[][] mapaBase;
    private Cromosoma mejor;
    private int indiceMapa = 0;

    private Graphics2D g2;
    private int offsetX;
    private int offsetY;

    public Tablero() {}

    public void setMapaBase(int[][] mapa) {
        this.mapaBase = mapa;
        this.mejor = null;
        this.indiceMapa = 0;
        repaint();
    }

    public void setMejor(Cromosoma mejor) {
        this.mejor = mejor;
        SwingUtilities.invokeLater(this::repaint);
    }

    public void setIndiceMapa(int i) {
        this.indiceMapa = i;
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapaBase == null) return;

        g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // datos del mapa seleccionado (si hay mejor ya evaluado)
        Cromosoma.DatosMapa dm = null;
        if (mejor != null && mejor.datosPorMapa != null
            && indiceMapa < mejor.datosPorMapa.length
            && mejor.datosPorMapa[indiceMapa] != null) {
            dm = mejor.datosPorMapa[indiceMapa];
        }

        // mapa original: el del individuo evaluado si existe, si no, el preview
        int[][] mapaOriginal = (dm != null) ? dm.mapaInicial : mapaBase;

        int anchoTablero = mapaOriginal[0].length * tamCelda;
        int altoTablero = mapaOriginal.length * tamCelda;
        offsetX = Math.max(10, (getWidth() - anchoTablero) / 2);
        offsetY = 10;

        // mapa para pintar: si hay mejor, usamos su estado final (muestras recogidas quitadas)
        int[][] mapa = (dm != null) ? dm.mapaFinal : mapaOriginal;
        boolean[][] visitado = (dm != null) ? dm.visitado : null;

        for (int row = 0; row < mapaOriginal.length; row++) {
            for (int col = 0; col < mapaOriginal[0].length; col++) {
                int x = offsetX + col * tamCelda;
                int y = offsetY + row * tamCelda;
                int celda = mapa[row][col];

                switch (celda) {
                    case 1 -> {
                        g2.setColor(new Color(90, 20, 20));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        g2.setColor(new Color(220, 60, 60));
                        g2.setStroke(new BasicStroke(2));
                        g2.drawLine(
                            x + 4,
                            y + 4,
                            x + tamCelda - 4,
                            y + tamCelda - 4
                        );
                        g2.drawLine(
                            x + tamCelda - 4,
                            y + 4,
                            x + 4,
                            y + tamCelda - 4
                        );
                    }
                    case 3 -> {
                        g2.setColor(new Color(230, 140, 40));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                    }
                    default -> {
                        g2.setColor(new Color(55, 55, 60));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                    }
                }

                // huella
                if (visitado != null && visitado[row][col] && celda != 1) {
                    g2.setColor(new Color(120, 220, 255, 100));
                    g2.fillRect(x, y, tamCelda, tamCelda);
                }

                // muestra recogida (transparente) si el rover la visito; si no, muestra normal
                boolean recogida = visitado != null
                    && visitado[row][col]
                    && mapaOriginal[row][col] == 2
                    && celda != 2;
                if (celda == 2 || recogida) {
                    int d = tamCelda - 10;
                    if (recogida) {
                        g2.setColor(new Color(255, 220, 40, 70));
                        g2.fillOval(x + 5, y + 5, d, d);
                        g2.setColor(new Color(180, 120, 0, 120));
                    } else {
                        g2.setColor(new Color(255, 220, 40));
                        g2.fillOval(x + 5, y + 5, d, d);
                        g2.setColor(new Color(180, 120, 0));
                    }
                    g2.setStroke(new BasicStroke(1));
                    g2.drawOval(x + 5, y + 5, d, d);
                }

                // borde
                g2.setColor(new Color(80, 80, 80));
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(x, y, tamCelda, tamCelda);
            }
        }

        if (dm != null) {
            pintarRover(dm.posFila, dm.posCol, dm.direccion);
            pintarDatos(altoTablero, dm);
        }
    }

    private void pintarRover(int fila, int col, int direccion) {
        int x = offsetX + col * tamCelda;
        int y = offsetY + fila * tamCelda;
        g2.setColor(new Color(0, 150, 255));
        g2.fillOval(x + 4, y + 4, tamCelda - 8, tamCelda - 8);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(x + 4, y + 4, tamCelda - 8, tamCelda - 8);

        // flecha de direccion
        int cx = x + tamCelda / 2;
        int cy = y + tamCelda / 2;
        // 0=Norte, 1=Este, 2=Sur, 3=Oeste
        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { -1, 0, 1, 0 };
        int hx = cx + dx[direccion] * (tamCelda / 2 - 4);
        int hy = cy + dy[direccion] * (tamCelda / 2 - 4);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(cx, cy, hx, hy);
    }

    private void pintarDatos(int altoTablero, Cromosoma.DatosMapa dm) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        int textY = offsetY + altoTablero + 20;
        int textX = offsetX;

        g2.drawString(
            String.format("Fitness final: %.2f", mejor.fitness),
            textX,
            textY
        );
        textY += 18;
        g2.drawString(
            String.format(
                "Fitness base (media 3 mapas): %.2f",
                mejor.fitnessBase
            ),
            textX,
            textY
        );
        textY += 18;
        g2.drawString(
            String.format("Profundidad: %d  |  Nodos: %d", mejor.profundidadMax, mejor.nodos),
            textX,
            textY
        );
        textY += 18;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(60, 60, 60));
        g2.drawString(
            String.format("Mapa %d  |  fitness base: %.2f", indiceMapa + 1, dm.fitnessBase),
            textX,
            textY
        );
        textY += 16;
        g2.setColor(Color.BLACK);
        g2.drawString(
            String.format("Muestras recogidas: %d", dm.muestrasRecogidas),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Casillas exploradas: %d", dm.casillasExploradas),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Pisadas en arena: %d", dm.pisadasArena),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Colisiones: %d", dm.colisiones),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Energía restante: %.1f", dm.energiaRestante),
            textX,
            textY
        );
    }
}
