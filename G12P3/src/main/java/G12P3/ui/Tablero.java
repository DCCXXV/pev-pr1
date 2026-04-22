package G12P3.ui;

import G12P3.ag.Cromosoma;
import java.awt.*;
import javax.swing.*;

public class Tablero extends JPanel {

    private int tamCelda = 28;

    private int[][] mapaBase;
    private Cromosoma mejor;

    private Graphics2D g2;
    private int offsetX;
    private int offsetY;

    public Tablero() {}

    public void setMapaBase(int[][] mapa) {
        this.mapaBase = mapa;
        this.mejor = null;
        repaint();
    }

    public void setMejor(Cromosoma mejor) {
        this.mejor = mejor;
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

        int anchoTablero = mapaBase[0].length * tamCelda;
        int altoTablero = mapaBase.length * tamCelda;
        offsetX = Math.max(10, (getWidth() - anchoTablero) / 2);
        offsetY = 10;

        // mapa para pintar: si ya hay un mejor, usamos su estado final (muestras recogidas quitadas)
        int[][] mapa = (mejor != null && mejor.mapaMuestra != null)
            ? mejor.mapaMuestra
            : mapaBase;
        boolean[][] visitado = (mejor != null) ? mejor.visitadoMuestra : null;

        for (int row = 0; row < mapaBase.length; row++) {
            for (int col = 0; col < mapaBase[0].length; col++) {
                int x = offsetX + col * tamCelda;
                int y = offsetY + row * tamCelda;
                int celda = mapa[row][col];

                // fondo segun tipo de terreno
                switch (celda) {
                    case 1 -> {
                        // muro
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
                        // arena
                        g2.setColor(new Color(230, 140, 40));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                    }
                    default -> {
                        // suelo
                        g2.setColor(new Color(55, 55, 60));
                        g2.fillRect(x, y, tamCelda, tamCelda);
                    }
                }

                // huella
                if (visitado != null && visitado[row][col] && celda != 1) {
                    g2.setColor(new Color(120, 220, 255, 100));
                    g2.fillRect(x, y, tamCelda, tamCelda);
                }

                // muestra cientifica
                if (celda == 2) {
                    int d = tamCelda - 10;
                    g2.setColor(new Color(255, 220, 40));
                    g2.fillOval(x + 5, y + 5, d, d);
                    g2.setColor(new Color(180, 120, 0));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawOval(x + 5, y + 5, d, d);
                }

                // borde
                g2.setColor(new Color(80, 80, 80));
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(x, y, tamCelda, tamCelda);
            }
        }

        // rover
        if (mejor != null) {
            pintarRover(
                mejor.posFilaFinal,
                mejor.posColFinal,
                mejor.direccionFinal
            );
            pintarDatos(altoTablero);
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

    private void pintarDatos(int altoTablero) {
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
            String.format("Tamaño AST: %d nodos", mejor.nodos),
            textX,
            textY
        );
        textY += 18;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(60, 60, 60));
        g2.drawString("Mapa 1", textX, textY);
        textY += 16;
        g2.setColor(Color.BLACK);
        g2.drawString(
            String.format("Muestras recogidas: %d", mejor.muestrasRecogidas),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Casillas exploradas: %d", mejor.casillasExploradas),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Pisadas en arena: %d", mejor.pisadasArena),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Colisiones: %d", mejor.colisiones),
            textX,
            textY
        );
        textY += 16;
        g2.drawString(
            String.format("Energía restante: %.1f", mejor.energiaRestante),
            textX,
            textY
        );
    }
}
