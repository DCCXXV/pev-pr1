package G12P2.ui;

import javax.swing.*;
import java.util.List;
import java.awt.*;

public class Tablero extends JPanel {

    //variable para la apariencia del tablero
    private int tamCelda = 22;

    //informacion del tablero
    private int[][] grid;
    private int[][] camaras;
    private int mejorFiness = 0;
    private List<List<int[]>> rutasDrones;

    //variables auxiliares del tablero
    Graphics2D g2 = null;
    int anchoTablero = 0;
    int altoTablero = 0;
    int offsetX = 0;
    int offsetY = 0;

    public Tablero(int[][] grid, int[][] camaras) {
        this.grid = grid;
        this.camaras = camaras;
    }

    public Tablero() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tamaño total del tablero
        anchoTablero = grid[0].length * tamCelda;
        altoTablero = grid.length * tamCelda;

        // Calcular desplazamiento para centrar
        offsetX = (getWidth() - anchoTablero) / 2;
        offsetY = (getHeight() - altoTablero) / 2;

        int x;
        int y;

        //indice de la camra actual
        int indice = 1;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                x = offsetX + col * tamCelda;
                y = offsetY + row * tamCelda;

                // Dibujar la celda según el valor
                switch (grid[row][col]) {
                    case 0: //dibuja pared
                        g2.setColor(Color.BLACK);
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        break;

                    case 1:
                        g2.setColor(Color.WHITE);
                        g2.fillRect(x, y, tamCelda, tamCelda);
                        break;

                    default: //dibuja vacio y el coste
                        double aux = (255 - (this.grid[row][col] - 3.0)/20 * 255);
                        g2.setColor(new Color(255, (int)aux, 0, 128));
                        g2.fillRect(x, y, tamCelda, tamCelda);

                        //pinta el coste de la casilla
                        Font fuente = new Font("Arial", Font.BOLD, 7);
                        g2.setFont(fuente);
                        g2.setColor(Color.BLACK);
                        String texto = String.valueOf(this.grid[row][col]);
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(texto, x, y+7);
                        break;
                }

                // Dibujar bordes del cuadrado
                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tamCelda, tamCelda);
            }
        }

        //pone las camaras
        for (int[] camara : camaras) {

            //coge la posicion de la camara en el grid
            int row = camara[0];
            int col = camara[1];

            //calcula su posicion relativa en el tablero
            x = offsetX + col * tamCelda;
            y = offsetY + row * tamCelda;

            //dibuja el punto en el centro
            g2.setColor(new Color(0, 208, 255, 168));
            int diametro = 17;
            int centroX = x + tamCelda / 2 - diametro / 2;
            int centroY = y + tamCelda / 2 - diametro / 2;
            g2.fillOval(centroX, centroY, diametro, diametro);

            //dibuja el indice de la camara
            Font fuente = new Font("Arial", Font.BOLD, 7);
            g2.setFont(fuente);
            g2.setColor(Color.BLACK);
            String texto = String.valueOf(indice);
            indice++;
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + tamCelda / 2 - 4;
            int textY = y + tamCelda / 2 + 5;
            g2.drawString(texto, textX, textY);
        }

        if (this.rutasDrones != null) {
            pintarLineas(this.rutasDrones);
        }
    }

    private void pintarMejorFitness() {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));

        String texto = String.valueOf(this.mejorFiness);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(texto);

        int textX = offsetX + (anchoTablero - textWidth) / 2;
        int textY = offsetY + altoTablero + fm.getAscent() + 10;

        g2.drawString(texto, textX, textY);
    }

    private void pintarLineas(List<List<int[]>> rutasDrones) {
        int[] prev = new int[2];
        int[] current = new int[2];
        for (List<int[]> ruta : rutasDrones) {
            prev[0] = ruta.get(0)[1];
            prev[1] = ruta.get(0)[0];
            for (int j = 1; j < ruta.size(); j++) {
                current[0] = ruta.get(j)[1];
                current[1] = ruta.get(j)[0];
                Color color = new Color(0, 204, 255, 128);
                pintarLinea(g2, prev, current, color);
                prev[0] = current[0];
                prev[1] = current[1];
            }
        }
    }

    private void pintarLinea(Graphics2D g2, int[] origen, int[] destino, Color color) {
        // Centro de la celda origen
        int x1 = offsetX + origen[1] * tamCelda + tamCelda / 2;
        int y1 = offsetY + origen[0] * tamCelda + tamCelda / 2;

        // Centro de la celda destino
        int x2 = offsetX + destino[1] * tamCelda + tamCelda / 2;
        int y2 = offsetY + destino[0] * tamCelda + tamCelda / 2;

        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x1, y1, x2, y2);
    }

    public void setTablero(int[][] grid, int[][] camaras, int mejorFitness, List<List<int[]>> rutasDrones) {
        this.grid = grid;
        this.camaras = camaras;
        this.mejorFiness = mejorFitness;
        this.rutasDrones = rutasDrones;
        this.repaint();
    }

    public void setRutas(List<List<int[]>> rutasDrones) {
        this.rutasDrones = rutasDrones;
    }
}
